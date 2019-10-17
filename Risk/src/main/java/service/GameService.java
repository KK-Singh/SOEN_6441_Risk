package service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import model.Continent;
import model.Country;
import model.Player;

public class GameService {

	Map<Integer, Country> countryMap = MapService.countryMap;
	Map<Integer, Continent> continentMap = MapService.continentMap;
	
	
	public Country getCountry(String name) {
		
		Iterator<Integer> ite = countryMap.keySet().iterator();

		while (ite.hasNext()) {
			int key = ite.next();
			if (countryMap.get(key).getName().equalsIgnoreCase(name)) {
				return countryMap.get(key);
			}
		}
		return null;

	}
	

	public void loadMapOnConsole(List<Player> players) {

		players.forEach(player -> {
			System.out.println("Player Name " + " : " + player.getPlayerName()); 
			System.out.println("Army Count " + " : " + player.getArmy());
			System.out.println("Total Countries : " + " : " + player.getAlloccupied_countries().size());	
			System.out.println("Countries ");
			player.getAlloccupied_countries().forEach(country -> {
				System.out.print("Country Name : "+ country.getName() + " : " + country.getNo_of_armies());
				country.getNeighbors().forEach(nc -> {
					if (!nc.getOwner_player().getPlayerName().equalsIgnoreCase(player.getPlayerName())) {
						System.out.print(" Neighbour Country : " + nc.getName() + " ");
					}
				});
				System.out.println();
			});
		});
	}

	public int getArmyCountBasedOnPlayers(List<Player> players) {
		switch (players.size()) {
		case 2:
			return 40;
		case 3:
			return 35;
		case 4:
			return 30;
		case 5:
			return 25;
		default:
			return 20;
		}
	}

	public void assignCountriesAtRandom(List<Player> players) {
		Map<Integer,Country> countryMap = MapService.countryMap;
		int count =0;
		int totalPlayer = players.size();
		int totalCountries = countryMap.size();
		
		
		while(countryMap.size()!=0) {
			
			if(count==totalPlayer){
				count =0;
			}
			
			int index = randomIndex(0,totalCountries);
			while(countryMap.get(index)==null) {
				index = randomIndex(0,totalCountries);
				if (countryMap.get(index)!=null)
					break;
			}
			
			Country country = countryMap.get(index);
			Player player = players.get(count);
			List<Country> playerCountries = player.getAlloccupied_countries();
			country.setNo_of_armies(1);
			player.setArmy(player.getArmy()-1);
			playerCountries.add(country);
			country.setOwner_player(player);
			player.setAlloccupied_countries(playerCountries);
			count++;
			countryMap.remove(index);
			
		}
	} 
	
	public Player getPlayerAtRandom(List<Player> players) {
		int index = randomIndex(0, players.size());
		return players.get(index);
	}
	
	public int randomIndex(int min, int max) {
		return (int) (Math.random() * ((max - min) + 1)) + min;
	}

	public static void main(String[] args) {
		MapService service = new MapService();
		GameService gService = new GameService();

		try {
			service.readFile("C:\\Users\\Shivam\\Downloads\\RISK-1.0\\Resources\\Asia.map");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Player p1 = new Player("P1");
		Player p2 = new Player("P2");

		gService.loadMapOnConsole(Arrays.asList(p1, p2));

	}
	
	public void reinforcementArmy(Player player) {
		int totalCountries = player.getAlloccupied_countries().size();
		int totalArmy = 0;
		
		if(totalCountries<9) {
			totalArmy+=3;
		}else {
			totalArmy+=(totalCountries/3);
		}
		
		Set<Continent> visitedContinent = new HashSet<>();
		
		List<Country> countries = player.getAlloccupied_countries();
		
		for(int i=0;i<countries.size();i++) {
			
			Continent continent = countries.get(i).getContinent();
			
			if(ifContinetOwned(continent, player) && !visitedContinent.contains(continent)) {
				totalArmy+=continent.getArmyValue();
			}
			visitedContinent.add(continent);
		}
		player.setArmy(totalArmy);
	}
	

	public void fillPlayerCountry(Player player, String countryName, int armyCount) {
		player.getAlloccupied_countries().forEach(country->{
			if(country.getName().equalsIgnoreCase(countryName)) {
				country.setNo_of_armies(country.getNo_of_armies()+armyCount);
				player.setArmy(player.getArmy()-armyCount);
			}
			
		});
		
	}
	
	
	public String fortifyPosition(Country startCountry, Country destinationCountry, int armyToMove) {
		int startArmy = startCountry.getNo_of_armies();
		if(startArmy<=1 || (startArmy-armyToMove)<1 || !ifPathExists(startCountry, destinationCountry)) {
			return "Cannot move army from the Country";
		}else {
			destinationCountry.setNo_of_armies(destinationCountry.getNo_of_armies()+armyToMove);
			destinationCountry.setNo_of_armies(startArmy-armyToMove);
			return "Army Moved";
		}
	}
	

	private boolean ifPathExists(Country startCountry, Country endCountry) {
		Queue<Country> nextToVisit = new LinkedList<Country>();
		Set<Country> visited = new HashSet<>();
		nextToVisit.add(startCountry);
		while(!nextToVisit.isEmpty()) {
			Country country = nextToVisit.remove();
			String playerName = country.getOwner_player().getPlayerName();
			if(country.equals(endCountry)) {
				return true;
			}
			if(visited.contains(country))
				continue;
			visited.add(country);
			
			country.getNeighbors().forEach(nc->{
				if(nc.getOwner_player().getPlayerName().equalsIgnoreCase(playerName))
					nextToVisit.add(nc);
			});
		}
		
		return false;
	}
	
	private boolean ifContinetOwned(Continent continent, Player player) {
		List<Country> countries = player.getAlloccupied_countries();
		List<Country> countriesInContinent = continent.getCountries();
		
		for(int i=0;i<countriesInContinent.size();i++) {
			if(!countries.contains(countriesInContinent.get(0))) {
				return false;
			}
		}
		return true;
	}
	
}

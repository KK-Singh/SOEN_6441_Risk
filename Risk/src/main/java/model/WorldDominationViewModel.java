package model;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

public class WorldDominationViewModel extends Observable{

	
	private List<Player> playersList;
	
	private Map<Player,Double> playerCoverage;
	
	private Map<Player,Set<Continent>> playerContinentMapping;
	
	private Map<Player,Integer> playerArmy;
	
	public WorldDominationViewModel(List<Player> players) {
		this.playersList = players;
		
		playerCoverage = new LinkedHashMap<>();
		playerContinentMapping = new LinkedHashMap<>();
		playerArmy = new LinkedHashMap<>();
		
		players.forEach(player->{
			playerCoverage.put(player, 0d);
			playerContinentMapping.put(player, new HashSet<>());
			playerArmy.put(player, 0);
		});
				
	}

	public Map<Player, Double> getPlayerCoverage() {
		return playerCoverage;
	}

	public Map<Player, Set<Continent>> getPlayerContinentMapping() {
		return playerContinentMapping;
	}

	public Map<Player, Integer> getPlayerArmy() {
		return playerArmy;
	}

	
	public void stateUpdate(Map<Integer,Continent> continentMap, Map<Integer,Country> countryMap) {
	
		
		double coveredMap;
		Set<Continent> ownedContinent;
		int army;
		double totalCountries = countryMap.size();
		
		for(Player player: playersList) {
			
			coveredMap = (player.getPlayerCountries().size()/totalCountries)*100;
			playerCoverage.put(player, coveredMap);
			
			ownedContinent = getAllContinentRuled(player,continentMap);
			playerContinentMapping.put(player, ownedContinent);
			
			army = getPlayerTotalArmy(player);
			playerArmy.put(player, army);
			
		}

		setChanged();
		notifyObservers(this);
	}

	private Set<Continent> getAllContinentRuled(Player player, Map<Integer, Continent> continentMap) {
		List<Country> pc = player.getPlayerCountries();
		Set<Continent> ownedContinent = new HashSet<>();
		
		for(Entry<Integer, Continent> entry : continentMap.entrySet()) {
			boolean ifCovered = true;
			
			List<Country> continentCountries = entry.getValue().getCountries();
			
			for(Country contCountry:continentCountries) {
				if(!pc.contains(contCountry)) {
					ifCovered = false;
					break;
				}
			}
			if(ifCovered)
				ownedContinent.add(entry.getValue());
		}
		return ownedContinent;
	}

	private int getPlayerTotalArmy(Player player) {
		int totalArmies = 0;
		for(Country pc : player.getPlayerCountries()) {
			totalArmies+=pc.getArmyCount();
		}
		totalArmies+=player.getArmy();
		return totalArmies;
	}
	
	


}

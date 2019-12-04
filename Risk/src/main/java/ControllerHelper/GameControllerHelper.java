package ControllerHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import model.AggressiveStrategy;
import model.BenevolentStrategy;
import model.CardViewModel;
import model.CheaterStrategy;
import model.Continent;
import model.Country;
import model.GameObject;
import model.GameObjectBuilder;
import model.HumanStrategy;
import model.PhaseViewModel;
import model.Player;
import model.RandomStrategy;
import model.StrategyEnum;
/**
* This GameService file can contains the core functions
* for the risk game.
* @author  Yash
* @version 1.2
*/
import model.StrategyInterface;

public class GameControllerHelper {

	/**
	 * This method is used to load the map on the console.
	 * 
	 * @param players
	 *            list of players
	 */
	public void loadMapOnConsole(List<Player> players) {

		players.forEach(player -> {
			System.out.println("Player Name " + " : " + player.getPlayerName());
			System.out.println("Army Count " + " : " + player.getArmy());
			System.out.println("Total Countries : " + " : " + player.getPlayerCountries().size());
			System.out.println("Countries ");
			player.getPlayerCountries().forEach(country -> {
				System.out.print("Country Name : " + country.getName() + " : " + country.getArmyCount());
				country.getNeighbors().forEach(nc -> {
					if (!nc.getCountryOwner().getPlayerName().equalsIgnoreCase(player.getPlayerName())) {
						System.out.print(" Neighbour Country : " + nc.getName() + " ");
					}
				});
				System.out.println();
			});
		});
	}

	/**
	 * This method is used to get army count based on number of players.
	 * 
	 * @param players
	 *            list of players
	 * @return int number of army for particular player
	 */
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

	/**
	 * This method is used to assign countries randomly to the particular player
	 * 
	 * @param players
	 *            list of players
	 */
	public void assignCountriesAtRandom(List<Player> players) {
		Map<Integer, Country> countryMap = new HashMap<Integer, Country>();
		MapControllerHelper.getObject().countryMap.forEach((k, v) -> countryMap.put(k, v));
		int count = 0;
		int totalPlayer = players.size();
		int totalCountries = countryMap.size();

		while (countryMap.size() != 0) {

			if (count == totalPlayer) {
				count = 0;
			}

			int index = randomIndex(0, totalCountries);
			while (countryMap.get(index) == null) {
				index = randomIndex(0, totalCountries);
				if (countryMap.get(index) != null)
					break;
			}

			Country country = countryMap.get(index);
			Player player = players.get(count);
			List<Country> playerCountries = player.getPlayerCountries();
			country.setArmyCount(1);
			player.setArmy(player.getArmy() - 1);
			playerCountries.add(country);
			country.setCountryOwner(player);
			player.setPlayerCountries(playerCountries);
			count++;
			countryMap.remove(index);

		}
	}

	/**
	 * This method is used to get player randomly
	 * 
	 * @param players
	 *            list of players
	 * @return Player it returns random player
	 */
	public Player getPlayerAtRandom(List<Player> players) {
		int index = randomIndex(0, players.size());
		return players.get(index);
	}

	/**
	 * This method is used to index randomly
	 * 
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @return int it returns random index
	 */
	public int randomIndex(int min, int max) {
		return (int) (Math.random() * ((max - min) + 1)) + min;
	}

	/**
	 * This is java main method
	 * 
	 * @param args
	 *            arguments for main method
	 */
	public static void main(String[] args) {
		MapControllerHelper service = MapControllerHelper.getObject();
		GameControllerHelper gService = new GameControllerHelper();

		try {
			service.readFile("Resources\\Asia.map");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Player p1 = new Player("P1");
		Player p2 = new Player("P2");

		gService.loadMapOnConsole(Arrays.asList(p1, p2));

	}

	/**
	 * This is method is used to reinforce the army by setting army to player object
	 * 
	 * @param player
	 *            a single player object
	 */
	public void reinforcementArmy(Player player) {
		int totalCountries = player.getPlayerCountries().size();
		int totalArmy = player.getArmy();

		if (totalCountries < 9) {
			totalArmy += 3;
		} else {
			totalArmy += (totalCountries / 3);
		}

		Set<Continent> visitedContinent = new HashSet<>();

		List<Country> countries = player.getPlayerCountries();

		for (int i = 0; i < countries.size(); i++) {

			Continent continent = countries.get(i).getContinent();

			if (ifContinetOwned(continent, player) && !visitedContinent.contains(continent)) {
				totalArmy += continent.getArmyValue();
			}
			visitedContinent.add(continent);
		}
		player.setArmy(totalArmy);
	}

	/**
	 * This is method is used to set the army on the country
	 * 
	 * @param player
	 *            a single player object
	 * @param countryName
	 *            name of the country
	 * @param armyCount
	 *            number of armies
	 */
	public void fillPlayerCountry(Player player, String countryName, int armyCount) {
		player.getPlayerCountries().forEach(country -> {
			if (country.getName().equalsIgnoreCase(countryName)) {
				country.setArmyCount(country.getArmyCount() + armyCount);
				player.setArmy(player.getArmy() - armyCount);
			}

		});

	}

	/**
	 * This is method is used to validate the fortification from one country to
	 * another
	 * 
	 * @param fromCountry
	 *            a single country object of source/start country
	 * @param toCountry
	 *            a single country object of destination country
	 * @param armyToMove
	 *            number of armies
	 * @return ArrayList a list of the Strings
	 */
	public List<String> validateFortification(Country fromCountry, Country toCountry, int armyToMove) {
		int startArmy = fromCountry.getArmyCount();
		List<Country> fortifiableCountries = getFortifiableCountries(fromCountry);
		List<String> result = new ArrayList<>();
		if (startArmy <= 1 || (startArmy - armyToMove) < 1) {
			result.add("Cannot move army from the Country");
		}

		if (!fortifiableCountries.contains(toCountry)) {
			result.add("Fortifiable countries for the given country are : ");
			fortifiableCountries.forEach(c -> {
				result.add(c.getName());
			});
		}

		return result;
	}

	/**
	 * This is method is used to get the all possible fortifiable countries of the
	 * country
	 * 
	 * @param territory
	 *            a single country object
	 * @return ArrayList a list of all the fortifiable countries
	 */
	public List<Country> getFortifiableCountries(Country territory) {
		Player player = territory.getCountryOwner();
		List<Country> fortifiableTerritories = new ArrayList<>();

		Queue<Country> queue = new LinkedList<>();
		queue.add(territory);
		Country t;

		// doing BFS to get all the territory which can be fortified by given territory.
		while (queue.size() > 0) {
			t = queue.poll();
			for (Country neighbours : t.getNeighbors()) {
				if (neighbours.getCountryOwner() == player && !fortifiableTerritories.contains(neighbours)) {
					fortifiableTerritories.add(neighbours);
					queue.add(neighbours);
				}
			}
		}
		if (fortifiableTerritories.contains(territory))
			fortifiableTerritories.remove(territory);
		return fortifiableTerritories;
	}

	/**
	 * This is method is used to get check the ownership of the continent
	 * 
	 * @param continent
	 *            a single country object
	 * @param player
	 *            a player object
	 * @return boolean true if continent is owned else false
	 */
	public boolean ifContinetOwned(Continent continent, Player player) {
		List<Country> countries = player.getPlayerCountries();
		List<Country> countriesInContinent = continent.getCountries();

		for (int i = 0; i < countriesInContinent.size(); i++) {
			if (!countries.contains(countriesInContinent.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This is method is used to check if game is ended or not
	 * 
	 * @param player
	 *            a player object
	 * @param totalCountries
	 *            total count of the countries
	 * @return boolean true if game is ended else false
	 */
	public boolean ifGameEnded(Player player, int totalCountries) {
		if (player.getPlayerCountries().size() == totalCountries)
			return true;
		return false;
	}

	/**
	 * This is method is used to validate number of dies
	 * 
	 * @param attackerCountry
	 *            a attacker country object
	 * @param defenderCountry
	 *            a defender country object
	 * @param attackerTotalDice
	 *            total number of dies of attacker
	 * @param defenderTotalDice
	 *            total number of dies of defender
	 * @return boolean true if game is ended else false
	 */
	public String validateSelectedNumberOfDice(Country attackerCountry, Country defenderCountry,
			String attackerTotalDice, String defenderTotalDice) {
		int totalAttackerDice, totalDefenderDice;

		try {
			totalAttackerDice = Integer.parseInt(attackerTotalDice);
			totalDefenderDice = Integer.parseInt(defenderTotalDice);
		} catch (NumberFormatException e) {
			return "Enter Valid Number for Attacker and Defender";
		}

		if (totalAttackerDice > 3 || totalAttackerDice < 1 || totalAttackerDice > attackerCountry.getArmyCount() - 1) {
			return "Selected attacker can roll min 1 and max "
					+ (3 > attackerCountry.getArmyCount() - 1 ? attackerCountry.getArmyCount() - 1 : 3);
		}

		if (totalDefenderDice > 2 || totalDefenderDice < 1 || totalDefenderDice > defenderCountry.getArmyCount()) {
			return "Selected defender can roll min 1 and max "
					+ (2 > defenderCountry.getArmyCount() ? defenderCountry.getArmyCount() : 2);
		}

		return "ValidChoice";
	}

	public int validatePlayerStrategyMappingForTournament(Map<Player, StrategyEnum> map) {

		Iterator<Player> ite = map.keySet().iterator();

		while (ite.hasNext()) {
			Player p = ite.next();
			StrategyEnum playerEnum = map.get(p);
			if (playerEnum == null) {
				return -1;
			} else if (playerEnum.equals(StrategyEnum.HUMAN)) {
				return -2;
			}

		}

		return 0;
	}

	public StrategyInterface getPlayerStrategy(Map<Player, StrategyEnum> mapping, Player curPlayer) {
		StrategyEnum playerEnum = mapping.get(curPlayer);
		if (playerEnum == null) {
			return null;
		}
		switch (playerEnum) {
		case HUMAN:
			return new HumanStrategy();
		case BENEVOLENT:
			return new BenevolentStrategy();
		case RANDOM:
			return new RandomStrategy();
		case CHEATER:
			return new CheaterStrategy();
		case AGGRESSIVE:
			return new AggressiveStrategy();
		default:
			return null;
		}
	}

	public void nonHumanStartupPhase(Player curPlayer, int armyCount, PhaseViewModel phaseViewModel) {

		phaseViewModel.setCurrentPhaseInfo("Startup phase for non human");
		phaseViewModel.setCurrentPlayer(curPlayer.getPlayerName());
		Country con = curPlayer.getPlayerCountries().get(randomIndex(0, curPlayer.getPlayerCountries().size() - 1));

		con.setArmyCount(con.getArmyCount() + armyCount);
		phaseViewModel.setCurrentPhaseInfo(
				phaseViewModel.getCurrentPhaseInfo() + "\n Moved " + armyCount + " to " + con.getName());
		curPlayer.setArmy(curPlayer.getArmy() - armyCount);
	}

	public boolean saveGame(File fileToSave, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap,
			List<Player> playerList, Player currentPlayer, String currentPhase, CardViewModel cardExchangeViewModel,
			Integer playerIndexForStartUpPhase) {
		FileOutputStream fo;
		ObjectOutputStream out = null;

		try {
			fo = new FileOutputStream(fileToSave);
			out = new ObjectOutputStream(fo);
			GameObject go = new GameObjectBuilder().setPlayerList(playerList)
					.setCardExchangeViewModel(cardExchangeViewModel).setContinentMap(continentMap)
					.setCountryMap(countryMap).setCurrentPhase(currentPhase).setCurrentPlayer(currentPlayer)
					.setPlayerIndexForStartUpPhase(playerIndexForStartUpPhase).build();
			out.writeObject(go);
			out.close();
			fileToSave = null;
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}

	public GameObject resumeSavedGame(File fileToLoad) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		GameObject go = null;
		try {
			fis = new FileInputStream(fileToLoad);
			ois = new ObjectInputStream(fis);
			go = (GameObject) ois.readObject();
			if (go != null) {
				ois.close();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return go;
	}
}

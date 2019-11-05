package model;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

/**
 * @author Gurwinder kaur
 */
public class WorldDominationViewModel extends Observable{
/**
* playersList : Represents player list 	
 */
	
	private List<Player> playersList;	
	
	/**
	 * playerCoverage : Represents player coverage
	 */
	
	private Map<Player,Double> playerCoverage;	
	/**
	 * playerContinentMapping : player continent mapping
	 */
	private Map<Player,Set<Continent>> playerContinentMapping;	
	/**
	 * playerArmy : Represents player army
	 */
	private Map<Player,Integer> playerArmy;
	/**
	 * constructor for initialization of WorldDominationViewModel
	 * @param players: set players
	 */
	 
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
/**
 * This method get the player coverage
 * @return player coverage: gets the player coverage
 */
	public Map<Player, Double> getPlayerCoverage() {
		return playerCoverage;
	}
	/**
	 * This method get the player continent mapping 
	 * @return playerContinentMapping: gets the player continent mapping 
	 */
	public Map<Player, Set<Continent>> getPlayerContinentMapping() {
		return playerContinentMapping;
	}
/**
 * This method get the player army
 * @return playerArmy: gets the player army
 */
	public Map<Player, Integer> getPlayerArmy() {
		return playerArmy;
	}

	/** 
	 * This method update the status of continent and country 
	 * @param continentMap : status of continent map 
	 * @param countryMap : status of country map
	 */
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
/**
 * This method get the continent ruled
 * @param player : get the player
 * @param continentMap : get the continent map 
 * @return ownedContinent : owned continent 
 */
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
/**
 * This method get the total player army  
 * @param player : player 
 * @return totalArmies : get the total armies
 */
	private int getPlayerTotalArmy(Player player) {
		int totalArmies = 0;
		for(Country pc : player.getPlayerCountries()) {
			totalArmies+=pc.getArmyCount();
		}
		totalArmies+=player.getArmy();
		return totalArmies;
	}
	
	


}

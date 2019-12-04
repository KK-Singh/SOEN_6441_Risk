package model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * GameObject class implement Serializable interface 
 * @author kumar
 * @version 1.3
 *
 */

public class GameObject implements Serializable{

	private static final long serialVersionUID = 1L;

	Map<Integer, Continent> continentMap;
	
	Map<Integer, Country> countryMap;
	
	List<Player> playerList;
	
	Player currentPlayer;
	
	String currentPhase;
	
	CardViewModel cardExchangeViewModel;
	
	Integer playerIndexForStartUpPhase;
	
	/**
	 * This constructor call methods by using its object
	 * @param continentMap   Map of the continent
	 * @param countryMap  Map of the country
	 * @param playerList List of all the player
	 * @param currentPlayer player currently playing game
	 * @param currentPhase phase of current game
	 * @param cardExchangeViewModel view of card
	 * @param playerIndexForStartUpPhase index of player
	 */
	public GameObject(Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap,
			List<Player> playerList, Player currentPlayer, String currentPhase, CardViewModel cardExchangeViewModel,Integer playerIndexForStartUpPhase) {
		super();
		this.continentMap = continentMap;
		this.countryMap = countryMap;
		this.playerList = playerList;
		this.currentPlayer = currentPlayer;
		this.currentPhase = currentPhase;
		this.cardExchangeViewModel = cardExchangeViewModel;
		this.playerIndexForStartUpPhase = playerIndexForStartUpPhase;
	}
	/**
	 * This method get object of Serialversionuid object 
	 * @return version UID
	 */
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 *This method get object of PlayerIndexForStartUpPhase object
	 * @return index of player
	 */
	public Integer getPlayerIndexForStartUpPhase() {
		return playerIndexForStartUpPhase;
	}

	 /**
	 * This method get object of ContinentMap object
	 * @return map of continent
	 */
	public Map<Integer, Continent> getContinentMap() {
		return continentMap;
	}

	/**
	 *  This method get object of CountryMap object
	 * @return map of country
	 */
	public Map<Integer, Country> getCountryMap() {
		return countryMap;
	}

	/**
	 * This method get object of list of player 
	 * @return list of players
	 */
	public List<Player> getPlayerList() {
		return playerList;
	}

	/**
	 *  This method get object of currentplayer
	 * @return current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * This method get object of getCurrentPhase
	 * @return current phase of game
	 */
	public String getCurrentPhase() {
		return currentPhase;
	}

	/**
	 *  This method get object of getCardExchangeViewModel
	 * @return model view of card exchange
	 */
	public CardViewModel getCardExchangeViewModel() {
		return cardExchangeViewModel;
	}
	
	
	
}

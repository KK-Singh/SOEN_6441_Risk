package model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Map<Integer, Continent> continentMap;
	
	Map<Integer, Country> countryMap;
	
	List<Player> playerList;
	
	Player currentPlayer;
	
	String currentPhase;
	
	CardViewModel cardExchangeViewModel;
	
	Integer playerIndexForStartUpPhase;
	

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getPlayerIndexForStartUpPhase() {
		return playerIndexForStartUpPhase;
	}

	public Map<Integer, Continent> getContinentMap() {
		return continentMap;
	}

	public Map<Integer, Country> getCountryMap() {
		return countryMap;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public String getCurrentPhase() {
		return currentPhase;
	}

	public CardViewModel getCardExchangeViewModel() {
		return cardExchangeViewModel;
	}
	
	
	
}

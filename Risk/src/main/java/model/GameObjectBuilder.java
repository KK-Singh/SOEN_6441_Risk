package model;

import java.util.List;
import java.util.Map;

public class GameObjectBuilder{

	Map<Integer, Continent> continentMap;
	
	Map<Integer, Country> countryMap;
	
	List<Player> playerList;
	
	Player currentPlayer;
	
	String currentPhase;
	
	CardViewModel cardExchangeViewModel;

	Integer playerIndexForStartUpPhase;
	
	public GameObjectBuilder setPlayerIndexForStartUpPhase(Integer playerIndexForStartUpPhase) {
		this.playerIndexForStartUpPhase = playerIndexForStartUpPhase;
		return this;
	}

	public GameObjectBuilder setContinentMap(Map<Integer, Continent> continentMap) {
		this.continentMap = continentMap;
		return this;
	}

	public GameObjectBuilder setCountryMap(Map<Integer, Country> countryMap) {
		this.countryMap = countryMap;
		return this;
	}

	public GameObjectBuilder setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
		return this;
	}

	public GameObjectBuilder setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		return this;
	}

	public GameObjectBuilder setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
		return this;
	}

	public GameObjectBuilder setCardExchangeViewModel(CardViewModel cardExchangeViewModel) {
		this.cardExchangeViewModel = cardExchangeViewModel;
		return this;
	}
	
	
	public GameObject build() {
		return new GameObject(continentMap, countryMap, playerList, currentPlayer, currentPhase, cardExchangeViewModel,playerIndexForStartUpPhase);
	}

	
	
	
}

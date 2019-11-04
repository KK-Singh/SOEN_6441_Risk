package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;

import controller.MapController;

public class CardViewModel extends Observable {

	Player curPlayer;

	Card card;

	Map<Player, Queue<Card>> playerCardMap;

	Queue<Card> allCards;

	int totalExchange;

	boolean ifPlayerWonCard;

	public static final String INFANTARY_STRING = "Infantary";

	public static final String CAVALRY_STRING = "Cavalry";

	public static final String ARTILLERY_STRING = "Artillery";

	Country cardOwnedCountry;
	
	MapController mapController = new MapController();

	public CardViewModel() {
		playerCardMap = new HashMap<>();
		ifPlayerWonCard = false;
		totalExchange = 0;
		allCards = new LinkedList<>();
		cardOwnedCountry = null;
		initializeCards();

	}

	public String getTypeOfCard() {
		return card.typeOfCard;
	}

	public Country getCountryOfTheCard() {
		return card.cardCountry;
	}

	public Player getCurPlayer() {
		return curPlayer;
	}

	public void setCurrentPlayerView(Player curPlayer) {
		this.curPlayer = curPlayer;
		setChanged();
		notifyObservers(this);
	}

	public void setAllCards(Queue<Card> allCards) {
		this.allCards = allCards;
	}

	public Queue<Card> getAllCards() {
		return allCards;
	}

	public Queue<Card> getCardOfCurrentPlayer(Player curPlayer) {
		Queue<Card> playerCards = playerCardMap.get(curPlayer);
		if (playerCards != null) {
			return playerCards;
		} else {
			return new LinkedList<>();
		}
	}

	public void setCurPlayerCards(Player curPlayer, Queue<Card> playersCards) {
		playerCardMap.put(curPlayer, playersCards);
	}

	public void setTotalExchanges(int totalExchange) {
		this.totalExchange = totalExchange;
	}

	public int getTotalExchange() {
		return this.totalExchange;
	}

	public void setArmyCountOfPlayer(Player curPlayer, int cardOfPlayerCountryExchanged, Country cardOwnedCountry) {

		int armyCount = fetchArmyCount();
		armyCount += cardOfPlayerCountryExchanged;
		if (totalExchange <= 6) {
			curPlayer.setArmy(curPlayer.getArmy() + armyCount);
		} else {
			for (int i = 1; i <= totalExchange - 6; i++) {
				armyCount += 5;
			}
			curPlayer.setArmy(curPlayer.getArmy() + armyCount);
		}

	}

	public Country getCardOwnedCountry() {
		return cardOwnedCountry;
	}

	public void setCardOwnedCountry(Country cardOwnedCountry) {
		this.cardOwnedCountry = cardOwnedCountry;
	}

	private int fetchArmyCount() {
		switch (totalExchange) {
		case 1:
			return 4;
		case 2:
			return 6;
		case 3:
			return 8;
		case 4:
			return 10;
		case 5:
			return 12;
		default:
			return 15;
		}

	}

	public boolean getIfPlayerWonCard() {
		return ifPlayerWonCard;
	}
	
	public void setIfPlayerWonCard(boolean ifPlayerWonCard) {
		this.ifPlayerWonCard = ifPlayerWonCard;
	}
	
	private void initializeCards() {
		String [] cardType = {INFANTARY_STRING, CAVALRY_STRING, ARTILLERY_STRING};
		Queue<Card> allCards = new LinkedList<>();
		Map<Integer,Country> countryMap = mapController.getCountryMap(); 
		Iterator<Integer> ite = countryMap.keySet().iterator();
		
		int index =0;
		while(ite.hasNext()) {
			int key = ite.next();
			Country country = countryMap.get(key);
			if(index ==3)
				index =0;
			String typeOfCard = cardType[index];
			Card card = new Card(typeOfCard, country);
			allCards.add(card);
			index++;
		}
		setAllCards(allCards);
	}
	
	public void giveCardToPlayer(Player attacker) {
		Queue<Card> allCards = getAllCards();
		Queue<Card> cardOfPlayer = getCardOfCurrentPlayer(attacker);
		
		Card topCard = allCards.remove();
		
		if(!cardOfPlayer.isEmpty()) {
			cardOfPlayer.add(topCard);
		}else {
			cardOfPlayer.add(topCard);
		}
		
		setCurPlayerCards(attacker, cardOfPlayer);
	}

}

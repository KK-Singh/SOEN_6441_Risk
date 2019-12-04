package model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;
import controller.MapController;
/**
@author Gurwinder Kaur 
*/
public class CardViewModel extends Observable implements Serializable
{
/**
 *  curPlayer : Represents curPlayer
	 
 */
	Player curPlayer;
	/**
	 * card : Represents card
	 */
	Card card;
	/**
	 * playerCardMap : Represents playerCardMap
	 */
	Map<Player, Queue<Card>> playerCardMap;
	/**
	 * allCards : Represents all cards
	 */
	Queue<Card> allCards;
	/**
	 * totalExchange : Represents totalExachange
	 */
	int totalExchange;
	/**
	 * ifPlayerWonCard : Represents if player won card
	 */
	boolean ifPlayerWonCard;
	/**
	 * INFANTARY_STRING : Represents Infantry
	 */
	public static final String INFANTARY_STRING = "Infantary";
	/**
	 * 	CAVALRY_STRING : Represents Cavalry
	 */
	public static final String CAVALRY_STRING = "Cavalry";
	/**
	 * ARTILLERY_STRING : Represents artillery
	 */
	public static final String ARTILLERY_STRING = "Artillery";
	Country cardOwnedCountry;
	
	MapController mapController = new MapController();
	/**
	 * This method is constructor for initialization of card
	 */
	public CardViewModel() {
		playerCardMap = new HashMap<>();
		ifPlayerWonCard = false;
		totalExchange = 0;
		allCards = new LinkedList<>();
		cardOwnedCountry = null;
		initializeCards();
	}

	/**
	 * This method gets the card type 
	 * @return typrOdCard : type of card
	 */
	public String getTypeOfCard() {
		return card.typeOfCard;
	}
	/**
	 * This method gets the card country 
	 * @return cardCounty : country card
	 */
	public Country getCountryOfTheCard() {
		return card.cardCountry;
	}
	/**
	 * This method get the cur player
	 * @return curPlayer : cur player
	 */
	public Player getCurPlayer() {
		return curPlayer;
	}
	/**
	 * This method set the current player view 
	 * @param curPlayer : cur player
	 */
	public void setCurrentPlayerView(Player curPlayer) {
		this.curPlayer = curPlayer;
		setChanged();
		notifyObservers(this);
	}
	/**
	 * Sets the value for allCards
	 * @param allCards : all the cards 
	 */
	public void setAllCards(Queue<Card> allCards) {
		this.allCards = allCards;
	}
	/**
	 * This method gets allCards
	 * @return allCards : all type of cards 
	 */
	public Queue<Card> getAllCards() {
		return allCards;
	}
/**
 * this method gets the card of current player
 * @param curPlayer: cur player
 * @return list of current players
 */
	public Queue<Card> getCardOfCurrentPlayer(Player curPlayer) {
		Queue<Card> playerCards = playerCardMap.get(curPlayer);
		if (playerCards != null) {
			return playerCards;
		} else {
			return new LinkedList<>();
		}
	}
/**
 * This method sets the player cards
 * @param curPlayer : cur player, playersCards: player cards
 */
	public void setCurPlayerCards(Player curPlayer, Queue<Card> playersCards) {
		playerCardMap.put(curPlayer, playersCards);
	}
/**
 * setTotalExchanges() to set the value of totalExchange
 * @param totalExchange : total exchange 
 */
	public void setTotalExchanges(int totalExchange) {
		this.totalExchange = totalExchange;
	}
/**
 * This method gets the totalExchange
 * @return totalExchange : gets the total exchange 
 */
	public int getTotalExchange() {
		return this.totalExchange;
	}
/**
 *This method set the Army count of player
 *@param curplayer : cur Player, cardOfPlayerCountryExchanged:card of the player country exchanged, cardOwnedCountry:card owned by Country
 */
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
	/**
     * This method gets the card Owned by Country
     * @return cardOwnedCountry : get the card owned by country
     */	
	public Country getCardOwnedCountry() 
	{
		return cardOwnedCountry;
	}
	/**
     *This method sets the card Owned by Country
     * @param cardOwnedCountry : type of card owned by Country
     */	
	public void setCardOwnedCountry(Country cardOwnedCountry) {
		this.cardOwnedCountry = cardOwnedCountry;
	}
/**
 *This method fetch the total army count 
 */
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
/**
 *getIfPlayerWonCard() to return the player won card status 
 *@return ifPlayerWonCard : gets the boolean value if player won
 */
	public boolean getIfPlayerWonCard() {
		return ifPlayerWonCard;
	}
	/**
	 * This method set the player won card status 
	 * @param ifPlayerWonCard : set the status if player won
	 */
	public void setIfPlayerWonCard(boolean ifPlayerWonCard) 
	{
		this.ifPlayerWonCard = ifPlayerWonCard;
	}
	/**
	 * This method initialize the cards
	 */
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
	/**
	 * This method gives card to players
	 */
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

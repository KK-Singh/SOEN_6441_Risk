package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ControllerHelper.MapControllerHelper;
import javafx.util.Pair;

/**
 * @author Gurwinder Kaur
 */
public class Player implements Serializable {
	/**
	 * name : Represents name
	 */
	private String name;
	/**
	 * player_id : Represents player id
	 */
	private int player_id;
	/**
	 * cards : Represents cards
	 */
	private List<Card> cards;
	/**
	 * army : Represents army
	 */
	private int army;
	/**
	 * playerCountries : Represents players country
	 */
	private List<Country> playerCountries;

	private StrategyInterface playerStrategy;

	public StrategyInterface getPlayerStrategy() {
		return playerStrategy;
	}

	public void setPlayerStrategy(StrategyInterface playerStrategy) {
		this.playerStrategy = playerStrategy;
	}

	/**
	 * This constructor is for initialization of Player
	 * 
	 * @param name
	 *            : set the name
	 */
	public Player(String name) {
		this.name = name;
		this.playerCountries = new ArrayList<>();
		this.cards = new ArrayList<>();
	}

	/**
	 * This method get the player name
	 * 
	 * @return name : get the player name
	 */
	public String getPlayerName() {
		return name;
	}

	/**
	 * This method set the player name
	 * 
	 * @param name
	 *            : sets the player name
	 */
	public void setPlayerName(String name) {
		this.name = name;
	}

	/**
	 * This method get the player id
	 * 
	 * @return player id : get the player id
	 */
	public int getPlayer_id() {
		return player_id;
	}

	/**
	 * This method set the player id
	 * 
	 * @param player_id
	 *            : sets the player id
	 */
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	/**
	 * This method get the cards
	 * 
	 * @return cards
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * this method set the cards
	 * 
	 * @param cards
	 *            : set the cards
	 */
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	/**
	 * this method get the army
	 * 
	 * @return army : army
	 */
	public int getArmy() {
		return army;
	}

	/**
	 * This method set the army
	 * 
	 * @param army
	 *            : army
	 */
	public void setArmy(int army) {
		this.army = army;
	}

	/**
	 * This method get the player country
	 * 
	 * @return playerCountries : get the player country
	 */
	public List<Country> getPlayerCountries() {
		return playerCountries;
	}

	/**
	 * This method set the player countries
	 * 
	 * @param alloccupied_countries
	 *            : set the player country
	 */
	public void setPlayerCountries(List<Country> alloccupied_countries) {
		this.playerCountries = alloccupied_countries;
	}

	public void reinforcementPhase(Country reinforcementCountry, int noOfArmies, PhaseViewModel phaseViewModel) {
		playerStrategy.reinforcement(this, reinforcementCountry, noOfArmies, phaseViewModel);
	}

	public void fortify(Country fromCountry, Country toCountry, int armiesToMove, PhaseViewModel phaseViewModel) {
		playerStrategy.fortify(this, fromCountry, toCountry, armiesToMove, phaseViewModel);
	}

	/**
	 * This method for attack phase
	 * 
	 * @param attackerCountry
	 *            : attacker country
	 * @param defenderCountry
	 *            : defender country
	 * @param defender
	 *            : defender
	 * @param ifAllOut
	 *            : if all out
	 * @param totalAttackerDice
	 *            : total attacker dice
	 * @param totalDefenderDice
	 *            : total defender dice
	 * @param phaseViewModel
	 *            : phase view model
	 * @return pair : get the new pair
	 */
	public Pair<Boolean, Integer> attack(Country attackerCountry, Country defenderCountry, Player defender,
			boolean ifAllOut, int totalAttackerDice, int totalDefenderDice, PhaseViewModel phaseViewModel) {
		return playerStrategy.attack(this, attackerCountry, defenderCountry, defender, ifAllOut, totalAttackerDice,
				totalDefenderDice, phaseViewModel);
	}

}

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;
import service.MapService;
/**
@author Gurwinder Kaur 
*/
public class Player {
/**
 * 	name  : Represents name
 */
	private String name;
	/**
	 * player_id  : Represents player id
	 */
	private int player_id;
	/**
	 * cards : Represents cards
	 */
	private List<Card> cards;
	/**
	 * 	army : Represents army
	 */
	private int army;
	/**
	 * playerCountries : Represents players country
	 */
	private List<Country> playerCountries;

	/**
	 * This constructor is for initialization of Player
	 * @param name : set the name
	 */
	public Player(String name) {
		this.name = name;
		this.playerCountries = new ArrayList<>();
		this.cards = new ArrayList<>();
	}

	/**
	 * This method get the player name 
	 * @return name : get the player name 
	 */
	public String getPlayerName() {
		return name;
	}
	/**
	 * This method set the player name 
	 * @param name : sets the player name 
	 */
	public void setPlayerName(String name) {
		this.name = name;
	}
	/** 
	 * This method get the player id
	 * @return player id : get the player id 
	 */
	public int getPlayer_id() {
		return player_id;
	}
	/**
	 * This method set the player id
	 * @param player_id : sets the player id 
	 */
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	/**
	 * This method get the cards 
	 * @return cards 
	 */
	public List<Card> getCards() {
		return cards;
	}
	/**
	 * this method set the cards 
	 * @param cards : set the cards 
	 */
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	/**
	 * this method get the army
	 * @return army : army
	 */
	public int getArmy() {
		return army;
	}
	/**
	 * This method set the army
	 * @param army : army
	 */
	public void setArmy(int army) {
		this.army = army;
	}
	/**
	 * This method get the player country
	 * @return playerCountries : get the player country
	 */
	public List<Country> getPlayerCountries() {
		return playerCountries;
	}
	/**
	 * This method set the player countries
	 * @param alloccupied_countries : set the player country
	 */
	public void setPlayerCountries(List<Country> alloccupied_countries) {
		this.playerCountries = alloccupied_countries;
	}

	public void reinforcementPhase(Country reinforcementCountry, int noOfArmies) {
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount()+noOfArmies);
		this.setArmy(this.getArmy()-noOfArmies);
	}
	
	public void fortify(Country fromCountry, Country toCountry, int armiesToMove) {
		fromCountry.setArmyCount(fromCountry.getArmyCount()-armiesToMove);
		toCountry.setArmyCount(toCountry.getArmyCount()+armiesToMove);
	}
	/**
	 * This method for attack phase
	 * @param attackerCountry : attacker country
	 * @param defenderCountry : defender country
	 * @param defender : defender
	 * @param ifAllOut : if all out
	 * @param totalAttackerDice : total attacker dice
	 * @param totalDefenderDice : total defender dice
	 * @param phaseViewModel : phase view model
	 * @return pair : get the new pair
	 */
	public Pair<Boolean, Integer> attack(Country attackerCountry, Country defenderCountry, Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice, PhaseViewModel phaseViewModel ){
		boolean ifWon = false;
		int leftTroop = -1;
		List<Integer> attackerDiceResult;
		List<Integer> defenderDiceResult;
		
		if(!ifAllOut) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n"+" dice rolling----");
			attackerDiceResult = diceRollResult(totalAttackerDice, true);
			defenderDiceResult = diceRollResult(totalDefenderDice, false);
			leftTroop = attackHelper(attackerCountry,defenderCountry,attackerDiceResult,defenderDiceResult,phaseViewModel);
			
			if(defenderCountry.getArmyCount()==0) {
				ifWon =true;
			}
		}else {
			while(attackerCountry.getArmyCount()>1) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n"+" dice rolling----");
				attackerDiceResult = diceRollResult(attackerCountry.getArmyCount()-1, true);
				defenderDiceResult = diceRollResult(defenderCountry.getArmyCount(), false);
				leftTroop = attackHelper(attackerCountry,defenderCountry,attackerDiceResult,defenderDiceResult,phaseViewModel);
				
				if(defenderCountry.getArmyCount()==0) {
					ifWon =true;
					break;
				}
			}
		}
		
		if(ifWon) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n" + "attacker won the country");
			defender.getPlayerCountries().remove(defenderCountry);
			this.getPlayerCountries().add(defenderCountry);
			defenderCountry.setCountryOwner(this);
			return new Pair<Boolean, Integer>(ifWon, leftTroop);
			
		}else{
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n" + "attacker didn't win the country");
			return new Pair<Boolean, Integer>(ifWon, null);
		}
		
	}
/**
 * This method is helper method for  attack() 
 * @param attackerCountry : attacker country
 * @param defenderCountry  : defender country
 * @param attackerDiceResult  : attacker dice result
 * @param defenderDiceResult : defender dice result
 * @param phaseViewModel : phase view model
 * @return leftTroops : get the left troop
 */
	public int attackHelper(Country attackerCountry, Country defenderCountry, List<Integer> attackerDiceResult,
			List<Integer> defenderDiceResult, PhaseViewModel phaseViewModel) {
		
		int leftTroops = attackerDiceResult.size();
		
		Collections.sort(attackerDiceResult,Collections.reverseOrder());
		Collections.sort(defenderDiceResult,Collections.reverseOrder());;
		
		int times = attackerDiceResult.size()>defenderDiceResult.size() ? defenderDiceResult.size() : attackerDiceResult.size();
		
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"\n"+"attacker dice "+ attackerDiceResult.toString() +"\n"+ "defender dice "+ defenderDiceResult.toString());
		phaseViewModel.allChanged();
		for(int i=0;i<times;i++) {
			
			if(attackerDiceResult.get(i)>defenderDiceResult.get(i)) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+ "\n" + "attacker won in "+ i + " die roll");
				defenderCountry.setArmyCount(defenderCountry.getArmyCount()-1);
			}else {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+ "\n" + "attacker didn't win in "+ i + " die roll");
				attackerCountry.setArmyCount(attackerCountry.getArmyCount()-1);
				leftTroops--;
			}
		}
		
		
		return leftTroops;
	}
	/**
	 * diceRollResult() to return results of dice
	 * @param armyCount : army count
	 * @param ifAttacker  : current attacker 
	 * @return result  : get the result 
	 */
	public List<Integer> diceRollResult(int armyCount, boolean ifAttacker) {
		Random random = new Random();
		List<Integer> result = new ArrayList<>();
		int noOfDice;

		if (ifAttacker) {
			if (armyCount >= 3) {
				noOfDice = 3;
			} else {
				noOfDice = armyCount;
			}
		} else {
			if (armyCount >= 2) {
				noOfDice = 2;
			} else {
				noOfDice = armyCount;
			}
		}

		int randomResult;

		for (int i = 0; i < noOfDice; i++) {
			randomResult = random.nextInt(6) + 1;
			result.add(i, randomResult);
		}
		return result;
	}

}

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;
/**
 * This class implements human.
 *  
 *  @author Pegah
 */
public class HumanStrategy implements StrategyInterface,Serializable {
	/**
	 * 
	 * this method is used for the reinforcement phase.
	 * 
	 * @param Player : player this is an object of Player
	 * @param Country : reinforcementCountry this is an object of Country
	 * @param int : noOfArmies this is number of armies
	 * @param PhaseViewModel : phaseViewModel this is an object of PhaseViewModel
	 */
	@Override
	public void reinforcement(Player player, Country reinforcementCountry, int noOfArmies,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Reinforcing for Human Player");
		phaseViewModel.allChanged();
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount() + noOfArmies);
		player.setArmy(player.getArmy() - noOfArmies);
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n" + noOfArmies
				+ " armies moved to " + reinforcementCountry.getName());
		phaseViewModel.allChanged();

	}
	/**
	 * 
	 * this method is used for the fortification phase.
	 * 
	 * @param Player : player this is an object of Player
	 * @param Country : fromCountry this is an object of Country
	 * @param Country : toCountry this is an object of Country
	 * @param int : armiesToMove this is the number of armies to move
	 * @param PhaseViewModel : phaseViewModel this is an object of PhaseViewModel 
	 */
	@Override
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + " \n Fortification fro Human Player");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Moved " + armiesToMove + " to "
				+ toCountry.getName() + " from " + fromCountry.getName());
		phaseViewModel.allChanged();
		fromCountry.setArmyCount(fromCountry.getArmyCount() - armiesToMove);
		toCountry.setArmyCount(toCountry.getArmyCount() + armiesToMove);
	}
	/**
	 * 
	 * this method is used for the attack phase.
	 * 
	 * @param Player : attacker this is an object of Player
	 * @param Country : attackerCountry this is an object of Country
	 * @param Country : defenderCountry this is an object of Country
	 * @param Player : defender this is an object of Player
	 * @param boolean : ifAllOut this check if all the armies out or not
	 * @param int : totalAttackerDice this calculate the total number of attacker dices
	 * @param int : totalDefenderDice this calculate the total number of defender dices
	 * @param PhaseViewModel : phaseViewModel this is an object of PhaseViewModel 
	 * @return Pair: this returns the countries who wins the game
	 */
	@Override
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry,
			Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice,
			PhaseViewModel phaseViewModel) {
		// TODO Auto-generated method stub
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" humam Attacker");
		phaseViewModel.allChanged();
		boolean ifWon = false;
		int leftTroop = -1;
		List<Integer> attackerDiceResult;
		List<Integer> defenderDiceResult;
		
		if(!ifAllOut) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n"+" dice rolling----");
			phaseViewModel.allChanged();
			attackerDiceResult = diceRollResult(totalAttackerDice, true);
			defenderDiceResult = diceRollResult(totalDefenderDice, false);
			leftTroop = attackHelper(attackerCountry,defenderCountry,attackerDiceResult,defenderDiceResult,phaseViewModel);
			
			if(defenderCountry.getArmyCount()==0) {
				ifWon =true;
			}
		}else {
			while(attackerCountry.getArmyCount()>1) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n"+" dice rolling----");
				phaseViewModel.allChanged();
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
			phaseViewModel.allChanged();
			defender.getPlayerCountries().remove(defenderCountry);
			attacker.getPlayerCountries().add(defenderCountry);
			defenderCountry.setCountryOwner(attacker);
			return new Pair<Boolean, Integer>(ifWon, leftTroop);
			
		}else{
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n" + "attacker didn't win the country");
			phaseViewModel.allChanged();
			return new Pair<Boolean, Integer>(ifWon, null);
		}

	}

	/**
	 * This method is helper method for attack()
	 * 
	 * @param attackerCountry : attacker country this is an object of Country
	 * @param defenderCountry : defender country this is an object of Country
	 * @param attackerDiceResult : attacker dice result this is the total number of attacker dices
	 * @param defenderDiceResult : defender dice result this is the total number of defender dices
	 * @param phaseViewModel : phase view model this is an object of PhaseViewModel
	 * @return leftTroops : get the left troop
	 */
	private int attackHelper(Country attackerCountry, Country defenderCountry, List<Integer> attackerDiceResult,
			List<Integer> defenderDiceResult, PhaseViewModel phaseViewModel) {

		int leftTroops = attackerDiceResult.size();

		Collections.sort(attackerDiceResult, Collections.reverseOrder());
		Collections.sort(defenderDiceResult, Collections.reverseOrder());
		;

		int times = attackerDiceResult.size() > defenderDiceResult.size() ? defenderDiceResult.size()
				: attackerDiceResult.size();

		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker dice "
				+ attackerDiceResult.toString() + "\n" + "defender dice " + defenderDiceResult.toString());
		phaseViewModel.allChanged();
		for (int i = 0; i < times; i++) {

			if (attackerDiceResult.get(i) > defenderDiceResult.get(i)) {
				phaseViewModel.setCurrentPhaseInfo(
						phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker won in " + i + " die roll");
				phaseViewModel.allChanged();
				defenderCountry.setArmyCount(defenderCountry.getArmyCount() - 1);
			} else {
				phaseViewModel.setCurrentPhaseInfo(
						phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker didn't win in " + i + " die roll");
				phaseViewModel.allChanged();
				attackerCountry.setArmyCount(attackerCountry.getArmyCount() - 1);
				leftTroops--;
			}
		}

		return leftTroops;
	}
	/**
	 * diceRollResult() to return results of dice
	 * 
	 * @param armyCount : army count this is the count of armies
	 * @param ifAttacker : this is the current attacker
	 * @return result : get the result
	 */
	private List<Integer> diceRollResult(int armyCount, boolean ifAttacker) {
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
	/**
	 * this method is used for finding random numbers
	 * @param int : size this is a size of given list
	 * @return returns a random number
	 */
	private int randomInt(int size) {
		Random random = new Random();
		return random.nextInt(size);
	}

}

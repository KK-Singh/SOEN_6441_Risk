package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import ControllerHelper.MapControllerHelper;
import javafx.util.Pair;
/**
 * 
 * This class implements random.
 * 
 * @author Pegah
 *
 */
public class RandomStrategy implements StrategyInterface,Serializable {
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
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Reinforcement for Random Player");
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		phaseViewModel.allChanged();
		reinforcementCountry = player.getPlayerCountries().get(randomInt(player.getPlayerCountries().size()));
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount() + player.getArmy());
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n player moved " + player.getArmy()
				+ " armies to randomly selected territory(" + reinforcementCountry.getName() + ")");
		phaseViewModel.allChanged();
		player.setArmy(0);

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
		toCountry = player.getPlayerCountries().get(randomInt(player.getPlayerCountries().size()));
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + " \n Fortification of random player");
		phaseViewModel.setCurrentPhaseInfo(
				phaseViewModel.getCurrentPhaseInfo() + "\n Random Country : " + toCountry.getName());
		phaseViewModel.allChanged();
		
		List<Country> fortifiableCountries = new ArrayList<>();

		Queue<Country> queue = new LinkedList<>();
		queue.add(toCountry);
		Country c;

		while (queue.size() > 0) {
			c = queue.poll();
			for (Country con : c.getNeighbors()) {
				if (con.getCountryOwner() == player && !fortifiableCountries.contains(con)) {
					fortifiableCountries.add(con);
					queue.add(con);
				}
			}
		}

		if (fortifiableCountries.contains(toCountry))
			fortifiableCountries.remove(toCountry);

		fromCountry = fortifiableCountries.size() == 0 ? null
				: fortifiableCountries.get(randomInt(fortifiableCountries.size()));

		if (fromCountry == null) {
			phaseViewModel.setCurrentPhaseInfo(
					phaseViewModel.getCurrentPhaseInfo() + "\n no country to fortify from " + toCountry.getName());
			phaseViewModel.allChanged();
			return;
		} else {
			if (fromCountry.getArmyCount() == 1) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n cannot fortify from "
						+ fromCountry.getName() + " as it has just one army");
				phaseViewModel.allChanged();
			} else {
				armiesToMove = fromCountry.getArmyCount() - 1;
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n fortify from "
						+ fromCountry.getName() + " to " + toCountry.getName() + " " + armiesToMove + " armies");
				phaseViewModel.allChanged();
				fromCountry.setArmyCount(1);
				toCountry.setArmyCount(toCountry.getArmyCount() + armiesToMove);
			}
		}
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
	 * @return Pair: this returns the countries who win the game
	 */
	@Override
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry,
			Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice,
			PhaseViewModel phaseViewModel) {

		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + " \n Atatcking fro Random Player ");
		phaseViewModel.allChanged();

		boolean ifWon = false;
		int leftTroop = -1;
		List<Integer> attackerDiceResult;
		List<Integer> defenderDiceResult;

		phaseViewModel
				.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n" + " Atatcking with Random Strategy ");

		int noOfAttack = randomInt(10) + 1;
		attackerCountry = attacker.getPlayerCountries().get(randomInt(attacker.getPlayerCountries().size()));
		phaseViewModel
				.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Total possible attacks: " + noOfAttack);
		phaseViewModel.setCurrentPhaseInfo(
				phaseViewModel.getCurrentPhaseInfo() + "\n Random attacker Country is " + attackerCountry.getName());

		List<Country> defendingCountryList = new ArrayList<>();
		List<Country> totalCountries  = new ArrayList<>();
		MapControllerHelper.getObject().countryMap.forEach((k,v)->totalCountries.add(v));;
		
		for(Country con: totalCountries) {
			if(con.getCountryOwner()!=attacker) {
				defendingCountryList.add(con);
			}
		}
		
		defenderCountry = defendingCountryList.size()==0? null : defendingCountryList.get(randomInt(defendingCountryList.size()));
		
		if(defenderCountry==null) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" \n There is no country to attack from "+ attackerCountry.getName());
			return new Pair<Boolean, Integer>(ifWon, null);
		}else {
			defender= defenderCountry.getCountryOwner();
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" Random Defender Country is "+ defenderCountry.getName());
			
			for(int i=0;i<noOfAttack;i++) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n"+ i+" attack");
				
				if(attackerCountry.getArmyCount()==1) {
					phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" \n cannot frther attack as army count has reached 1");
					
				}else {
					phaseViewModel.setCurrentPhase(phaseViewModel.getCurrentPhaseInfo()+"\n"+" dice are rolling ");
					attackerDiceResult = diceRollResult(attackerCountry.getArmyCount()-1, true);
					defenderDiceResult = diceRollResult(defenderCountry.getArmyCount(), false);
					leftTroop = attackHelper(attackerCountry, defenderCountry, attackerDiceResult, defenderDiceResult, phaseViewModel);
					if(defenderCountry.getArmyCount()==0) {
						ifWon = true;
						break;
					}
				}
			}
		}
		
		if (ifWon) {
			phaseViewModel
					.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker won the country");
			defender.getPlayerCountries().remove(defenderCountry);
			attacker.getPlayerCountries().add(defenderCountry);
			defenderCountry.setCountryOwner(attacker);

			defenderCountry.setArmyCount(leftTroop);
			attackerCountry.setArmyCount(attackerCountry.getArmyCount()-leftTroop);
			return new Pair<Boolean, Integer>(ifWon, leftTroop);
		} else {
			phaseViewModel.setCurrentPhaseInfo(
					phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker didn't win the country");
			return new Pair<Boolean, Integer>(ifWon, null);
		}
	}
	/**
	 * This method is helper method for attack()
	 * 
	 * @param attackerCountry : attacker country this is an object of Country
	 * @param defenderCountry : defender country this is an object of Country
	 * @param attackerDiceResult : attacker dice result this is a list of attacker dices
	 * @param defenderDiceResult : defender dice result this is a list of defender dices
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
				defenderCountry.setArmyCount(defenderCountry.getArmyCount() - 1);
			} else {
				phaseViewModel.setCurrentPhaseInfo(
						phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker didn't win in " + i + " die roll");
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

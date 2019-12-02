package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;

public class HumanStrategy implements StrategyInterface {

	@Override
	public void reinforcement(Player player, Country reinforcementCountry, int noOfArmies,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Reinforcing for Human Player");
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount() + noOfArmies);
		player.setArmy(player.getArmy() - noOfArmies);
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n" + noOfArmies
				+ " armies moved to " + reinforcementCountry.getName());

	}

	@Override
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + " \n Fortification fro Human Player");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Moved " + armiesToMove + " to "
				+ toCountry.getName() + " from " + fromCountry.getName());
		fromCountry.setArmyCount(fromCountry.getArmyCount() - armiesToMove);
		toCountry.setArmyCount(toCountry.getArmyCount() + armiesToMove);
	}

	@Override
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry,
			Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice,
			PhaseViewModel phaseViewModel) {
		// TODO Auto-generated method stub
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" humam Attacker");
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
			attacker.getPlayerCountries().add(defenderCountry);
			defenderCountry.setCountryOwner(attacker);
			return new Pair<Boolean, Integer>(ifWon, leftTroop);
			
		}else{
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n" + "attacker didn't win the country");
			return new Pair<Boolean, Integer>(ifWon, null);
		}

	}

	/**ss
	 * This method is helper method for attack()
	 * 
	 * @param attackerCountry
	 *            : attacker country
	 * @param defenderCountry
	 *            : defender country
	 * @param attackerDiceResult
	 *            : attacker dice result
	 * @param defenderDiceResult
	 *            : defender dice result
	 * @param phaseViewModel
	 *            : phase view model
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
	 * @param armyCount
	 *            : army count
	 * @param ifAttacker
	 *            : current attacker
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

	private int randomInt(int size) {
		Random random = new Random();
		return random.nextInt(size);
	}

}

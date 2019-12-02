package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import ControllerHelper.MapControllerHelper;
import javafx.util.Pair;

public class RandomStrategy implements StrategyInterface {

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
	 * thoda theek karna hai. 
	 */
	@Override
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove,
			PhaseViewModel phaseViewModel) {
		fromCountry = player.getPlayerCountries().get(randomInt(player.getPlayerCountries().size()));
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + " \n Fortification of random player");
		phaseViewModel.setCurrentPhaseInfo(
				phaseViewModel.getCurrentPhaseInfo() + "\n Random Country : " + toCountry.getName());
		phaseViewModel.allChanged();
		List<Country> fortifiableCountries = new ArrayList<>();

		Queue<Country> queue = new LinkedList<>();
		queue.add(fromCountry);
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

		if (fortifiableCountries.contains(fromCountry))
			fortifiableCountries.remove(fromCountry);

		toCountry = fortifiableCountries.size() == 0 ? null
				: fortifiableCountries.get(randomInt(fortifiableCountries.size()));

		if (toCountry == null) {
			phaseViewModel.setCurrentPhaseInfo(
					phaseViewModel.getCurrentPhaseInfo() + "\n no country to fortify from " + fromCountry.getName());
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

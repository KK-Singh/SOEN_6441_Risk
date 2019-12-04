package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import javafx.util.Pair;
/**
 * This AggressiveStrategy class implements StrategyInterface,Serializable
 * @author Gurwinder
 *
 */
public class AggressiveStrategy implements StrategyInterface,Serializable {
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

		phaseViewModel
				.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + " \n Reinforcement for Aggressive Player");
		phaseViewModel.allChanged();
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		reinforcementCountry = findStrongestCountryToAttackFrom(player);
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount() + player.getArmy());
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n moved " + noOfArmies + " to "
				+ reinforcementCountry.getName());
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
		phaseViewModel
				.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Fortification for Aggressive Player");
		toCountry = findStrongestCountryToAttackFrom(player);
		phaseViewModel.setCurrentPhaseInfo(
				phaseViewModel.getCurrentPhaseInfo() + " \n Fortifying to the strongest country" + toCountry.getName());
		phaseViewModel.allChanged();
		List<Country> fortifiableCountries = new ArrayList<Country>();

		Queue<Country> queue = new LinkedList<Country>();
		queue.add(toCountry);
		Country cont = null;

		while (queue.size() > 0) {
			cont = queue.poll();
			for (Country con : cont.getNeighbors()) {
				if (con.getCountryOwner() == player && !fortifiableCountries.contains(con)) {
					fortifiableCountries.add(con);
					queue.add(con);
				}
			}
		}

		if (fortifiableCountries.contains(toCountry)) {
			fortifiableCountries.remove(toCountry);
		}

		fromCountry = fortifiableCountries.size() > 0 ? fortifiableCountries.get(0) : null;

		for (Country con : fortifiableCountries) {
			if (fromCountry.getArmyCount() < con.getArmyCount()) {
				fromCountry = con;
			}
		}

		if (fromCountry == null || fromCountry.getArmyCount() == 1) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n No country to fortify from ");
			phaseViewModel.allChanged();
		} else {
			armiesToMove = fromCountry.getArmyCount() - 1;
			fromCountry.setArmyCount(1);
			toCountry.setArmyCount(armiesToMove);
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n fortify from "
					+ fromCountry.getName() + " to " + toCountry.getName());
			phaseViewModel.allChanged();
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
	 * @return Pair: this returns the countries who wins the game
	 */
	@Override
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry,
			Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice,
			PhaseViewModel phaseViewModel) {
		phaseViewModel
				.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n Attacking for Aggressive Strategy");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()
				+ "\n Player will attack from the strongest to the weakest country");
		phaseViewModel.allChanged();
		
		attackerCountry = findStrongestCountryToAttackFrom(attacker);
		defenderCountry = weakestCountryToAttack(attackerCountry);

		defender = defenderCountry != null ? defenderCountry.getCountryOwner() : null;
		phaseViewModel.setCurrentPhaseInfo(
				phaseViewModel.getCurrentPhaseInfo() + "\n Attacking Country " + attackerCountry.getName());

		phaseViewModel.allChanged();
		
		if (defenderCountry == null) {
			phaseViewModel
					.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n cannot find country to attack to");
			phaseViewModel.allChanged();
			return new Pair<Boolean, Integer>(Boolean.FALSE, null);
		} else {
			phaseViewModel.setCurrentPhaseInfo(
					phaseViewModel.getCurrentPhaseInfo() + "\n Defending country is " + defenderCountry.getName());
			phaseViewModel.allChanged();
			boolean ifWon = false;

			int leftTroops = -1;
			List<Integer> attackerDiceResult = new ArrayList<>();
			List<Integer> defenderDiceResult = new ArrayList<>();

			while (attackerCountry.getArmyCount() > 1) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n dice rolling");
				attackerDiceResult = diceRollResult(attackerCountry.getArmyCount() - 1, true);
				defenderDiceResult = diceRollResult(defenderCountry.getArmyCount(), false);
				leftTroops = attackHelper(attackerCountry, defenderCountry, attackerDiceResult, defenderDiceResult,
						phaseViewModel);

				if (defenderCountry.getArmyCount() == 0) {
					ifWon = true;
					break;
				}
			}

			if (ifWon) {
				phaseViewModel
						.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker won the country");
				defender.getPlayerCountries().remove(defenderCountry);
				attacker.getPlayerCountries().add(defenderCountry);
				defenderCountry.setCountryOwner(attacker);
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\n moved armies from "
						+ attackerCountry.getName() + " to " + defenderCountry.getName());
				phaseViewModel.allChanged();
				defenderCountry.setArmyCount(attackerCountry.getArmyCount() - 1);
				attackerCountry.setArmyCount(1);
				return new Pair<Boolean, Integer>(ifWon, leftTroops);

			} else {
				phaseViewModel.setCurrentPhaseInfo(
						phaseViewModel.getCurrentPhaseInfo() + "\n" + "attacker didn't win country");
				phaseViewModel.allChanged();
				return new Pair<Boolean, Integer>(ifWon, null);
			}
		}

	}
	/**
	 * findStrongestCountryToAttackFrom() will find the strongest country to attack
	 * @param Player: player this is a Player object
	 * @return Country: StrongestCountry this is a Country object   
	 */
	public Country findStrongestCountryToAttackFrom(Player player) {
		List<Country> countryList = player.getPlayerCountries();
		Country strongestCountry = countryList.get(0);
		for (Country country : countryList) {
			if (country.getArmyCount() > strongestCountry.getArmyCount())
				strongestCountry = country;
		}

		return strongestCountry;
	}
/**
 * weakestCountryToAttack() will find the weakest country to attack
 * @param Country: attackFrom this is a Country object
 * @return Country: weakestCountry this is a Country object   
 */
	public Country weakestCountryToAttack(Country attackFrom) {

		List<Country> possibleAttackableCountries = new ArrayList<>();

		for (Country con : attackFrom.getNeighbors()) {
			if (attackFrom.getCountryOwner() != con.getCountryOwner()) {
				possibleAttackableCountries.add(con);
			}
		}

		Country weakestCountry = possibleAttackableCountries.size() > 0 ? possibleAttackableCountries.get(0) : null;

		for (Country con : possibleAttackableCountries) {
			if (weakestCountry.getArmyCount() < con.getArmyCount()) {
				weakestCountry = con;
			}
		}

		return weakestCountry;
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

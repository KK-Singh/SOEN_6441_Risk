package model;
import javafx.util.Pair;
/**
 * StrategyInterface for strategy
 * @author Gurwinder
 *
 */
public interface StrategyInterface  
{	
	
	public void reinforcement(Player player, Country reinforcementCountry, int noOfArmies, PhaseViewModel phaseViewModel);
	
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove, PhaseViewModel phaseViewModel);
	
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry, Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice, PhaseViewModel phaseViewModel );
}

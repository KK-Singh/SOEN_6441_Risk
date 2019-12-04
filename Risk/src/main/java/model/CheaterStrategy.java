package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
/**
 * This class implements cheater.
 *  
 *  @author Pegah
 */
public class CheaterStrategy implements StrategyInterface,Serializable{

	private int maxPossibleAryCount =1000;
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
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" \n Reinforcing for Cheater player");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n player will double the armies on all it's countries");
		phaseViewModel.allChanged();
		for(Country con: player.getPlayerCountries()) {
			if(con.getArmyCount()>maxPossibleAryCount) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n total armies being placed on "+con.getName()+" are max equal to"+ maxPossibleAryCount);
				phaseViewModel.allChanged();
				con.setArmyCount(maxPossibleAryCount);
			}else {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n total armies being palced on "+ con.getName()+" will be double of previous value");
				phaseViewModel.allChanged();
				con.setArmyCount(con.getArmyCount()*2);
			}
		}
		
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
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n Fortification for Cheater player");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n Player will double the values of armimes on all the countries which have neigbours occupied by other players");
		phaseViewModel.allChanged();
		for(Country pc: player.getPlayerCountries()) {
			for(Country nc : pc.getNeighbors()) {
				if(nc.getCountryOwner()!=player) {
					if(pc.getArmyCount()*2>maxPossibleAryCount) {
						pc.setArmyCount(maxPossibleAryCount);
					}else {
						pc.setArmyCount(pc.getArmyCount()*2);
					}
				}
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
		
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n Attacking for Cheater player");
		phaseViewModel.allChanged();
		List<Country> wonCountries = new ArrayList<>();
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n player try to win all the countries");
		phaseViewModel.allChanged();
		
		for(Country pc : attacker.getPlayerCountries()) {
			for(Country nc: pc.getNeighbors()) {
				if(nc.getCountryOwner()!=attacker) {
					nc.getCountryOwner().getPlayerCountries().remove(nc);
					nc.setCountryOwner(attacker);
					pc.setArmyCount(1);
					wonCountries.add(nc);
					
				}
			}
		}
		
		attacker.getPlayerCountries().addAll(wonCountries);
		
		if(wonCountries.size()>0) {
			return new Pair<Boolean, Integer>(Boolean.TRUE, null);
		}else {
			return new Pair<Boolean, Integer>(Boolean.FALSE, null);
		}
	}

}

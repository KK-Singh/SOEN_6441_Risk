package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.util.Pair;
/**
 * This BenevolentStrategy class implements StrategyInterface,Serializable
 * @author Gurwinder
 *
 */
public class BenevolentStrategy implements StrategyInterface,Serializable{

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
		
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"\n reinforcing for benevolent player");
		phaseViewModel.allChanged();
		reinforcementCountry= findWeekestCountry(player);
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount()+player.getArmy());
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\nmoved"+ player.getArmy()+ "to weekestcountry"
				+ reinforcementCountry.getName());
		phaseViewModel.allChanged();
		player.setArmy(0);
		
		
	}

	@Override
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"\n fortifying for benevolent player");
		toCountry=findWeekestCountry(player);
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"fortifying weakest country"+ toCountry.getName());
		phaseViewModel.allChanged();
		
		List<Country> fortifiableCountries = new ArrayList<>();
		Queue<Country> queue = new LinkedList<>();
		queue.add(toCountry);
		Country country;
		
		while(queue.size()>0) {
			country = queue.poll();
			List<Country>neighbors = country.getNeighbors();
			for (Country neighborcountry : neighbors) {
				if (neighborcountry.getCountryOwner()== player && !fortifiableCountries.contains(neighborcountry)) {
					fortifiableCountries.add(neighborcountry);
					queue.add(neighborcountry);
				}
			}
			
		}
		
		if (fortifiableCountries.contains(toCountry))
			fortifiableCountries.remove(toCountry);
		
		if (fortifiableCountries.size()>0) 
			fromCountry = fortifiableCountries.get(0);
		else fromCountry= null;
		
		for (Country fortifyCountry : fortifiableCountries) {
			if (fortifyCountry.getArmyCount()> fromCountry.getArmyCount())
				fromCountry=fortifyCountry;
		}
		
		if (fromCountry==null || fromCountry.getArmyCount()==1) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() 
					+ "\n no possible territory to fortify " + toCountry.getName());
			phaseViewModel.allChanged();
			return;
		}else {
			armiesToMove= fromCountry.getArmyCount()-1;
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()
					+ "\n fortified"+ toCountry.getName() +"with"+ armiesToMove+ "from"+ fromCountry.getName());
			phaseViewModel.allChanged();
			fromCountry.setArmyCount(1);
			toCountry.setArmyCount(toCountry.getArmyCount()+armiesToMove);
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
		// TODO Auto-generated method stub
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\nAttacking for Benevolent player.");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\nPlayer will not attack.");
		phaseViewModel.allChanged();	
		
		return new Pair<Boolean, Integer>(Boolean.FALSE,null);
	}
	
	/**
	 * weakestCountryToAttack() will find the weakest country to attack
	 * @param Country: attackFrom this is a Country object
	 * @return Country: weakestCountry this is a Country object   
	 */
		
	private Country findWeekestCountry(Player player) {
		List<Country>countryList=player.getPlayerCountries();
		Country weekestCountry = countryList.get(0);
		for(Country country : countryList) {
			if (country.getArmyCount()<weekestCountry.getArmyCount())
				weekestCountry=country;
		
		
		}
		return weekestCountry;
	}
	
	

}

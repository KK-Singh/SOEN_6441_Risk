package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.util.Pair;

public class BenevolentStrategy implements StrategyInterface{

	@Override
	public void reinforcement(Player player, Country reinforcementCountry, int noOfArmies,
			PhaseViewModel phaseViewModel) {
		
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"\n reinforcing for benevolent player");
		reinforcementCountry= findWeekestCountry(player);
		reinforcementCountry.setArmyCount(reinforcementCountry.getArmyCount()+player.getArmy());
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\nmoved"+ player.getArmy()+ "to weekestcountry"
				+ reinforcementCountry.getName());
		player.setArmy(0);
		
		
	}

	@Override
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"\n fortifying for benevolent player");
		toCountry=findWeekestCountry(player);
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() +"fortifying weekest country"+ toCountry.getName());
			
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
			return;
		}else {
			armiesToMove= fromCountry.getArmyCount()-1;
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()
					+ "\n fortified"+ toCountry.getName() +"with"+ armiesToMove+ "from"+ fromCountry.getName());
			
			fromCountry.setArmyCount(1);
			toCountry.setArmyCount(toCountry.getArmyCount()+armiesToMove);
		}
			
		
		
		
	}

	@Override
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry,
			Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice,
			PhaseViewModel phaseViewModel) {
		// TODO Auto-generated method stub
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\nAttacking for Benevolent player.");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo() + "\nPlayer will not attack.");
			
		
		return new Pair<Boolean, Integer>(Boolean.FALSE,null);
	}
	
	
	
	
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

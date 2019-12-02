package model;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class CheaterStrategy implements StrategyInterface{

	private int maxPossibleAryCount =1000;
	
	@Override
	public void reinforcement(Player player, Country reinforcementCountry, int noOfArmies,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+" \n Reinforcing for Cheater player");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n player will double the armies on all it's countries");
		for(Country con: player.getPlayerCountries()) {
			if(con.getArmyCount()>maxPossibleAryCount) {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n total armies being placed on "+con.getName()+" are max equal to"+ maxPossibleAryCount);
				con.setArmyCount(maxPossibleAryCount);
			}else {
				phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n total armies being palced on "+ con.getName()+" will be double of previous value");
				con.setArmyCount(con.getArmyCount()*2);
			}
		}
		
		player.setArmy(0);
	}

	@Override
	public void fortify(Player player, Country fromCountry, Country toCountry, int armiesToMove,
			PhaseViewModel phaseViewModel) {
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n Fortification for Cheater player");
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n Player will double the values of armimes on all the countries which have neigbours occupied by other players");
		
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

	@Override
	public Pair<Boolean, Integer> attack(Player attacker, Country attackerCountry, Country defenderCountry,
			Player defender, boolean ifAllOut, int totalAttackerDice, int totalDefenderDice,
			PhaseViewModel phaseViewModel) {
		
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n Attacking for Cheater player");
		List<Country> wonCountries = new ArrayList<>();
		phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()+"\n player try to win all the countries");
		
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

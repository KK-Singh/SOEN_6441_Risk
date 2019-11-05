package view;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import model.Continent;
import model.Player;
import model.WorldDominationViewModel;
/**
* This PhaseView file can contains the observer method
* of the risk game.
* @author  Yash
* @version 1.2
*/
public class WorldDominationView implements Observer{
	
	
	/**
	* This method is used to update the observers of the Dominationview
	* @param o Domination view observable object
	* @param arg Domination view observable object
	*/
	@Override
	public void update(Observable o, Object arg) {
		WorldDominationViewModel model = (WorldDominationViewModel)o;
		
		Map<Player,Integer> playersArmies = model.getPlayerArmy();
		Map<Player, Set<Continent>> playersContinent = model.getPlayerContinentMapping();
		Map<Player,Double> playerCoverage = model.getPlayerCoverage();
		System.out.println("------------------WORLD DOMINATION VIEW START------------------");
		for(Entry<Player, Integer> entry: playersArmies.entrySet()) {
			Player cp = entry.getKey();
			int totalCpArmies = playersArmies.get(cp);
			double playerCoverageVal = playerCoverage.get(cp);
			Set<Continent> ownedContinent = playersContinent.get(cp);
			
			System.out.print("Current Player :" + cp.getPlayerName());
			System.out.println();
			System.out.print("Current Player Total Army Count :" + totalCpArmies);
			System.out.println();
			System.out.print("Current Player Map Covered Percentage :" + playerCoverageVal );
			System.out.println();
			System.out.print("Current Player Continents Owned :" + ownedContinent);
			System.out.println();
		}
		System.out.println("-----------------WORLD DOMINATION VIEW END-------------------");
	}

}

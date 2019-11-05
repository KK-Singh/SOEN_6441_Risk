package view;

import java.util.Observable;
import java.util.Observer;

import model.PhaseViewModel;
/**
* This PhaseView file can contains the observer method
* of the risk game.
* @author  Yash
* @version 1.2
*/
public class PhaseView implements Observer {
	
	/**
	* This method is used to update the observers of the phase
	* @param o phase view observable object
	* @param arg phase view observable object
	*/
	@Override
	public void update(Observable o, Object arg) {
		PhaseViewModel model = (PhaseViewModel) o;
		System.out.println("------------------PHASE VIEW START--------------------");
		System.out.println("Current Player is " + model.getCurrentPlayer());
		System.out.println("Current Phase is " + model.getCurrentPhase());
		System.out.println("Current Phase Information is " + model.getCurrentPhaseInfo());
		System.out.println("------------------PHASE VIEW END--------------------");
	}

}

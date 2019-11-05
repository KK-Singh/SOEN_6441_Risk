package view;

import java.util.Observable;
import java.util.Observer;

import model.PhaseViewModel;

public class PhaseView implements Observer {

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

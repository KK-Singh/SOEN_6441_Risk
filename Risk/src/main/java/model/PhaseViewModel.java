package model;

import java.util.Observable;

public class PhaseViewModel extends Observable{

	
	String currentPhase;
	
	String currentPlayer;
	
	String currentPhaseInfo;

	public String getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public String getCurrentPhaseInfo() {
		return currentPhaseInfo;
	}

	public void setCurrentPhaseInfo(String currentPhaseInfo) {
		this.currentPhaseInfo = currentPhaseInfo;
	}
	
	public void allChanged() {
		notficationHelper();
	}
	
	private void notficationHelper() {
		setChanged();
		notifyObservers(this);
	}
}

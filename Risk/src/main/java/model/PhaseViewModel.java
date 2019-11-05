package model;
import java.util.Observable;
/**
@author Gurwinder Kaur 
*/
public class PhaseViewModel extends Observable
{
/**
 *currentPhase : Represents current phase
 */
	
	String currentPhase;
	/**
	 * currentPlayer : Represents current player
	 */
	String currentPlayer;
	/** 
	 *currentPhaseInfo : Represents current phase infor
	 */
	String currentPhaseInfo;
/**
 * This method get the current phase
 * @return currentPhase : current phase
 */
	public String getCurrentPhase() {
		return currentPhase;
	}
	/**
	 * This method set the current phase
	 * @param currentPhase : current phase
	 */
	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}
/**
 * This method set the current player
 * @return current player : current player
 */
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	/**
	 * This method set the current player
	 * @param currentPlayer : current player
	 */
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	/**
	 * This method get the current phase info
	 * @return currentPhaseInfor : current phase info
	 */
	public String getCurrentPhaseInfo() {
		return currentPhaseInfo;
	}

	/**
	 * This method set the current phase info
	 * @param currentPhaseInfo : current phase info
	 */
	public void setCurrentPhaseInfo(String currentPhaseInfo) {
		this.currentPhaseInfo = currentPhaseInfo;
	}
	 /**
	  * This method notify all the changes
	  */
	
	public void allChanged() {
		notficationHelper();
	}
	/**
	 * This method notify the helper
	 */
	private void notficationHelper() {
		setChanged();
		notifyObservers(this);
	}
}

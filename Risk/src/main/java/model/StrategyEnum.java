package model;
/**
 * Enumeration of strategy 
 * @author Gurwinder
 *
 */
public enum StrategyEnum 
{
HUMAN, BENEVOLENT, RANDOM, CHEATER, AGGRESSIVE;
	@Override
	public String toString() 
	{
		switch (this) {
		case HUMAN:
			return "Human";
		case BENEVOLENT:
			return "Benevolent";
		case RANDOM:
			return "Random";
		case CHEATER:
			return "Cheater";
		case AGGRESSIVE:
			return "Aggressive";
		default:
			return "Wrong";
		}
		
	}
	
	
}

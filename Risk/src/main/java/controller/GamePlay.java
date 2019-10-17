package controller;

import java.util.List;

import model.Country;
import model.Player;
import service.GameService;

/**
 * Class reprsent playing whole game
 * 
 * 
 */

public class GamePlay {

	GameService gameService = new GameService();
	
	public void startGame(List<Player> players) {
		gameService.assignCountriesAtRandom(players);
	}
	
	/**
	 * startup pahase of game 
	 * @param players list of players
	 * @param playerTurn turn of each player
	 * @param allPlaced placing army check
	 * @param countryName naame of country
	 *  @param armyCount count of army
	 */
	
	
	public void startUpPhase(List<Player> players, int playerTurn, boolean allPlaced, String countryName, int armyCount  ) {
		
		if(allPlaced) {
			players.get(playerTurn).getAlloccupied_countries().get(0).setNo_of_armies(players.get(playerTurn).getArmy());
			players.get(playerTurn).setArmy(0);
				
		}else {
			gameService.fillPlayerCountry(players.get(playerTurn), countryName, armyCount);
		}
		
	}
	
	
	// calling fortifying postion method
	public void fortify(Country startCountry, Country destinationCountry, int armyToMove) {
		gameService.fortifyPosition(startCountry, destinationCountry, armyToMove);
	}
	
	
	/**
	 * Assging country and army to players
	 * @param Player: player name
	 * @param  countryName : name of country
	 * @param armyCount : count nummber of army
	 */
	
	public void reinforcementPhase(Player player, String countryName, int armyCount) {
		gameService.reinforcementArmy(player);
		gameService.fillPlayerCountry(player, countryName, armyCount);
	}
	/**
	 * Setting number of army for player
	 * @param players : List of players
	 *
	 */
	
	public void setArmyCountForPlayer(List<Player> players) {
		
		int armyCount = gameService.getArmyCountBasedOnPlayers(players);
		players.forEach(player->{
			player.setArmy(armyCount);
		});
	}
	
}

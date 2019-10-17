package controller;

import java.util.List;

import model.Country;
import model.Player;
import service.GameService;
import service.MapService;

public class GamePlay {

	GameService gameService = new GameService();
	
	public void startGame(List<Player> players) {
		gameService.assignCountriesAtRandom(players);
	}
	
	public void startUpPhase(List<Player> players, int playerTurn, boolean allPlaced, String countryName, int armyCount  ) {
		
		if(allPlaced) {
			players.get(playerTurn).getAlloccupied_countries().get(0).setNo_of_armies(
					players.get(playerTurn).getAlloccupied_countries().get(0).getNo_of_armies() +
					players.get(playerTurn).getArmy()
			);
			players.get(playerTurn).setArmy(0);
				
		}else {
			gameService.fillPlayerCountry(players.get(playerTurn), countryName, armyCount);
		}
		
	}
	
	public void fortify(String startCountry, String destinationCountry, int armyToMove) {
		
		System.out.println(gameService.fortifyPosition(MapService.getObject().getCountry(startCountry.trim()), MapService.getObject().getCountry(destinationCountry.trim()), armyToMove));
	}
	
	
	public void reinforcementPhase(Player player, String countryName, int armyCount) {
		
		gameService.fillPlayerCountry(player, countryName, armyCount);
	}
	
	public void setArmyCountForPlayer(List<Player> players) {
		
		int armyCount = gameService.getArmyCountBasedOnPlayers(players);
		players.forEach(player->{
			player.setArmy(armyCount);
		});
	}
	
	
	public void loadMapOnConsole(List<Player> players) {
		gameService.loadMapOnConsole(players);
	}
	
	public void reinforcementArmy(Player player) {
		gameService.reinforcementArmy(player);
	}
}

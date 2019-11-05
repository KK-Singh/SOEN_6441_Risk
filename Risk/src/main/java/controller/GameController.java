package controller;

import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import model.CardViewModel;
import model.Continent;
import model.Country;
import model.PhaseViewModel;
import model.Player;
import model.WorldDominationViewModel;
import service.GameService;
import service.MapService;
import view.CardExchangeView;
import view.PhaseView;
import view.WorldDominationView;

/**
 * GameController class implements the methods of the game
 * 
 * @author Pegah
 */
public class GameController {

	GameService gameService = new GameService();

	Map<Integer, Continent> continentMap;

	Map<Integer, Country> countryMap;

	List<Player> playersList;

	PhaseViewModel phaseViewModel;

	WorldDominationViewModel worldDominationModel;

	CardViewModel cardViewModel;
/**
 * This method is used to start the game
 * @param players : this is a list of players
 * @param continentMap : this is the map of continent
 * @param countryMap : this is the map of country
 */
	public void startGame(List<Player> players, Map<Integer, Continent> continentMap,
			Map<Integer, Country> countryMap) {

		this.continentMap = continentMap;
		this.countryMap = countryMap;
		this.playersList = players;

		gameService.assignCountriesAtRandom(players);
		phaseViewModel = new PhaseViewModel();
		phaseViewModel.addObserver(new PhaseView());

		worldDominationModel = new WorldDominationViewModel(players);
		worldDominationModel.addObserver(new WorldDominationView());

		worldDominationModel.stateUpdate(continentMap, countryMap);

		cardViewModel = new CardViewModel();
		cardViewModel.addObserver(new CardExchangeView());
	}
	/**
	 * This method is used for the startUpPhase of the game
	 * @param players : this is a list of players
	 * @param playerTurnp  : this is player turn
	 * @param allPlaced : this is allPlaced for armies
	 * @param countryName : this is country name
	 * @param armyCount : this is the number of armies
	 */
	public void startUpPhase(List<Player> players, int playerTurn, boolean allPlaced, String countryName,
			int armyCount) {
		phaseViewModel.setCurrentPhase("Startup Phase ::::");
		phaseViewModel.setCurrentPlayer(players.get(playerTurn).getPlayerName());

		if (allPlaced) {
			phaseViewModel.setCurrentPhaseInfo(
					"Player " + players.get(playerTurn).getPlayerName() + " will place all amries at once");
			phaseViewModel.allChanged();
			worldDominationModel.stateUpdate(continentMap, countryMap);
			
			Player player = players.get(playerTurn);
			int index = 0;
			List<Country> playerCountries = player.getPlayerCountries();
			int size = playerCountries.size();

			while (player.getArmy() > 0) {
				if (index == size) {
					index = 0;
				}
				countryName = player.getPlayerCountries().get(index).getName();
				gameService.fillPlayerCountry(player, countryName, 1);
				index++;
			}
		} else {
			phaseViewModel.setCurrentPhaseInfo("Player " + players.get(playerTurn).getPlayerName() + " will place "
					+ armyCount + " on " + countryName);
			phaseViewModel.allChanged();
			gameService.fillPlayerCountry(players.get(playerTurn), countryName, armyCount);
		}
	}
	
	/**
	 * This method is used for fortification of armies
	 * @param Player player : this is Player object
	 * @param startCountry : this is the starting country
	 * @param destinationCountry : this is the destination country
	 * @param armyToMove : this is the number of armies to move
	 */
	public void fortify(Player player, String startCountry, String destinationCountry, int armyToMove) {
		
		phaseViewModel.setCurrentPhase("Fortification Phase ::");
		phaseViewModel.setCurrentPhaseInfo("Player "+ player.getPlayerName() + " will fortify from "+ startCountry + " to "+ destinationCountry +" "+ armyToMove+" armies");
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);
		if(startCountry!=null)
		player.fortify(MapService.getObject().getCountry(startCountry.trim()),
				MapService.getObject().getCountry(destinationCountry.trim()), armyToMove);

	}
	/**
	 * This method is used for moving armies after attack phase
	 * @param Player player : this is Playar object
	 * @param startCountry : this is the starting country
	 * @param destinationCountry : this is the destination country
	 * @param armyToMove : this is the number of armies to move
	 */
	public void moveArmyAfterAttack(Player player, String startCountry, String destinationCountry, int armyToMove) {
		
		phaseViewModel.setCurrentPhaseInfo("Player "+ player.getPlayerName() + " will move from "+ startCountry + " to "+ destinationCountry +" "+ armyToMove+" armies");
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);
		
		player.fortify(MapService.getObject().getCountry(startCountry.trim()),
				MapService.getObject().getCountry(destinationCountry.trim()), armyToMove);

	}
	/**
	 *  This method is used for validation of Fortification
	 * @param startCountry : this is the starting country
	 * @param destinationCountry : this is the destination country
	 * @param armyToMove : this is the number of armies to move
	 * @return gameService object
	 */
	public List<String> validateFortification(String startCountry, String destinationCountry, int armyToMove) {

		return gameService.validateFortification(MapService.getObject().getCountry(startCountry),
				MapService.getObject().getCountry(destinationCountry), armyToMove);

	}
	/**
	 * This method is used for Reinforcement phase 
	 * @param Player player : this is Playar object
	 * @param countryName : this is country name
	 * @param armyCount : this is the number of armies
	 */
	public void reinforcementPhase(Player player, String countryName, int armyCount) {
		
		phaseViewModel.setCurrentPhase("Reinforcement Phase::");
		phaseViewModel.setCurrentPhaseInfo("Player "+ player.getPlayerName() + " will reinforce "+ countryName + " with "+ armyCount+ " armies");
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);
		
		Country reinforcementCountry = MapService.getObject().getCountry(countryName);
		player.reinforcementPhase(reinforcementCountry, armyCount);
	}
	/**
	 * This method is used for setting armies for players
	 * @param players : this is a list of Players
	 */
	public void setArmyCountForPlayer(List<Player> players) {
		int armyCount = gameService.getArmyCountBasedOnPlayers(players);
		players.forEach(player -> {
			player.setArmy(armyCount);
		});
	}
	/**
	 * This method is used for loading map into the console
	 * @param players : this is a list of Players
	 */
	public void loadMapOnConsole(List<Player> players) {
		gameService.loadMapOnConsole(players);
	}
	/**
	 * This method is used for reinforcements of armies
	 * @param Player player : this is Player object
	 */
	public void reinforcementArmy(Player player) {
		gameService.reinforcementArmy(player);
	}
	/**
	 * This method is used for validation of selected numbers of dices
	 * @param attackerCountryName : this is the name of country for attacker
	 * @param defenderCountryName : this is the name of country for defender
	 * @param attackerTotalDice : this is the total number of dice for attacker
	 * @param defenderTotalDice : this is the total number of dice for defender
	 * @return gameService object
	 */
	public String validateSelectedNumberOfDice(String attackerCountryName, String defenderCountryName,
			String attackerTotalDice, String defenderTotalDice) {
		Country attackerCountry = MapService.getObject().getCountry(attackerCountryName);
		Country defenderCountry = MapService.getObject().getCountry(defenderCountryName);

		return gameService.validateSelectedNumberOfDice(attackerCountry, defenderCountry, attackerTotalDice,
				defenderTotalDice);
	}
	/**
	 * This method is used for Attack phase
	 * @param Player attacker : this is an object of Player for attacker
	 * @param attackerCountryName : this is the name of country for attacker
	 * @param defenderCountryName : this is the name of country for defender
	 * @param ifAllOut : this is checked all armies out 
	 * @param totalAttackerDice : this is the total number of dice for attacker
	 * @param totalDefenderDice : this is the total number of dice for defender
	 * @return resultMap
	 */
	public Pair<Boolean, Integer> attack(Player attacker,String attackerCountryName, String defenderCountryName, boolean ifAllOut,
			int totalAttackerDice, int totalDefenderDice) {
		Country attackerCountry = MapService.getObject().getCountry(attackerCountryName);
		Country defenderCountry = MapService.getObject().getCountry(defenderCountryName);
		Player defender = defenderCountry.getCountryOwner();
		
		phaseViewModel.setCurrentPhase("Attack Phase");
		phaseViewModel.setCurrentPlayer(attacker.getPlayerName());
		phaseViewModel.setCurrentPhaseInfo("Player "+ attacker.getPlayerName()+ " will try attack from "+ attackerCountryName +" to "+ defenderCountryName);
		Pair<Boolean, Integer> resultMap = attacker.attack(attackerCountry, defenderCountry, defender, ifAllOut,
				totalAttackerDice, totalDefenderDice, phaseViewModel);
		
		if (!cardViewModel.getIfPlayerWonCard() && resultMap.getKey()) {
			cardViewModel.setIfPlayerWonCard(resultMap.getKey());
		}

		if (gameService.ifGameEnded(attackerCountry.getCountryOwner(), countryMap.size())) {
			System.out.println(
					"Game Completed" + attackerCountry.getCountryOwner().getPlayerName() + " has won the game");
			System.exit(0);
		}
		return resultMap;

	}
	/**
	 * This method is used for finishing the atack 
	 * @param Player curPlayer : this is Player object
	 */
	public void finishAttack(Player curPlayer) {
		if (cardViewModel.getIfPlayerWonCard()) {
			if (cardViewModel.getAllCards().size() != 0) {
				cardViewModel.giveCardToPlayer(curPlayer);
			} else {
				System.out.println("Cannot give any card to the player");
			}
		}
		cardViewModel.setIfPlayerWonCard(false);
	}
	/**
	 * This method is used for calculation of cards
	 * @param Player curPlayer : this is Player object
	 */

	public void callCardExchangeView(Player curPlayer) {
		cardViewModel.setCurrentPlayerView(curPlayer);
	}

}

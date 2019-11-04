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

public class GameController {

	GameService gameService = new GameService();

	Map<Integer, Continent> continentMap;

	Map<Integer, Country> countryMap;

	List<Player> playersList;

	PhaseViewModel phaseViewModel;

	WorldDominationViewModel worldDominationModel;

	CardViewModel cardViewModel;

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
	
	public void moveArmyAfterAttack(Player player, String startCountry, String destinationCountry, int armyToMove) {
		
		phaseViewModel.setCurrentPhaseInfo("Player "+ player.getPlayerName() + " will move from "+ startCountry + " to "+ destinationCountry +" "+ armyToMove+" armies");
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);
		
		player.fortify(MapService.getObject().getCountry(startCountry.trim()),
				MapService.getObject().getCountry(destinationCountry.trim()), armyToMove);

	}

	public List<String> validateFortification(String startCountry, String destinationCountry, int armyToMove) {

		return gameService.validateFortification(MapService.getObject().getCountry(startCountry),
				MapService.getObject().getCountry(destinationCountry), armyToMove);

	}

	public void reinforcementPhase(Player player, String countryName, int armyCount) {
		
		phaseViewModel.setCurrentPhase("Reinforcement Phase::");
		phaseViewModel.setCurrentPhaseInfo("Player "+ player.getPlayerName() + " will reinforce "+ countryName + " with "+ armyCount+ " armies");
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);
		
		Country reinforcementCountry = MapService.getObject().getCountry(countryName);
		player.reinforcementPhase(reinforcementCountry, armyCount);
	}

	public void setArmyCountForPlayer(List<Player> players) {

		int armyCount = gameService.getArmyCountBasedOnPlayers(players);
		players.forEach(player -> {
			player.setArmy(armyCount);
		});
	}

	public void loadMapOnConsole(List<Player> players) {
		gameService.loadMapOnConsole(players);
	}

	public void reinforcementArmy(Player player) {
		gameService.reinforcementArmy(player);
	}

	public String validateSelectedNumberOfDice(String attackerCountryName, String defenderCountryName,
			String attackerTotalDice, String defenderTotalDice) {
		Country attackerCountry = MapService.getObject().getCountry(attackerCountryName);
		Country defenderCountry = MapService.getObject().getCountry(defenderCountryName);

		return gameService.validateSelectedNumberOfDice(attackerCountry, defenderCountry, attackerTotalDice,
				defenderTotalDice);
	}

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

	public void callCardExchangeView(Player curPlayer) {
		cardViewModel.setCurrentPlayerView(curPlayer);
	}

}

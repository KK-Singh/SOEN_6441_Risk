package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ControllerHelper.GameControllerHelper;
import ControllerHelper.MapControllerHelper;
import javafx.util.Pair;
import model.CardViewModel;
import model.Continent;
import model.Country;
import model.GameObject;
import model.PhaseViewModel;
import model.Player;
import model.StrategyEnum;
import model.StrategyInterface;
import model.WorldDominationViewModel;
import view.CardExchangeView;
import view.PhaseView;
import view.WorldDominationView;

/**
 * GameController class implements the methods of the game
 * 
 * @author Pegah
 */
public class GameController {

	GameControllerHelper gameService = new GameControllerHelper();

	Map<Integer, Continent> continentMap;

	Map<Integer, Country> countryMap;

	List<Player> playersList;

	PhaseViewModel phaseViewModel;

	WorldDominationViewModel worldDominationModel;

	CardViewModel cardViewModel;

	Map<String, List<String>> tournamentModeResult;

	String statOfTorunament = null;

	MapControllerHelper mapService = MapControllerHelper.getObject();

	/**
	 * This method is used to start the game
	 * 
	 * @param players
	 *            : this is a list of players
	 * @param continentMap
	 *            : this is the map of continent
	 * @param countryMap
	 *            : this is the map of country
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
	 * 
	 * @param players
	 *            : this is a list of players
	 * @param playerTurnp
	 *            : this is player turn
	 * @param allPlaced
	 *            : this is allPlaced for armies
	 * @param countryName
	 *            : this is country name
	 * @param armyCount
	 *            : this is the number of armies
	 */
	public void startUpPhase(List<Player> players, int playerTurn, boolean allPlaced, String countryName,
			int armyCount) {
		phaseViewModel.setCurrentPhase("Startup Phase ::::");
		phaseViewModel.setCurrentPlayer(players.get(playerTurn).getPlayerName());

		if (!players.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
			gameService.nonHumanStartupPhase(players.get(playerTurn), 1, phaseViewModel);
		} else {
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
	}

	/**
	 * This method is used for fortification of armies
	 * 
	 * @param Player
	 *            player : this is Player object
	 * @param startCountry
	 *            : this is the starting country
	 * @param destinationCountry
	 *            : this is the destination country
	 * @param armyToMove
	 *            : this is the number of armies to move
	 */
	public void fortify(Player player, String startCountry, String destinationCountry, int armyToMove) {

		phaseViewModel.setCurrentPhase("Fortification Phase ::");
		phaseViewModel.setCurrentPhaseInfo("Player " + player.getPlayerName() + " will fortify from " + startCountry
				+ " to " + destinationCountry + " " + armyToMove + " armies");
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);
		if (startCountry != null)
			player.fortify(MapControllerHelper.getObject().getCountry(startCountry.trim()),
					MapControllerHelper.getObject().getCountry(destinationCountry.trim()), armyToMove, phaseViewModel);

	}

	/**
	 * This method is used for moving armies after attack phase
	 * 
	 * @param Player
	 *            player : this is Playar object
	 * @param startCountry
	 *            : this is the starting country
	 * @param destinationCountry
	 *            : this is the destination country
	 * @param armyToMove
	 *            : this is the number of armies to move
	 */
	public void moveArmyAfterAttack(Player player, String startCountry, String destinationCountry, int armyToMove) {

		phaseViewModel.setCurrentPhaseInfo("Player " + player.getPlayerName() + " will move from " + startCountry
				+ " to " + destinationCountry + " " + armyToMove + " armies");
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);

		player.fortify(MapControllerHelper.getObject().getCountry(startCountry.trim()),
				MapControllerHelper.getObject().getCountry(destinationCountry.trim()), armyToMove, phaseViewModel);

	}

	/**
	 * This method is used for validation of Fortification
	 * 
	 * @param startCountry
	 *            : this is the starting country
	 * @param destinationCountry
	 *            : this is the destination country
	 * @param armyToMove
	 *            : this is the number of armies to move
	 * @return gameService object
	 */
	public List<String> validateFortification(String startCountry, String destinationCountry, int armyToMove) {

		return gameService.validateFortification(MapControllerHelper.getObject().getCountry(startCountry),
				MapControllerHelper.getObject().getCountry(destinationCountry), armyToMove);

	}

	/**
	 * This method is used for Reinforcement phase
	 * 
	 * @param Player
	 *            player : this is Playar object
	 * @param countryName
	 *            : this is country name
	 * @param armyCount
	 *            : this is the number of armies
	 */
	public void reinforcementPhase(Player player, String countryName, int armyCount) {

		phaseViewModel.setCurrentPhase("Reinforcement Phase::");
		phaseViewModel.setCurrentPhaseInfo("Player " + player.getPlayerName() + " will reinforce " + countryName
				+ " with " + armyCount + " armies");
		phaseViewModel.setCurrentPlayer(player.getPlayerName());
		phaseViewModel.allChanged();
		worldDominationModel.stateUpdate(continentMap, countryMap);

		Country reinforcementCountry = MapControllerHelper.getObject().getCountry(countryName);
		player.reinforcementPhase(reinforcementCountry, armyCount, phaseViewModel);
	}

	/**
	 * This method is used for setting armies for players
	 * 
	 * @param players
	 *            : this is a list of Players
	 */
	public void setArmyCountForPlayer(List<Player> players) {
		int armyCount = gameService.getArmyCountBasedOnPlayers(players);
		players.forEach(player -> {
			player.setArmy(armyCount);
		});
	}

	/**
	 * This method is used for loading map into the console
	 * 
	 * @param players
	 *            : this is a list of Players
	 */
	public void loadMapOnConsole(List<Player> players) {
		gameService.loadMapOnConsole(players);
	}

	/**
	 * This method is used for reinforcements of armies
	 * 
	 * @param Player
	 *            player : this is Player object
	 */
	public void reinforcementArmy(Player player) {
		gameService.reinforcementArmy(player);
	}

	/**
	 * This method is used for validation of selected numbers of dices
	 * 
	 * @param attackerCountryName
	 *            : this is the name of country for attacker
	 * @param defenderCountryName
	 *            : this is the name of country for defender
	 * @param attackerTotalDice
	 *            : this is the total number of dice for attacker
	 * @param defenderTotalDice
	 *            : this is the total number of dice for defender
	 * @return gameService object
	 */
	public String validateSelectedNumberOfDice(String attackerCountryName, String defenderCountryName,
			String attackerTotalDice, String defenderTotalDice) {
		Country attackerCountry = MapControllerHelper.getObject().getCountry(attackerCountryName);
		Country defenderCountry = MapControllerHelper.getObject().getCountry(defenderCountryName);

		return gameService.validateSelectedNumberOfDice(attackerCountry, defenderCountry, attackerTotalDice,
				defenderTotalDice);
	}

	/**
	 * This method is used for Attack phase
	 * 
	 * @param Player
	 *            attacker : this is an object of Player for attacker
	 * @param attackerCountryName
	 *            : this is the name of country for attacker
	 * @param defenderCountryName
	 *            : this is the name of country for defender
	 * @param ifAllOut
	 *            : this is checked all armies out
	 * @param totalAttackerDice
	 *            : this is the total number of dice for attacker
	 * @param totalDefenderDice
	 *            : this is the total number of dice for defender
	 * @return resultMap
	 */
	public Pair<Boolean, Integer> attack(Player attacker, String attackerCountryName, String defenderCountryName,
			boolean ifAllOut, int totalAttackerDice, int totalDefenderDice) {
		Country attackerCountry = MapControllerHelper.getObject().getCountry(attackerCountryName);
		Country defenderCountry = MapControllerHelper.getObject().getCountry(defenderCountryName);
		Player defender = defenderCountry.getCountryOwner();

		phaseViewModel.setCurrentPhase("Attack Phase");
		phaseViewModel.setCurrentPlayer(attacker.getPlayerName());
		phaseViewModel.setCurrentPhaseInfo("Player " + attacker.getPlayerName() + " will try attack from "
				+ attackerCountryName + " to " + defenderCountryName);
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
	 * 
	 * @param Player
	 *            curPlayer : this is Player object
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
	 * 
	 * @param Player
	 *            curPlayer : this is Player object
	 */

	public void callCardExchangeView(Player curPlayer) {
		cardViewModel.setCurrentPlayerView(curPlayer);
	}

	public void playTournament(List<String> allFiles, Map<Player, StrategyEnum> playerStrategyMap, int numberOfGames,
			int maxTurns, boolean ifTournament, List<Player> players) throws IOException, InterruptedException {

		this.playersList = players;
		int result = gameService.validatePlayerStrategyMappingForTournament(playerStrategyMap);

		if (result == -1 || result == -2) {
			phaseViewModel.setCurrentPhaseInfo(phaseViewModel.getCurrentPhaseInfo()
					+ "\n cannot player tournamnet either you've entered a human stratgey for it or there is no strategy for a player");
			phaseViewModel.allChanged();
			return;
		} else {
			for (Player curPlayer : playersList) {
				StrategyInterface si = gameService.getPlayerStrategy(playerStrategyMap, curPlayer);
				if (si == null) {
					return;
				} else {
					curPlayer.setPlayerStrategy(si);
				}
			}
		}

		tournamentModeResult = new HashMap<>();

		for (int i = 0; i < numberOfGames; i++) {
			for (int j = 0; j < allFiles.size(); j++) {
				int movesToDraw = maxTurns;

				String filePath = allFiles.get(j);
				mapService.readFile(filePath);
				this.continentMap = MapControllerHelper.getObject().continentMap;
				this.countryMap = MapControllerHelper.getObject().countryMap;
				startGame(playersList, continentMap, countryMap);
				startUpForNonHumanHelper();

				outer: while (movesToDraw != 0) {
					for (Player curPlayer : playersList) {
						reinforcementForNonHuman(curPlayer);
						Thread.sleep(1000);
						if (attackForNonHuman(curPlayer, playerStrategyMap, filePath)) {
							System.out.println(curPlayer.getPlayerStrategy() + " has won");
							break outer;
						}
						Thread.sleep(1000);
						fortificationForNonHuman(curPlayer);
						movesToDraw--;
					}
				}
				if (movesToDraw == 0) {
					statOfTorunament = "Draw";
					List<String> result2 = new ArrayList<>();
					if (tournamentModeResult.get(filePath) != null) {
						result2 = tournamentModeResult.get(filePath);
						result2.add(statOfTorunament);
						tournamentModeResult.put(filePath, result2);
					} else {
						result2.add(statOfTorunament);
						tournamentModeResult.put(filePath, result2);
					}
				}

				playersList.forEach(p -> {
					// p.setArmy(0);
					setArmyCountForPlayer(playersList);
					p.setCards(new ArrayList<>());
					p.setPlayerCountries(new ArrayList<>());
				});

			}
		}
		if (ifTournament) {
			System.out.print("M : ");
			for (int i = 0; i < allFiles.size(); i++) {
				if (i == allFiles.size() - 1) {
					System.out.print(allFiles.get(i));
				} else {
					System.out.print(allFiles.get(i) + ",");
				}
			}
			System.out.println();
			System.out.print("P : ");

			int i = playerStrategyMap.size();
			Iterator<Player> mapIte = playerStrategyMap.keySet().iterator();
			while (mapIte.hasNext()) {
				Player p = mapIte.next();
				if (i == 1) {
					System.out.print(playerStrategyMap.get(p));
				} else {
					System.out.print(playerStrategyMap.get(p));
					System.out.print(",");
				}
				i--;
			}
			System.out.println();
			System.out.print("G : ");
			System.out.println(numberOfGames);

			System.out.print("D : ");
			System.out.println(maxTurns);

			printTournamentStats(numberOfGames, allFiles.size());
		}
	}

	private void printTournamentStats(int totalGames, int totalFiles) {
		System.out.println();
		StringBuilder sb = new StringBuilder();
		sb.append("      ");
		for (int i = 1; i <= totalGames; i++) {

			sb.append("Game ");
			sb.append(i);
			sb.append(" ");
		}

		System.out.println(sb.toString());

		Iterator<String> resultIte = tournamentModeResult.keySet().iterator();
		int i = 1;
		while (resultIte.hasNext()) {
			String key = resultIte.next();
			List<String> values = tournamentModeResult.get(key);
			sb = new StringBuilder();
			sb.append("Map ");
			sb.append(i);
			sb.append(" ");
			System.out.print(sb.toString());
			values.forEach(val -> {
				System.out.print(val + " ");
			});

			i++;
			System.out.println();
		}

	}

	private void startUpForNonHumanHelper() {
		List<Player> playerWithZeroArmy = new ArrayList<Player>();
		while (playerWithZeroArmy.size() != playersList.size()) {
			for (Player curPlayer : playersList) {
				if (curPlayer.getArmy() == 0) {
					playerWithZeroArmy.add(curPlayer);
					continue;
				}
				gameService.nonHumanStartupPhase(curPlayer, 1, phaseViewModel);
			}
		}
	}

	public void reinforcementForNonHuman(Player curPlayer) {
		reinforcementArmy(curPlayer);
		phaseViewModel.setCurrentPhase("Reinforcement Phase for Non Human::");
		phaseViewModel.setCurrentPlayer(curPlayer.getPlayerName());
		worldDominationModel.stateUpdate(continentMap, countryMap);
		curPlayer.reinforcementPhase(null, 0, phaseViewModel);
	}

	public boolean attackForNonHuman(Player curPlayer, Map<Player, StrategyEnum> playerStrategyMap, String fileName) {
		phaseViewModel.setCurrentPhaseInfo("Attack Phase for Non Human");
		Pair<Boolean, Integer> resultOfAttack = curPlayer.attack(null, null, null, true, 0, 0, phaseViewModel);
		worldDominationModel.stateUpdate(continentMap, countryMap);
		boolean ifWon = resultOfAttack.getKey();

		if (!cardViewModel.getIfPlayerWonCard() && ifWon) {
			cardViewModel.setIfPlayerWonCard(ifWon);
		}

		if (cardViewModel.getIfPlayerWonCard()) {
			if (cardViewModel.getAllCards().size() != 0) {
				cardViewModel.giveCardToPlayer(curPlayer);
			} else {
				phaseViewModel.setCurrentPhaseInfo(
						phaseViewModel.getCurrentPhaseInfo() + "\n don't have cards to give to the player");
				phaseViewModel.allChanged();
			}
			cardViewModel.setIfPlayerWonCard(false);
		}

		if (gameService.ifGameEnded(curPlayer, countryMap.size())) {
			statOfTorunament = playerStrategyMap.get(curPlayer).toString();
			List<String> result = new ArrayList<>();
			if (tournamentModeResult.get(fileName) != null) {
				result = tournamentModeResult.get(fileName);
				;
				result.add(statOfTorunament);
				tournamentModeResult.put(fileName, result);
			} else {
				result.add(statOfTorunament);
				tournamentModeResult.put(fileName, result);
			}

			return true;
		}
		return false;
	}

	public void fortificationForNonHuman(Player curPlayer) {

		phaseViewModel.setCurrentPhase("Fortification Phase for Non Human");
		phaseViewModel.setCurrentPlayer(curPlayer.getPlayerName());
		phaseViewModel.setCurrentPhaseInfo("Fortification Started");

		curPlayer.fortify(null, null, 0, phaseViewModel);
		worldDominationModel.stateUpdate(continentMap, countryMap);

	}

	public boolean saveGame(Player currentPlayer, String currentPhase, String filePath,
			Integer playerIndexForStartUpPhase) {
		File fileToSave = new File(filePath);
		return gameService.saveGame(fileToSave, continentMap, countryMap, playersList, currentPlayer, currentPhase,
				cardViewModel, playerIndexForStartUpPhase);

	}

	public Map<Integer, Object> resumeGame(File fileToLoad) {

		GameObject obj = gameService.resumeSavedGame(fileToLoad);
		if (obj == null) {
			return null;
		} else {
			Map<Integer, Object> answerObj = new HashMap<>();
			playersList = obj.getPlayerList();
			continentMap = obj.getContinentMap();
			countryMap = obj.getCountryMap();
			MapControllerHelper.getObject().continentMap = continentMap;
			MapControllerHelper.getObject().countryMap = countryMap;
			Player currentPlayer = obj.getCurrentPlayer();
			String currentPhase = obj.getCurrentPhase();
			cardViewModel = obj.getCardExchangeViewModel();
			Integer playerIndexForStartUpPhase = obj.getPlayerIndexForStartUpPhase();
			int phaseIndex = resumeHelper(currentPlayer, currentPhase, playerIndexForStartUpPhase, playersList);
			answerObj.put(1, playersList);
			answerObj.put(2, playerIndexForStartUpPhase);
			answerObj.put(3, phaseIndex);
			answerObj.put(4, currentPhase);
			answerObj.put(5, currentPlayer.getPlayerName());
			return answerObj;
		}
	}

	private int resumeHelper(Player currentPlayer, String currentPhase, Integer playerIndexForStartUpPhase,
			List<Player> players) {

		phaseViewModel = new PhaseViewModel();
		phaseViewModel.addObserver(new PhaseView());

		worldDominationModel = new WorldDominationViewModel(players);
		worldDominationModel.addObserver(new WorldDominationView());

		if (currentPhase.equalsIgnoreCase("Startup")) {
			phaseViewModel.setCurrentPhase(currentPhase);
			phaseViewModel.setCurrentPhaseInfo("Start Up Phase for " + currentPlayer.getPlayerName());
			phaseViewModel.setCurrentPlayer(currentPlayer.getPlayerName());
			phaseViewModel.allChanged();
			return 1;
			// startUpPhase(playersList, playerIndexForStartUpPhase, false, ,
			// playersList.get(playerIndexForStartUpPhase).getArmy());
		} else if (currentPhase.equalsIgnoreCase("Reinforcement")) {
			phaseViewModel.setCurrentPhase(currentPhase);
			phaseViewModel.setCurrentPhaseInfo("Reinforcement Phase for " + currentPlayer.getPlayerName());
			phaseViewModel.setCurrentPlayer(currentPlayer.getPlayerName());
			phaseViewModel.allChanged();
			return 2;
		} else if (currentPhase.equalsIgnoreCase("Attack")) {
			phaseViewModel.setCurrentPhase(currentPhase);
			phaseViewModel.setCurrentPhaseInfo("Attack Phase for " + currentPlayer.getPlayerName());
			phaseViewModel.setCurrentPlayer(currentPlayer.getPlayerName());
			phaseViewModel.allChanged();
			return 3;
		} else {
			phaseViewModel.setCurrentPhase(currentPhase);
			phaseViewModel.setCurrentPhaseInfo("Fortification Phase for " + currentPlayer.getPlayerName());
			phaseViewModel.setCurrentPlayer(currentPlayer.getPlayerName());
			phaseViewModel.allChanged();
			return 4;
		}
	}
}

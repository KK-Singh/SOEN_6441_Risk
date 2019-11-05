package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controller.GameController;
import controller.MapController;
import javafx.util.Pair;
import model.Player;

public class StartGame {

	static List<Player> playerList;
	static int playerTurn = 0;

	MapController mapController = new MapController();
	GameController gameController = new GameController();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {
		StartGame startGameObj = new StartGame();

		String input;
		System.out.println("----------------Welcome to Risk Game---------------");

		/*
		 * Logic to run the Map phase command till the map phase is completed.
		 */
		while (true) {
			System.out.println("Choose one of the option to start the game(press seq no): ");
			System.out.println("1. Create Map");
			System.out.println("2. Edit Map");
			System.out.println("3. Start Game");
			input = br.readLine();

			if (input.equalsIgnoreCase("1")) {
				startGameObj.createMapHelper(br, input, startGameObj);
				break;
			} else if (input.equalsIgnoreCase("2")) {
				System.out.println("Enter valid editmap command ");
				input = br.readLine();
				String arr[] = input.split(" ");
				if (startGameObj.validateEditMapCommand(input)) {
					startGameObj.loadMap(arr[1]);
					System.out.println("Enter Map editor command to edit the loaded map");
					startGameObj.createMapHelper(br, input, startGameObj);
					break;
				} else {
					System.out.println("No valid edit map command given");
				}

			} else if (input.equalsIgnoreCase("3")) {
				break;
			} else {
				System.out.println("Choose the valid option");
			}
		}

		System.out.println("----------------Starting the Game-------------");
		System.out.println("Load Map to play the game");

		while (true) {
			input = br.readLine();
			String filePath = startGameObj.validateLoadMapCommands(input);
			try {
				if (filePath != null) {
					String answer = startGameObj.loadMap(filePath);
					if (answer.startsWith("File"))
						break;
//					else
//						System.out.println("Enter Valid Load Map Command");
				} else {
					System.out.println("Enter Valid Load Map Command");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Enter Valid Load Map Command");
			}
		}

		System.out.println("Enter Player related Commands ");
		playerList = new ArrayList<>();
		while (true) {
			input = br.readLine();
			String[] arr = input.split(" ");
			if (startGameObj.validatePlayerCommand(input)) {
				startGameObj.editPlayer(arr);
			} else {
				System.out.println("Enter Valid Player command");
			}
			System.out.println("Do you want to add more players(y/n)");
			input = br.readLine();
			if (input.equalsIgnoreCase("n")) {
				if (playerList.size() < 2) {
					System.out.println("Cannot play game with less than 2 players");
				} else {
					break;
				}
			} else {
				System.out.println("Enter another player");
			}
		}
		System.out.println("Enter command to randomly assign countries to the players");

		while (true) {
			input = br.readLine();
			if (input.equalsIgnoreCase("populatecountries")) {
				startGameObj.gameController.setArmyCountForPlayer(playerList);
				startGameObj.gameController.startGame(playerList, startGameObj.mapController.getContinentMap(),
						startGameObj.mapController.getCountryMap());
				startGameObj.gameController.loadMapOnConsole(playerList);
				break;
			} else {
				System.out.println("Enter valid command");
			}
		}

		System.out.println("-------------Start up phase---------");

		while (true) {
			if (playerTurn == playerList.size()) {
				playerTurn = 0;
			}
			System.out.println("Place armies for " + playerList.get(playerTurn).getPlayerName());
			System.out.println("Total Armies are " + playerList.get(playerTurn).getArmy());
			input = br.readLine();
			if (input.equalsIgnoreCase("showmap")) {
				startGameObj.gameController.loadMapOnConsole(playerList);
				continue;
			} else {
				if (startGameObj.parseStartupCommand(input, playerTurn)) {
					playerTurn++;
				}
			}
			if (startGameObj.ifStartupPhaseEnd()) {
				break;
			}
		}
		System.out.println("--------------End of Startup Phase------------");

		playerTurn = 0;

		while (true) {
			if (playerTurn == playerList.size()) {
				playerTurn = 0;
			}
			Player player = playerList.get(playerTurn);
			startGameObj.gameController.callCardExchangeView(player);
			/*
			 * Reinforcement Phase
			 */
			System.out.println("---------------Reinforcement Phase--------------");
			startGameObj.gameController.reinforcementArmy(player);
			while (true) {
				System.out.println("Place armies for " + player.getPlayerName());
				System.out.println("Total Armies are " + player.getArmy());
				input = br.readLine();
				if (input.equalsIgnoreCase("showmap")) {
					startGameObj.gameController.loadMapOnConsole(playerList);
					continue;
				}
				String[] arr = input.split(" ");
				if (startGameObj.validateReinforcementCommand(input)) {
					try {
						int armyToPlace = Integer.parseInt(arr[2]);
						if (player.getArmy() >= armyToPlace)
							startGameObj.gameController.reinforcementPhase(player, arr[1], armyToPlace);
						else
							System.out.println("Cannot place armies more than what player has");
					} catch (Exception e) {
						System.out.println("Enter Valid Command");
					}

				} else {
					System.out.println("Enter Valid Reinforcement Command");
				}
				if (startGameObj.ifStartupPhaseEnd()) {
					break;
				}
			}

			System.out.println("-------------------Attack Phase--------------");

			while (true) {
				System.out.println("Enter attack and defend command: ");
				String attackCommand = br.readLine();
				String defendCommand = br.readLine();
				if (attackCommand.equalsIgnoreCase("showmap") || defendCommand.equalsIgnoreCase("showmap")) {
					startGameObj.gameController.loadMapOnConsole(playerList);
					continue;
				}
				if (startGameObj.validateAttackCommand(attackCommand)
						&& startGameObj.validateAttackCommand(defendCommand)) {
					String attackCommandArr[] = attackCommand.split(" ");
					String defendCommandArr[] = defendCommand.split(" ");
					boolean ifAllOut = false;
					if (attackCommandArr[attackCommandArr.length - 1].equalsIgnoreCase("-noattack")) {
						System.out.println("---Ending Attack Phase---");
						break;
					} else {
						String fromCountry = attackCommandArr[1];
						String toCountry = attackCommandArr[2];
						Integer attackDiceNumber = Integer.parseInt(attackCommandArr[3]);
						if (attackCommandArr.length == 5 && attackCommandArr[4].equalsIgnoreCase("-allout")) {
							ifAllOut = true;
						}
						Integer defenderDiceNumber = Integer.parseInt(defendCommandArr[1]);
						String ifCommandValid = startGameObj.gameController.validateSelectedNumberOfDice(fromCountry,
								toCountry, attackCommandArr[3], defendCommandArr[1]);
						if (ifCommandValid.equalsIgnoreCase("ValidChoice")) {
							Pair<Boolean, Integer> wonMap = startGameObj.gameController.attack(player, fromCountry,
									toCountry, ifAllOut, attackDiceNumber, defenderDiceNumber);

							if (wonMap.getValue() != null) {
								System.out.println("How many armies do you want to move?");
								String armyMoveCommand = br.readLine();
								startGameObj.moveArmyForAttackHelper(player, armyMoveCommand, fromCountry, toCountry);
							} else {
								System.out.println("Player did not win the country");
							}
						} else {
							System.out.println(ifCommandValid);
						}
					}

				} else {
					System.out.println("Enter valid attack command");
				}

				System.out.println("Do you want to continue the attack(y/n)");
				input = br.readLine();
				if (input.equalsIgnoreCase("n")) {
					startGameObj.gameController.finishAttack(player);
					break;
				}
			}

			System.out.println("----------------Fortification Phase-----------------");
			while (true) {
				input = br.readLine();
				if (input.equalsIgnoreCase("showmap")) {
					startGameObj.gameController.loadMapOnConsole(playerList);
					continue;
				}
				String[] arr = input.split(" ");
				if (startGameObj.validateFortificationCommand(input)) {
					try {
						if (arr.length == 2 && arr[1].equalsIgnoreCase("-none")) {
							startGameObj.gameController.fortify(player, null, null, 0);
							System.out.println("No Fortification");
							break;
						}
						int armyToFortify = Integer.parseInt(arr[3]);

						List<String> result = startGameObj.gameController.validateFortification(arr[1], arr[2],
								armyToFortify);
						if (result.size() > 0) {
							result.forEach(res -> System.out.println(res));
						} else {
							startGameObj.gameController.fortify(player, arr[1], arr[2], armyToFortify);
							break;
						}
					} catch (Exception e) {
						System.out.println("Enter Valid Command");
					}
				} else if (arr.length == 2) {
					break;
				} else {
					System.out.println("Enter Valid command");
				}

				System.out.println("Do you want to try with different country(y/n)");
				input = br.readLine();

				if (input.equalsIgnoreCase("n")) {
					break;
				}
			}

			System.out.println("Do you want to continue the game (y/n)");
			input = br.readLine();
			if (input.equalsIgnoreCase("n"))
				break;
			playerTurn++;
		}
		/*
		 * Logic to run the game phase after the map phase.
		 */

	}

	public boolean parseStartupCommand(String command, int playerTurn) {
		String[] args = command.trim().split(" ");
		switch (args[0]) {
		case "placearmy":
			gameController.startUpPhase(playerList, playerTurn, false, args[1], Integer.parseInt(args[2]));
			gameController.loadMapOnConsole(playerList);
			break;
		case "placeall":
			gameController.startUpPhase(playerList, playerTurn, true, "", 0);
			gameController.loadMapOnConsole(playerList);
			break;
		default:
			System.out.println("Enter Valid Command");
			return false;
		}
		return true;
	}

	public String loadMap(String filePath) {
		String answer = mapController.readFile(filePath);
		if (answer.startsWith("File")) {
			System.out.println("Entered Map is ::: ");
			mapController.showMap();
		}
		return answer;
	}

	public void createMapHelper(BufferedReader br, String input, StartGame startGameObj) throws IOException {
		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("editcontinent");
		possibleValues.add("editcountry");
		possibleValues.add("editneighbor");
		possibleValues.add("showmap");
		possibleValues.add("savemap");
		possibleValues.add("validatemap");

		while (true) {
			System.out.println("Enter Map Creation Commands");
			input = br.readLine();
			String[] arr = input.split(" ");
			if (startGameObj.validateMapEditorCommands(input, possibleValues)) {
				if (arr[0].equalsIgnoreCase("editcontinent")) {
					startGameObj.editContinent(arr);
				} else if (arr[0].equalsIgnoreCase("editcountry")) {
					startGameObj.editCountry(arr);
				} else if (arr[0].equalsIgnoreCase("editneighbor")) {
					startGameObj.editNeighbor(arr);
				} else if (arr[0].equalsIgnoreCase("showmap")) {
					startGameObj.showMap();
				} else if (arr[0].equalsIgnoreCase("savemap")) {
					startGameObj.saveFile(arr[1]);
				} else {
					startGameObj.validateMapCommand();
				}
			} else {
				System.out.println("Enter Valid Map Creation Commands");
			}
			System.out.println("Do you want to enter more commands(y/n)");
			input = br.readLine();
			if (input.equalsIgnoreCase("n"))
				break;

		}

	}

	public void editPlayer(String[] args) {
		switch (args[1]) {
		case "-add":
			playerList.add(new Player(args[2]));
			System.out.println("Player added.");
			break;
		case "-remove":
			Iterator<Player> playerItr = playerList.iterator();
			boolean ifNameCorrect = false;
			while (playerItr.hasNext()) {
				Player p = playerItr.next();
				if (p.getPlayerName().equalsIgnoreCase(args[2])) {
					playerItr.remove();
					ifNameCorrect = true;
					break;
				}
			}
			if (ifNameCorrect)
				System.out.println("Player removed.");
			else
				System.out.println("Wrong Player Name");
			break;
		default:
			System.out.println("Enter valid Player command");
		}
	}

	public void editCountry(String[] args) {
		switch (args[1]) {
		case "-add":
			System.out.println(mapController.addCountry(args[2], args[3]));
			break;
		case "-remove":
			System.out.println(mapController.removeCountry(args[2]));
			break;
		default:
			System.out.println("Enter Valid Edit Country Command");
		}

	}

	public void editContinent(String[] args) {

		switch (args[1]) {
		case "-add":
			System.out.println(mapController.addContinent(args[2], Integer.parseInt(args[3])));
			break;
		case "-remove":
			System.out.println(mapController.removeContinent(args[2]));
			break;
		default:
			System.out.println("Enter Valid Edit Continent Command");
		}

	}

	public void editNeighbor(String[] args) {

		switch (args[1]) {
		case "-add":
			System.out.println(mapController.addNeighbour(args[2], args[3]));
			break;
		case "-remove":
			System.out.println(mapController.removeNeighbour(args[2], args[3]));
			break;
		default:
			System.out.println("Enter Valid Edit Neighbor Command");
		}
	}

	public void showMap() {
		mapController.showMap();
	}

	public void saveFile(String filePath) {
		if (mapController.validateMap()) {
			System.out.println(mapController.saveFile(filePath));
		} else {
			System.out.println("Map is not valid therefore cannot be saved.");
		}
	}

	public void validateMapCommand() {
		mapController.validateMap();
	}

	private String validateLoadMapCommands(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("loadmap")) {
			return arr[1];
		} else {
			return null;
		}
	}

	private boolean validateMapEditorCommands(String input, List<String> possibleValues) {

		String arr[] = input.split(" ");
		if (possibleValues.contains(arr[0])) {
			return true;
		}
		return false;
	}

	
	private boolean validateEditMapCommand(String input) {
		String arr[] = input.split(" ");
		if (arr[0].equalsIgnoreCase("editmap")) {
			return true;
		}
		return false;
	}

	private boolean validatePlayerCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("gameplayer")) {
			return true;
		}
		return false;
	}

	private boolean ifStartupPhaseEnd() {
		for (Player player : playerList) {
			if (player.getArmy() > 0) {
				return false;
			}
		}
		return true;
	}

	private boolean validateReinforcementCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("reinforce")) {
			return true;
		}
		if (arr.length != 3) {
			return false;
		}
		return false;
	}

	private boolean validateAttackCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("attack") || arr[0].equalsIgnoreCase("defend")
				|| arr[0].equalsIgnoreCase("attackmove")) {
			return true;
		}
		return false;
	}

	private boolean validateFortificationCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("fortify")) {
			return true;
		}
		return false;

	}

	private void moveArmyForAttackHelper(Player player, String input, String attackCountry, String defendCountry)
			throws IOException {
		while (true) {

			if (validateArmyMoveComandForAttack(input)) {
				String[] arr = input.split(" ");
				Integer armyToMove = Integer.parseInt(arr[1]);
				gameController.moveArmyAfterAttack(player, attackCountry, defendCountry, armyToMove);
				break;
			} else {
				System.out.println("Enter Valid Move command");
				input = br.readLine();
			}
		}
	}

	private boolean validateArmyMoveComandForAttack(String input) {
		String arr[] = input.split(" ");
		if (arr[0].equalsIgnoreCase("attackmove")) {
			try {
				Integer.parseInt(arr[1]);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;

	}
}

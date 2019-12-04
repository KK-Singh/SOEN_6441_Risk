package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controller.GameController;
import controller.MapController;
import javafx.util.Pair;
import model.AggressiveStrategy;
import model.BenevolentStrategy;
import model.CheaterStrategy;
import model.HumanStrategy;
import model.Player;
import model.RandomStrategy;
import model.StrategyEnum;
import model.StrategyInterface;

/**
 * StartGame Class is the start of the risk game.
 * 
 * @author Kunal
 * @version 1.2
 */

public class StartGame {

	static List<Player> playerList;
	static int playerTurn = 0;

	MapController mapController = new MapController();
	GameController gameController = new GameController();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	Map<Player, StrategyEnum> playerStrategyEnumMap = new HashMap<>();
	List<String> fileLists = new ArrayList<>();
	Integer numberOfGame = 0;
	Integer maxTurn = 0;

	/**
	 * Main method where game starts
	 * 
	 */

	public static void main(String[] args) throws IOException {
		StartGame startGameObj = new StartGame();

		String input;
		Boolean ifResume = false;
		Integer playerIndexForStartUpPhase = null;
		Integer phaseIndex = null;
		String currentPhase = null;
		String currentPlayer = null;
		Map<Integer, Object> resumeIndex = null;
		System.out.println("----------------Welcome to Risk Game---------------");

		/*
		 * Logic to run the Map phase command till the map phase is completed.
		 */
		while (true) {
			System.out.println("Choose one of the option to start the game(press seq no): ");
			System.out.println("1. Create Map");
			System.out.println("2. Edit Map");
			System.out.println("3. Load Saved Game");
			System.out.println("4. Start Game");

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
				System.out.println("Enter the saved file path ");
				input = br.readLine();
				File fileToLoad = new File(input);
				resumeIndex = startGameObj.gameController.resumeGame(fileToLoad);
				if (resumeIndex == null) {
					System.out.println("File Load Failed");
					continue;
				} else {

					playerList = (List<Player>) resumeIndex.get(1);
					playerIndexForStartUpPhase = (Integer) resumeIndex.get(2);
					phaseIndex = (Integer) resumeIndex.get(3);
					currentPhase = (String) resumeIndex.get(4);
					currentPlayer = (String) resumeIndex.get(5);
					ifResume = true;
					break;
				}

			} else if (input.equalsIgnoreCase("4")) {
				break;

			} else {
				System.out.println("Choose the valid option");
			}
		}

		if (ifResume) {
			startGameObj.resumeFromStartUpPhase(phaseIndex, playerIndexForStartUpPhase, currentPhase, currentPlayer,
					input, startGameObj);
			System.exit(0);
		}

		System.out.println("----------------Starting the Game-------------");
		

		System.out.println(
				"Do you want to play tournament mode or single game mode select \n 1. Tournament Mode \n 2. Single Game Mode");
		input = br.readLine();
		if (input.equalsIgnoreCase("1")) {
			System.out.println("Enter Tournament related Commands ");

			while (true) {
				input = br.readLine();
				String arr[] = input.split(" ");
				if (startGameObj.prepareTournament(arr)) {
					break;
				} else {
					System.out.println("Please enter correct tournament mode command");
				}
			}

			try {
				startGameObj.gameController.setArmyCountForPlayer(playerList);
				startGameObj.gameController.playTournament(startGameObj.fileLists, startGameObj.playerStrategyEnumMap,
						startGameObj.numberOfGame, startGameObj.maxTurn, true, playerList);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Load Map to play the game");
			while (true) {
				input = br.readLine();
				String filePath = startGameObj.validateLoadMapCommands(input);
				try {
					if (filePath != null) {
						String answer = startGameObj.loadMap(filePath);
						boolean isValid = startGameObj.mapController.validateMap();

						if (answer.startsWith("File") && isValid)
							break;
						else
							System.out.println("Enter Valid Map file");
					} else {
						System.out.println("Enter Valid Load Map Command");
					}
				} catch (Exception e) {
					// e.printStackTrace();
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
				if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
					startGameObj.gameController.startUpPhase(playerList, playerTurn, false, null, 0);
					playerTurn++;
					if (startGameObj.ifStartupPhaseEnd()) {
						break;
					}
				} else {
					if (playerList.get(playerTurn).getArmy() == 0) {
						playerTurn++;
						if (startGameObj.ifStartupPhaseEnd()) {
							break;
						}
						continue;
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
			}
			System.out.println("--------------End of Startup Phase------------");

			startGameObj.gameController.loadMapOnConsole(playerList);
			playerTurn = 0;

			System.out.println("Do you want to save the game(y/n)? ");
			input = br.readLine();
			if (input.equalsIgnoreCase("y")) {

				while (true) {
					System.out.println("Enter valid file path");
					input = br.readLine();
					Player curPlayer = playerList.get(playerTurn);
					if (startGameObj.gameController.saveGame(curPlayer, "Reinforcement", input, playerTurn)) {
						System.exit(0);
					}

				}

			}

			while (true) {
				if (playerTurn == playerList.size()) {
					playerTurn = 0;
				}
				Player player = playerList.get(playerTurn);
				
				/*
				 * Reinforcement Phase
				 */
				System.out.println("---------------Reinforcement Phase--------------");
				startGameObj.gameController.reinforcementArmy(player);

				while (true) {
					if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
						startGameObj.gameController.reinforcementForNonHuman(playerList.get(playerTurn));
						break;
					} else {
						startGameObj.gameController.callCardExchangeView(player);
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
								e.printStackTrace();
								System.out.println("Enter Valid Command");
							}

						} else {
							System.out.println("Enter Valid Reinforcement Command");
						}
						if (startGameObj.ifStartupPhaseEnd()) {
							break;
						}
					}
				}
				System.out.println("Do you want to save the game(y/n)? ");
				input = br.readLine();
				if (input.equalsIgnoreCase("y")) {

					System.out.println("Enter valid file path");
					while (true) {
						input = br.readLine();
						Player curPlayer = playerList.get(playerTurn);
						if (startGameObj.gameController.saveGame(curPlayer, "Attack", input, playerTurn)) {
							break;
						} else {
							System.out.println("Enter Valid Path");
						}

					}

					break;

				}

				System.out.println("-------------------Attack Phase--------------");

				while (true) {
					if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
						startGameObj.gameController.attackForNonHuman(playerList.get(playerTurn),
								startGameObj.playerStrategyEnumMap, "PlaceHolderAtatck");
						break;
					} else {
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
								String ifCommandValid = startGameObj.gameController.validateSelectedNumberOfDice(
										fromCountry, toCountry, attackCommandArr[3], defendCommandArr[1]);
								if (ifCommandValid.equalsIgnoreCase("ValidChoice")) {
									Pair<Boolean, Integer> wonMap = startGameObj.gameController.attack(player,
											fromCountry, toCountry, ifAllOut, attackDiceNumber, defenderDiceNumber);

									if (wonMap.getValue() != null) {
										System.out.println("How many armies do you want to move?");
										String armyMoveCommand = br.readLine();
										startGameObj.moveArmyForAttackHelper(player, armyMoveCommand, fromCountry,
												toCountry);
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
				}
				System.out.println("Do you want to save the game(y/n)? ");
				input = br.readLine();
				if (input.equalsIgnoreCase("y")) {

					System.out.println("Enter valid file path");
					while (true) {
						input = br.readLine();
						Player curPlayer = playerList.get(playerTurn);
						if (startGameObj.gameController.saveGame(curPlayer, "Fortification", input, playerTurn)) {
							break;
						} else {
							System.out.println("Enter Valid Path");
						}
					}

					break;
				}

				System.out.println("----------------Fortification Phase-----------------");
				while (true) {
					if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
						startGameObj.gameController.fortificationForNonHuman(playerList.get(playerTurn));
						break;
					} else {
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
				}

				System.out.println("Do you want to continue the game (y/n)");
				input = br.readLine();
				if (input.equalsIgnoreCase("n"))
					break;
				playerTurn++;
				System.out.println("Do you want to save the game(y/n)? ");
				input = br.readLine();
				if (input.equalsIgnoreCase("y")) {

					System.out.println("Enter valid file path");
					while (true) {
						input = br.readLine();
						Player curPlayer = playerList.get(playerTurn);
						if (startGameObj.gameController.saveGame(curPlayer, "Reinforcement", input, playerTurn)) {
							break;
						} else {
							System.out.println("Enter Valid Path");
						}
					}

					break;
				}
			}
		}
		/*
		 * Logic to run the game phase after the map phase.
		 */

	}

	/**
	 * Parsing method of StartUp Command
	 * 
	 * @param command
	 *            :Command from console
	 * @param playerTurn
	 *            :playerTurn while game is running
	 * @return : boolean
	 * 
	 */
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

	/**
	 * This method is helping in creating of map
	 * 
	 * @param br
	 *            : buffer reader
	 * @param input
	 *            : String after parsing the command
	 * @param stargameObj:
	 *            GameObject upon which map command would be executed
	 */
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

	/**
	 * editPlayer method is used for adding or removing the player as per given
	 * command
	 * 
	 * @param args
	 *            : editPlayer commands
	 */

	public void editPlayer(String[] args) {
		switch (args[1]) {
		case "-add":
			Player player = new Player(args[2]);
			playerList.add(player);
			StrategyEnum playerEnum = getEnum(args[3]);
			playerStrategyEnumMap.put(player, playerEnum);
			StrategyInterface si = getPlayerStrategy(playerStrategyEnumMap, player);
			player.setPlayerStrategy(si);
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

	private StrategyEnum getEnum(String playerStrategy) {
		switch (playerStrategy) {
		case "Human":
			return StrategyEnum.HUMAN;
		case "Benevolent":
			return StrategyEnum.BENEVOLENT;
		case "Cheater":
			return StrategyEnum.CHEATER;
		case "Random":
			return StrategyEnum.RANDOM;
		case "Aggressive":
			return StrategyEnum.AGGRESSIVE;
		default:
			return null;

		}
	}

	/**
	 * editCountry is used for adding or removing country as per given Command
	 * 
	 * @param args
	 *            :editCountry Commands
	 */

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

	/**
	 * editContinent is used for adding or removing continent as per given Command
	 * 
	 * @param args
	 *            :editContinent Commands
	 */
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

	/**
	 * editNeighbor is used for adding or removing country's neighbor country as per
	 * given Command
	 * 
	 * @param args
	 *            :editNeighbor Commands
	 */
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

	/**
	 * showMap method is used to view the map
	 */
	public void showMap() {
		mapController.showMap();
	}

	/**
	 * saveFile method is to save the map
	 * 
	 * @param filePath
	 *            : where map is to be saved
	 */

	public void saveFile(String filePath) {
		if (mapController.validateMap()) {
			System.out.println(mapController.saveFile(filePath));
		} else {
			System.out.println("Map is not valid therefore cannot be saved.");
		}
	}

	/**
	 * validateMapCommand call the validateMap method
	 */

	public void validateMapCommand() {
		mapController.validateMap();
	}

	/**
	 * It is used to validate load map command
	 * 
	 * @param input
	 *            :load map command
	 * @return String :command after validation
	 */
	private String validateLoadMapCommands(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("loadmap")) {
			return arr[1];
		} else {
			return null;
		}
	}

	/**
	 * It is used to validate map editor command
	 * 
	 * @param input
	 *            :map editor command
	 * @param possibleValues
	 *            : list of map editor command
	 * @return boolean whether map editor command it correct!
	 */
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

	/**
	 * It is used to validate player command
	 * 
	 * @param input
	 *            :player related command
	 * @return :boolean if player related command is correct.
	 */

	private boolean validatePlayerCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("gameplayer") && getEnum(arr[3]) != null) {

			return true;
		}

		return false;
	}

	/**
	 * This method is used to check if the startup Phase has ended
	 * 
	 * @return :boolean
	 */
	private boolean ifStartupPhaseEnd() {
		for (Player player : playerList) {
			if (player.getArmy() > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * this method is check the validity of reinforcement commmand
	 * 
	 * @param input
	 *            reinforcement command
	 * @return boolean
	 */

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

	/**
	 * This method is to validate the attack command
	 * 
	 * @param input
	 *            attack related command
	 * @return boolean
	 */
	private boolean validateAttackCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("attack") || arr[0].equalsIgnoreCase("defend")
				|| arr[0].equalsIgnoreCase("attackmove")) {
			return true;
		}
		return false;
	}

	/**
	 * this method is to validate the fortification command
	 * 
	 * @param input
	 *            fortification related command
	 * @return boolean
	 */

	private boolean validateFortificationCommand(String input) {
		String[] arr = input.split(" ");
		if (arr[0].equalsIgnoreCase("fortify")) {
			return true;
		}
		return false;

	}

	/**
	 * This method helps in moving army after attack
	 * 
	 * @param player
	 *            current player
	 * @param input
	 *            attack command
	 * @param attackCountry
	 *            country which is attacking
	 * @param defendCountry
	 *            country upon attack
	 * @throws IOException
	 */
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

	/**
	 * This method validate Army move command for attack
	 * 
	 * @param input
	 *            command
	 * @return boolean
	 */
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

	private boolean prepareTournament(String[] arr) {
		String fileNames = arr[2];
		String strategyname = arr[4];
		String gameNumber = arr[6];
		String maxTurns = arr[8];
		playerList = new ArrayList<>();
		try {
			for (String temp : fileNames.split(",")) {
				if (temp.length() != 0)
					fileLists.add(temp);
			}
			int i = 1;
			for (String temp : strategyname.split(",")) {

				if (temp.length() != 0) {
					StrategyEnum testEnum = getEnum(temp);
					if (testEnum == null)
						return false;
					Player p = new Player("Player " + i);
					playerList.add(p);
					playerStrategyEnumMap.put(p, testEnum);
				}
				i++;
			}
			numberOfGame = Integer.parseInt(gameNumber);
			maxTurn = Integer.parseInt(maxTurns);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public StrategyInterface getPlayerStrategy(Map<Player, StrategyEnum> mapping, Player curPlayer) {
		StrategyEnum playerEnum = mapping.get(curPlayer);

		switch (playerEnum) {
		case HUMAN:
			return new HumanStrategy();
		case BENEVOLENT:
			return new BenevolentStrategy();
		case RANDOM:
			return new RandomStrategy();
		case CHEATER:
			return new CheaterStrategy();
		case AGGRESSIVE:
			return new AggressiveStrategy();
		default:
			return null;
		}
	}

	private void resumeFromStartUpPhase(Integer phaseIndex, Integer playerIndexForStartUpPhase, String currentPhase,
			String currentPlayer, String input, StartGame startGameObj) throws IOException {

		System.out.println("-------------Start up phase---------");
		playerTurn = playerIndexForStartUpPhase;

		if (phaseIndex == 1) {

			while (true) {
				if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
					startGameObj.gameController.startUpPhase(playerList, playerTurn, false, null, 0);
					playerTurn++;
					if (startGameObj.ifStartupPhaseEnd()) {
						break;
					}
				} else {
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
			}
			System.out.println("--------------End of Startup Phase------------");

			playerTurn = 0;

			while (true) {
				if (playerTurn == playerList.size()) {
					playerTurn = 0;
				}
				Player player = playerList.get(playerTurn);
				
				/*
				 * Reinforcement Phase
				 */
				System.out.println("---------------Reinforcement Phase--------------");
				startGameObj.gameController.reinforcementArmy(player);
				while (true) {
					if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
						startGameObj.gameController.reinforcementForNonHuman(playerList.get(playerTurn));
						break;
					} else {
						startGameObj.gameController.callCardExchangeView(player);
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
								e.printStackTrace();
								System.out.println("Enter Valid Command");
							}

						} else {
							System.out.println("Enter Valid Reinforcement Command");
						}
						if (startGameObj.ifStartupPhaseEnd()) {
							break;
						}
					}
				}

				System.out.println("-------------------Attack Phase--------------");

				while (true) {
					if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
						startGameObj.gameController.attackForNonHuman(playerList.get(playerTurn),
								startGameObj.playerStrategyEnumMap, "PlaceHolderAtatck");
						break;
					} else {

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
								String ifCommandValid = startGameObj.gameController.validateSelectedNumberOfDice(
										fromCountry, toCountry, attackCommandArr[3], defendCommandArr[1]);
								if (ifCommandValid.equalsIgnoreCase("ValidChoice")) {
									Pair<Boolean, Integer> wonMap = startGameObj.gameController.attack(player,
											fromCountry, toCountry, ifAllOut, attackDiceNumber, defenderDiceNumber);

									if (wonMap.getValue() != null) {
										System.out.println("How many armies do you want to move?");
										String armyMoveCommand = br.readLine();
										startGameObj.moveArmyForAttackHelper(player, armyMoveCommand, fromCountry,
												toCountry);
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
				}
				System.out.println("----------------Fortification Phase-----------------");
				while (true) {
					if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
						startGameObj.gameController.fortificationForNonHuman(playerList.get(playerTurn));
						phaseIndex = -1;
						break;
					} else {
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
				}

				System.out.println("Do you want to continue the game (y/n)");
				input = br.readLine();
				if (input.equalsIgnoreCase("n"))
					break;
				playerTurn++;
			}
		} else {
			playerTurn = playerIndexForStartUpPhase;

			while (true) {
				if (playerTurn == playerList.size()) {
					playerTurn = 0;
				}
				Player player = playerList.get(playerTurn);
				
				/*
				 * Reinforcement Phase
				 */
				if (phaseIndex <= 2) {
					System.out.println("---------------Reinforcement Phase--------------");
					startGameObj.gameController.reinforcementArmy(player);
					while (true) {
						if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
							startGameObj.gameController.reinforcementForNonHuman(playerList.get(playerTurn));
							phaseIndex = -1;
							break;
						} else {
							startGameObj.gameController.callCardExchangeView(player);
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
								phaseIndex = -1;
								break;
							}
						}
						phaseIndex = -1;
					}
				}
				if (phaseIndex <= 3) {
					System.out.println("-------------------Attack Phase--------------");

					while (true) {
						if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
							startGameObj.gameController.attackForNonHuman(playerList.get(playerTurn),
									startGameObj.playerStrategyEnumMap, "PlaceHolderAtatck");
							phaseIndex = -1;
							break;
						} else {

							System.out.println("Enter attack and defend command: ");
							String attackCommand = br.readLine();
							String defendCommand = br.readLine();
							if (attackCommand.equalsIgnoreCase("showmap")
									|| defendCommand.equalsIgnoreCase("showmap")) {
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
									if (attackCommandArr.length == 5
											&& attackCommandArr[4].equalsIgnoreCase("-allout")) {
										ifAllOut = true;
									}
									Integer defenderDiceNumber = Integer.parseInt(defendCommandArr[1]);
									String ifCommandValid = startGameObj.gameController.validateSelectedNumberOfDice(
											fromCountry, toCountry, attackCommandArr[3], defendCommandArr[1]);
									if (ifCommandValid.equalsIgnoreCase("ValidChoice")) {
										Pair<Boolean, Integer> wonMap = startGameObj.gameController.attack(player,
												fromCountry, toCountry, ifAllOut, attackDiceNumber, defenderDiceNumber);

										if (wonMap.getValue() != null) {
											System.out.println("How many armies do you want to move?");
											String armyMoveCommand = br.readLine();
											startGameObj.moveArmyForAttackHelper(player, armyMoveCommand, fromCountry,
													toCountry);
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
								phaseIndex = -1;
								break;
							}
						}
						phaseIndex = -1;
					}
				}
				if (phaseIndex <= 4) {
					System.out.println("----------------Fortification Phase-----------------");
					while (true) {
						if (!playerList.get(playerTurn).getPlayerStrategy().toString().contains("Human")) {
							startGameObj.gameController.fortificationForNonHuman(playerList.get(playerTurn));
							phaseIndex = -1;
							break;
						} else {

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

									List<String> result = startGameObj.gameController.validateFortification(arr[1],
											arr[2], armyToFortify);
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
								phaseIndex = -1;
								break;
							}
						}
						phaseIndex = -1;
					}
				}
				System.out.println("Do you want to continue the game (y/n)");
				input = br.readLine();
				if (input.equalsIgnoreCase("n"))
					break;
				playerTurn++;
			}
		}
	}

}

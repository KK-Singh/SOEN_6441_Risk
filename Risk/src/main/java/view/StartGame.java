package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Iterator;

import model.Country;
import model.Player;
import service.MapService;
import service.GameService;
import controller.GamePlay;

public class StartGame {

	List<Player> playerlist;
//	List<Continent> continent;
	MapService objMapService;
	GamePlay objGamePlay;
	boolean startup_phase = false;
	boolean reinforcement_phase = false;
	boolean allarmyplaced = false;

	public void editCountry(String[] args) {
		switch (args[1]) {
		case "-add":
			objMapService.addCountry(args[2], args[3]);
			System.out.println("Country added.");
			break;
		case "-remove":
			objMapService.removeCountry(args[2]);
			System.out.println("Country removed.");
			break;
		default:
			System.out.println("Wrong command");
		}

	}

	public void editContinent(String[] args) {

		switch (args[1]) {
		case "-add":
			objMapService.addContinent(args[2], Integer.parseInt(args[3]));
			System.out.println("Continent added.");
			break;
		case "-remove":
			objMapService.removeContinent(args[2]);
			System.out.println("Continent removed.");
			break;
		default:
			System.out.println("Wrong command");
		}

	}

	public void editNeighbor(String[] args) {

		switch (args[1]) {
		case "-add":
			objMapService.addNeighbour(args[2], args[3]);
			System.out.println("Neighbor added.");
			break;
		case "-remove":
			objMapService.removeNeighbour(args[2], args[3]);
			System.out.println("Neighbor removed.");
			break;
		default:
			System.out.println("Wrong command");
		}
	}

	public void editPlayer(String[] args) {
		switch (args[1]) {
		case "-add":
			playerlist.add(new Player(args[2]));
			System.out.println("Player added.");
			break;
		case "-remove":
			Iterator<Player> playerItr = playerlist.iterator();
			while (playerItr.hasNext()) {
				Player p = playerItr.next();
				System.out.println(p.getPlayerName() + "===" + args[2]);
				if (p.getPlayerName().equalsIgnoreCase(args[2])) {
					playerItr.remove();
					break;
				}
			}
			System.out.println("Player removed.");
			break;
		default:
			System.out.println("Wrong command");
		}
	}

	public boolean parseStartupCommand(String command, int playerTurn) {
		String[] args = command.trim().split(" ");
		switch (args[0]) {
		case "placearmy":
			objGamePlay.startUpPhase(playerlist, playerTurn, false, args[1], Integer.parseInt(args[2]));
			objGamePlay.loadMapOnConsole(playerlist);
			break;
		case "placeall":
			objGamePlay.startUpPhase(playerlist, playerTurn, true, "", 0);
			objGamePlay.loadMapOnConsole(playerlist);
			break;
		case "reinforce":
			objGamePlay.reinforcementPhase(playerlist.get(playerTurn), args[1], Integer.parseInt(args[2]));
			objGamePlay.loadMapOnConsole(playerlist);
		default:
			System.out.println("Invalid Command. Please try again");
			return false;
		}
		return true;
	}

	public boolean parseCommand(String command) {
		String[] args = command.trim().split(" ");
		switch (args[0]) {
		case "editcountry":
			editCountry(args);
			break;
		case "editcontinent":
			editContinent(args);

			break;
		case "editneighbor":
			editNeighbor(args);
			break;
		case "showmap":
			objMapService.showMap();
			break;
		case "validatemap":
			objMapService.mapValidate();
			break;
		case "savemap":
			try {
				objMapService.saveFile(args[1]);
				System.out.println("map saved successfully.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "loadmap":
			try {
				objMapService.readFile(args[1]);
//				System.out.println("Printing continent map after reading the file ");
//				MapService.continentMap.forEach((k,v)->System.out.println(k+" "+v));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "gameplayer":
			editPlayer(args);
			break;
		case "populatecountries":
			objGamePlay.setArmyCountForPlayer(playerlist);
			objGamePlay.startGame(playerlist);
			objGamePlay.loadMapOnConsole(playerlist);
			startup_phase = true;
			break;
		case "fortify":
			System.out.println(args[0]);
			System.out.println(args[1]);
			System.out.println(args[2]);
			System.out.println(args[3]);
			if (Objects.equals(command, "none")) {
				System.out.println("Not to move");
			} else {
				objGamePlay.fortify(args[1], args[2], Integer.parseInt(args[3]));
				objGamePlay.loadMapOnConsole(playerlist);
			}
		default:
			System.out.println("Invalid Command. Please try again");
			return false;
		}
		return true;
	}

	public boolean verifyAllArmyPlaced(List<Player> playerlist) {
		for (Player p : playerlist) {
			if (p.getArmy() != 0)
				return false;
		}
		return true;
	}

	public static void mapEditorCommands(StartGame objStartGame) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command;

		System.out.println("Creation of  Continents");
		System.out.println("Enter continent related command");
		while (true) {
			command = reader.readLine();
			if (Objects.equals(command, "exit")) {
				break;
			}
			objStartGame.parseCommand(command);
			System.out.println("Enter exit to move to next step to add countries");
		}

		System.out.println("Creation of  Countries");
		System.out.println("Enter country related command");
		while (true) {
			command = reader.readLine();
			if (Objects.equals(command, "exit")) {
				break;
			}
			objStartGame.parseCommand(command);
			System.out.println("Enter exit to move to next step to add neighbours.");
		}

		System.out.println("Creation of  Neighbours");
		System.out.println("Enter neighbours related command");
		while (true) {
			command = reader.readLine();
			objStartGame.parseCommand(command);
			if (Objects.equals(command, "exit")) {
				break;
			}
			System.out.println("Enter exit to move to next step and check available choices");
		}

		System.out.println("Map related commands");
		while (true) {
			System.out.println("Choose from below Options");
			System.out.println("1 : showMap");
			System.out.println("2 : saveMap");
			System.out.println("3 : editMap");
			System.out.println("4 : validateMap");
			System.out.println("5 : exit and move to next step");
			Scanner s = new Scanner(System.in);
			int choice = s.nextInt();
			if (choice == 5)
				break;
			switch (choice) {
			case 1:
				System.out.println("Please enter showMap command");
				command = reader.readLine();
				objStartGame.parseCommand(command);
				break;
			case 2:
				System.out.println("Please enter saveMap command");
				command = reader.readLine();
				objStartGame.parseCommand(command);
				break;
			case 3:
				System.out.println("Please enter editMap command");
				command = reader.readLine();
				objStartGame.parseCommand(command);
				break;
			case 4:
				System.out.println("Please enter validateMap command");
				command = reader.readLine();
				objStartGame.parseCommand(command);
				break;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	public static void gamePlayCommands(StartGame objStartGame) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command;

		System.out.println("Load Map commands");
		System.out.println("Enter loadmap related command");
		while (true) {
			command = reader.readLine();
			objStartGame.parseCommand(command);
			if (objStartGame.objMapService.mapValidate()) {
				System.out.println("Map is loaded Sucessfully.");
				break;
			} else
				System.out.println("Enter loadmap related command");
		}

		System.out.println("Player related commands");
		while (true) {
			command = reader.readLine();
			if (Objects.equals(command, "exit") && objStartGame.playerlist.size() > 1) {
				if (objStartGame.playerlist.size() < 2) {
					System.out.println("Please add atlease 2 players");
					continue;
				}
				break;
			}
			objStartGame.parseCommand(command);
			
			System.out.println("Enter exit to move to next step and to populate countires.");
		}

		System.out.println("enter Populate countries related command");
		command = reader.readLine();
		objStartGame.parseCommand(command);
	}

	public static void startupPhaseCommands(StartGame objStartGame) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command;

		System.out.println("\nStartup Phase\n");
		System.out.println("Enter startupPhase related command");

		while (true) {

			if (objStartGame.verifyAllArmyPlaced(objStartGame.playerlist))
				break;

			for (int index = 0; index < objStartGame.playerlist.size(); index++) {
				Player player = objStartGame.playerlist.get(index);
				if (player.getArmy() != 0) {
					System.out.println("Place army command for " + player.getPlayerName());
					try {
						String temp = reader.readLine();
						String[] args = temp.trim().split(" ");
						try {
							if (!Objects.equals(args[0], "placeall")) {
								if (Integer.parseInt(args[2]) > player.getArmy()) {
									System.out.println("You can't set more armies than you have");
									break;
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							break;
						}
						objStartGame.parseStartupCommand(temp, index);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void reinforcementPhaseCommands(StartGame objStartGame, int playerIndex) throws IOException {

		System.out.println("\n Reinforcement Phase\n");
		System.out.println("Enter Reinforcement related command");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Player player = objStartGame.playerlist.get(playerIndex);
		objStartGame.objGamePlay.reinforcementArmy(player);

		while (player.getArmy() != 0) {
			System.out.println(player.getPlayerName() + " Have " + player.getArmy() + " Army left");
			System.out.println("Place army command for " + player.getPlayerName());
			try {
				String temp = reader.readLine();
				String[] args = temp.trim().split(" ");
				try {
					if (Integer.parseInt(args[2]) > player.getArmy()) {
						System.out.println("You can't set more armies than you have");
						continue;
					}
				} catch (Exception e) {
					// TODO: handle exception
					continue;
				}
				objStartGame.parseStartupCommand(temp, playerIndex);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Fortification phase for " + player.getPlayerName());
			System.out.println("Enter fortification related command");
		}
	}

	public static void main(String[] args) throws IOException {

		StartGame objStartGame = new StartGame();
		// Initialize MapService
		objStartGame.objMapService = MapService.getObject();
		objStartGame.playerlist = new ArrayList<Player>();
		objStartGame.objGamePlay = new GamePlay();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Welcome to risk Game\n");
		System.out.println("Choose from below Options");
		System.out.println("1 : LoadMap");
		System.out.println("2 : CreateMap");

		Scanner s = new Scanner(System.in);
		switch (s.nextInt()) {
		case 1:
			gamePlayCommands(objStartGame);
			break;
		case 2:
			mapEditorCommands(objStartGame);
			break;
		default:
			break;
		}
		int nextPlayer = 0;

		int totalPlayerCount = objStartGame.playerlist.size();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {

			if(nextPlayer==totalPlayerCount) {
				nextPlayer=0;
			}
			if (objStartGame.startup_phase) {
				startupPhaseCommands(objStartGame);
				objStartGame.startup_phase = false;
				objStartGame.reinforcement_phase = true;
			}

			reinforcementPhaseCommands(objStartGame,nextPlayer);
			String command = br.readLine();
			objStartGame.parseCommand(command);
			nextPlayer++;
			System.out.println("Do you want to end the game (y/n)");
			command = s.next();
			if(command.equalsIgnoreCase("y")) {
				break;
			}
		}

		System.out.println("---- Build 1 End ----");
	}

}

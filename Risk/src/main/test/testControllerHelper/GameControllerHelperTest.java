package testControllerHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ControllerHelper.GameControllerHelper;
import ControllerHelper.MapControllerHelper;
import model.Continent;
import model.Country;
import model.Player;
import model.RandomStrategy;
import model.StrategyEnum;

/**
 * This class have test cases for Game Controllers
 * @author kunal
 * 
 */
public class GameControllerHelperTest {

	GameControllerHelper gameService;

	Map<Integer, Country> countryMapValid;
	Map<Integer, Continent> continentMapValid;
/**
 * this method setup require common context before every test is run
 */
	@Before
	public void initialize() {
		gameService = new GameControllerHelper();
	}
/**
 * This test will test the StartUpPhase
 */
	@Test
	public void testValidStartUpPhase() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		gameService.assignCountriesAtRandom(playerList);

		int army = p1.getArmy();
		Country temp = p1.getPlayerCountries().get(0);
		int prevCountryArmy = temp.getArmyCount();

		gameService.fillPlayerCountry(p1, temp.getName(), army);
		assertTrue(temp.getArmyCount() == (prevCountryArmy + army));
	}
/**
 * this test will test reinforcement of Armies
 */
	@Test
	public void testReinforcementArmies() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));
		p1.setArmy(0);
		p2.setArmy(0);

		gameService.reinforcementArmy(p1);
		gameService.reinforcementArmy(p2);

		assertEquals(7, p1.getArmy());
		assertEquals(8, p2.getArmy());
	}
/**
 * this test will test if the Game is running
 */
	@Test
	public void testIfgameEndedFalse() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		gameService.assignCountriesAtRandom(playerList);
		assertTrue(!gameService.ifGameEnded(p1, countryMapValid.size()));
	}

	@Test
	public void testIfgameEndedTrue() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2), countryMapValid.get(3),
				countryMapValid.get(4)));
		assertTrue(gameService.ifGameEnded(p1, countryMapValid.size()));
	}
/**
 * this test will test Continent Owned by player
 */
	@Test
	public void testIfContinentOwnedTrue() {

		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2), countryMapValid.get(3),
				countryMapValid.get(4)));
		assertTrue(gameService.ifContinetOwned(continentMapValid.get(1), p1));
	}

	@Test
	public void testIfContinentOwnedFalse() {

		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2), countryMapValid.get(3),
				countryMapValid.get(4)));
		assertTrue(!gameService.ifContinetOwned(continentMapValid.get(1), p2));
	}
	/**
	 * this test will test dice roll 
	 */
	@Test
	public void testValidateSelectedNumberOfDiceWrongInput() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(3);
		countryMapValid.get(3).setArmyCount(5);

		assertEquals("Enter Valid Number for Attacker and Defender", gameService
				.validateSelectedNumberOfDice(countryMapValid.get(1), countryMapValid.get(3), "dss", "adsds"));
	}
	/**
	 * this test will test if the dice rolled by attacker is wrong
	 */
	@Test
	public void testValidateSelectedNumberOfDiceAttackerWrong() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(2);
		countryMapValid.get(3).setArmyCount(5);

		assertEquals("Selected attacker can roll min 1 and max 1",
				gameService.validateSelectedNumberOfDice(countryMapValid.get(1), countryMapValid.get(3), "3", "2"));
	}
	/**
	 * this test will test if the dice rolled by defender is wrong
	 */
	@Test
	public void testValidateSelectedNumberOfDiceDefenderWrong() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(5);
		countryMapValid.get(3).setArmyCount(1);

		assertEquals("Selected defender can roll min 1 and max 1",
				gameService.validateSelectedNumberOfDice(countryMapValid.get(1), countryMapValid.get(3), "2", "2"));
	}
	/**
	 * this test will test number of dice rolled is correct
	 */
	@Test
	public void testValidateSelectedNumberOfDiceCorrectInput() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(3);
		countryMapValid.get(3).setArmyCount(5);

		assertEquals("ValidChoice",
				gameService.validateSelectedNumberOfDice(countryMapValid.get(1), countryMapValid.get(3), "2", "2"));
	}
	/**
	 * this test will test fortification
	 */
	@Test
	public void testValidateFortificationWrongCountry() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		countryMapValid.get(1).setCountryOwner(p1);
		countryMapValid.get(2).setCountryOwner(p1);
		countryMapValid.get(3).setCountryOwner(p2);
		countryMapValid.get(4).setCountryOwner(p2);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(3);
		countryMapValid.get(3).setArmyCount(5);
		assertTrue(gameService.validateFortification(countryMapValid.get(1), countryMapValid.get(3), 2).size() > 0);
	}
	/**
	 * this test will test if army move during fortification phase is valid
	 */
	@Test
	public void testValidateFortificationWrongArmyMove() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		countryMapValid.get(1).setCountryOwner(p1);
		countryMapValid.get(2).setCountryOwner(p1);
		countryMapValid.get(3).setCountryOwner(p2);
		countryMapValid.get(4).setCountryOwner(p2);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(1);
		countryMapValid.get(3).setArmyCount(5);
		assertEquals(Arrays.asList("Cannot move army from the Country"),
				gameService.validateFortification(countryMapValid.get(1), countryMapValid.get(2), 2));
	}
	/**
	 * this test will test fortification 
	 */
	@Test
	public void testValidateFortificationCorrect() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapControllerHelper.getObject().continentMap = continentMapValid;
		MapControllerHelper.getObject().countryMap = countryMapValid;
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		p1.setArmy(armyCount);
		p2.setArmy(armyCount);
		countryMapValid.get(1).setCountryOwner(p1);
		countryMapValid.get(2).setCountryOwner(p1);
		countryMapValid.get(3).setCountryOwner(p2);
		countryMapValid.get(4).setCountryOwner(p2);
		p1.setPlayerCountries(Arrays.asList(countryMapValid.get(1), countryMapValid.get(2)));
		p2.setPlayerCountries(Arrays.asList(countryMapValid.get(3), countryMapValid.get(4)));

		countryMapValid.get(1).setArmyCount(3);
		countryMapValid.get(3).setArmyCount(5);
		assertTrue(gameService.validateFortification(countryMapValid.get(1), countryMapValid.get(2), 2).size() == 0);
	}
	/**
	 * this test will test the Army count based on player
	 */
	@Test
	public void testGetArmyCountBasedOnPlayers() {
		Player p1, p2;
		List<Player> playerList = new ArrayList<>();
		p1 = new Player("p1");
		p2 = new Player("p2");
		playerList.add(p1);
		playerList.add(p2);
		int armyCount = gameService.getArmyCountBasedOnPlayers(playerList);
		assertEquals(40, armyCount);
	}

	/**
	 * this test will test if SaveMap is invalid  
	 */

	@Test
	public void testSaveMapFail() {
		assertTrue(!gameService.saveGame(null, continentMapValid, countryMapValid, null, null, null, null, -1));
	}
	/**
	 * this test will test if save Map is valid
	 */
	@Test
	public void testSaveMapPass() {
		File f = new File("Resources\\TestingSave.map");
		assertTrue(gameService.saveGame(f, continentMapValid, countryMapValid, null, null, null, null, -1));
	}
	/**
	 * this test will test if the LoadMap is invalid
	 */
	@Test
	public void testLoadMapFail() {
		assertTrue(gameService.resumeSavedGame(null) == null);
	}
	/**
	 * this test will test if the Load Map is Pass
	 */
	@Test
	public void testLoadMapPass() {
		File f = new File("Resources\\TestingLoadMap.map");
		assertTrue(gameService.resumeSavedGame(f) != null);
	}
	/**
	 * this test will test if the player strategy for tournament Human fail
	 */
	@Test
	public void testPlayerStartegyForTournamentHumanFail() {
		Map<Player,StrategyEnum> map = new HashMap<>();
		map.put(new Player("P1"), StrategyEnum.HUMAN);
		map.put(new Player("P2"), StrategyEnum.HUMAN);
		assertTrue(gameService.validatePlayerStrategyMappingForTournament(map)==-2);
	}
	/**
	 * this test will test if the player strategy for tournament Human pass
	 */
	@Test
	public void testPlayerStartegyForTournamentNonHumanPass() {
		Map<Player,StrategyEnum> map = new HashMap<>();
		map.put(new Player("P1"), StrategyEnum.CHEATER);
		map.put(new Player("P2"), StrategyEnum.RANDOM);
		assertTrue(gameService.validatePlayerStrategyMappingForTournament(map)==0);
	}
	/**
	 * this test will test if the player strategy is null 
	 */
	@Test
	public void testPlayerStartegyForTournamentNullFail() {
		Map<Player,StrategyEnum> map = new HashMap<>();
		map.put(new Player("P1"), null);
		map.put(new Player("P2"), StrategyEnum.RANDOM);
		assertTrue(gameService.validatePlayerStrategyMappingForTournament(map)==-1);
	}
	/**
	 * this test will test if all player strategy is null
	 */
	@Test
	public void testPlayerStartegyForTournamentAllNullFail() {
		Map<Player,StrategyEnum> map = new HashMap<>();
		map.put(new Player("P1"), null);
		map.put(new Player("P2"), null);
		assertTrue(gameService.validatePlayerStrategyMappingForTournament(map)==-1);
	}
	/**
	 * this test will test if the player strategy exist
	 */
	@Test
	public void testGetPlayerStrategyIfExists() {
		Map<Player,StrategyEnum> map = new HashMap<>();
		Player p1 = new Player("P1");
		Player p2 = new Player("P2");
		map.put(p1, StrategyEnum.CHEATER);
		map.put(p2, StrategyEnum.RANDOM);
		
		assertTrue(gameService.getPlayerStrategy(map, p2) instanceof RandomStrategy);
	}
	
	@Test
	public void testGetPlayerStrategyIfNotExists() {
		Map<Player,StrategyEnum> map = new HashMap<>();
		Player p1 = new Player("P1");
		Player p2 = new Player("P2");
		map.put(p2, StrategyEnum.RANDOM);
		
		assertTrue(gameService.getPlayerStrategy(map, p1)==null);
	}
	/**
	 * this test is to test setting up valid map
	 */
	private void setUpValidMap() {
		Continent cont1 = new Continent(4, "Cont1");
		Continent cont2 = new Continent(5, "Cont2");

		Country c1 = new Country("C1");
		c1.setContinent(cont1);
		c1.setArmyCount(10);
		Country c2 = new Country("C2");
		c2.setContinent(cont1);
		c2.setArmyCount(12);

		Country c3 = new Country("C3");
		c3.setContinent(cont2);
		c3.setArmyCount(5);

		Country c4 = new Country("C4");

		c4.setContinent(cont2);
		c4.setArmyCount(7);

		c3.setNeighbors(Arrays.asList(c4, c1));
		c4.setNeighbors(Arrays.asList(c3));
		c1.setNeighbors(Arrays.asList(c2, c3));
		c2.setNeighbors(Arrays.asList(c1));

		cont1.setCountries(Arrays.asList(c1, c2));
		cont2.setCountries(Arrays.asList(c3, c4));

		countryMapValid.put(1, c1);
		countryMapValid.put(2, c2);
		countryMapValid.put(3, c3);
		countryMapValid.put(4, c4);

		continentMapValid.put(1, cont1);
		continentMapValid.put(2, cont2);
	}
}

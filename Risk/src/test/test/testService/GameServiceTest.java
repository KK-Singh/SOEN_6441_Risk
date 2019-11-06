package testService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.Continent;
import model.Country;
import model.Player;
import service.GameService;
import service.MapService;

public class GameServiceTest {

	GameService gameService;

	Map<Integer, Country> countryMapValid;
	Map<Integer, Continent> continentMapValid;

	@Before
	public void initialize() {
		gameService = new GameService();
	}

	@Test
	public void testValidStartUpPhase() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testReinforcementArmies() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testIfgameEndedFalse() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testIfContinentOwnedTrue() {

		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateSelectedNumberOfDiceWrongInput() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateSelectedNumberOfDiceAttackerWrong() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateSelectedNumberOfDiceDefenderWrong() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateSelectedNumberOfDiceCorrectInput() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateFortificationWrongCountry() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateFortificationWrongArmyMove() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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

	@Test
	public void testValidateFortificationCorrect() {
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer, Continent>();
		setUpValidMap();
		MapService.getObject().continentMap = continentMapValid;
		MapService.getObject().countryMap = countryMapValid;
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
	
	public void testValidMoveAfterConquering() {
		
	}

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

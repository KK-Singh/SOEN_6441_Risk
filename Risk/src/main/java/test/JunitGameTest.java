package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Player;
import service.GameService;
import service.MapService;

public class JunitGameTest {

	MapService objMapService;
	GameService objGameService;
	Player objPlayer;
	
	
	public void initialize()
	{
		objMapService = MapService.getObject();
		objGameService = new GameService();
		objMapService.addContinent("NewContinent", 100);
		
		objMapService.addCountry("NewCountry", "NewContinent");
		objMapService.addCountry("NewCountry2", "NewContinent");
		objMapService.addCountry("Pakistan", "NewContinent");
		objMapService.addCountry("Philippines", "NewContinent");
		
		objMapService.addNeighbour("NewCountry", "NewCountry2");
		objMapService.addNeighbour("Pakistan", "NewCountry");
		objMapService.addNeighbour("Philippines", "NewCountry2");
		
		List<Player> playerList = new ArrayList<>();
		objPlayer = new Player("TestPlayer");
		playerList.add(objPlayer);
		objGameService.assignCountriesAtRandom(playerList);
	}
	
	@Before
	public void setUp()
	{}
	
	@Test
	public void checkReinforcementArmies() throws IOException
	{
		objMapService = null;
		objGameService = null;
		objPlayer = null;
		
		initialize();
		objGameService.reinforcementArmy(objPlayer);
		assertEquals(103, objPlayer.getArmy());
	}
	
	@Test
	public void checkInitialAllocation()
	{
		List<Player>  lstPlayer= new ArrayList<>();
		lstPlayer.add(new Player("1"));
		lstPlayer.add(new Player("2"));
		lstPlayer.add(new Player("3"));
		objGameService = new GameService();
		assertEquals(35, objGameService.getArmyCountBasedOnPlayers(lstPlayer));
	}

	@Test
	public void checkContinentOwnership()
	{
		objMapService = null;
		objGameService = null;
		objPlayer = null;
		
		initialize();
		boolean isContinentOwned = objGameService.ifContinetOwned(objMapService.getContinent("NewContinent"), objPlayer);
		assertTrue(isContinentOwned);
	}
}



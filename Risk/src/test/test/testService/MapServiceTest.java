package testService;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import model.Continent;
import model.Country;
import service.MapService;

public class MapServiceTest {

	
	@Test
	public void testValidMap() throws IOException {
		MapService.getObject().readFile("Resources/Asia.map");
		assertTrue(MapService.getObject().mapValidate());
	}

	@Test
	public void testInvalidMap() throws IOException {
		MapService.getObject().readFile("Resources/test.map");
		assertTrue(!MapService.getObject().mapValidate());
	}
	
	@Test
	public void testAddContinent() {
		MapService.getObject().continentMap = new HashMap<Integer, Continent>();
		MapService.getObject().addContinent("cont1", 4);
		assertTrue(MapService.getObject().continentMap.size()==1);
	}
	
	@Test
	public void testAddCountry() {
		MapService.getObject().countryMap = new HashMap<Integer, Country>();
		MapService.getObject().continentMap = new HashMap<Integer, Continent>();
		MapService.getObject().addContinent("cont1", 4);
		MapService.getObject().addCountry("country1", "cont1");
		assertTrue(MapService.getObject().countryMap.size()==1);
	}
}

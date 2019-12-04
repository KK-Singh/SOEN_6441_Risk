package testControllerHelper;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import ControllerHelper.MapControllerHelper;
import model.Continent;
import model.Country;

public class MapControllerHelperTest {

	
	@Test
	public void testValidMap() throws IOException {
		MapControllerHelper.getObject().readFile("Resources/RiskEurope.map");
		assertTrue(!MapControllerHelper.getObject().mapValidate());
	}

	@Test
	public void testInvalidMap() throws IOException {
		MapControllerHelper.getObject().readFile("Resources/test.map");
		assertTrue(!MapControllerHelper.getObject().mapValidate());
	}
	
	@Test
	public void testAddContinent() {
		MapControllerHelper.getObject().continentMap = new HashMap<Integer, Continent>();
		MapControllerHelper.getObject().addContinent("cont1", 4);
		assertTrue(MapControllerHelper.getObject().continentMap.size()==1);
	}
	
	@Test
	public void testAddCountry() {
		MapControllerHelper.getObject().countryMap = new HashMap<Integer, Country>();
		MapControllerHelper.getObject().continentMap = new HashMap<Integer, Continent>();
		MapControllerHelper.getObject().addContinent("cont1", 4);
		MapControllerHelper.getObject().addCountry("country1", "cont1");
		assertTrue(MapControllerHelper.getObject().countryMap.size()==1);
	}

	@Test
	public void testReadDominationFileFail() {
		try {
			MapControllerHelper.getObject().countryMap = new HashMap<>();
			MapControllerHelper.getObject().readFile("Resources/RiskEurope_Wrong.map");
			assertTrue(MapControllerHelper.getObject().countryMap.size()==0);
		} catch (IOException e) {
			assertTrue(true);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadDominationFilePass() {
		try {
			MapControllerHelper.getObject().readFile("Resources/RiskEurope.map");
			assertTrue(true);
		} catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadConquestFileFail() {
		try {
			MapControllerHelper.getObject().countryMap = new HashMap<>();
			MapControllerHelper.getObject().readFile("Resources/Asia_Wrong.map");
			assertTrue(MapControllerHelper.getObject().countryMap.size()==0);
		} catch (IOException e) {
			assertTrue(true);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadConquestFilePass() {
		try {
			MapControllerHelper.getObject().readFile("Resources/Asia.map");
			assertTrue(true);
		} catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
}

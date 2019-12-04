/**
 * 
 */
package testControllerHelper;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ControllerHelper.MapVerification;
import model.Continent;
import model.Country;

/**
 * This class have test cases for map verification
 * @author Pegah
 *
 */
public class MapVerificationTest {
	/**
	 * this hold the instance of MapVerication object for valid map
	 */
	public MapVerification objMapVerificationValidMap;
	/**
	 * this hold the instance of Mapverification for Invalid Map
	 */
	public MapVerification objMapVerificationInvalidMap;

	Map<Integer,Country> countryMapInvalid;
	Map<Integer,Continent> continentMapInvalid;
	
	Map<Integer,Country> countryMapValid;
	Map<Integer,Continent> continentMapValid;
	
	/**
	 * this method setup require common context before every test is run
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		countryMapInvalid = new HashMap<Integer, Country>();
		continentMapInvalid = new HashMap<Integer,Continent>();
		
		countryMapValid = new HashMap<Integer, Country>();
		continentMapValid = new HashMap<Integer,Continent>();

		setUpInvalidMap();
		setUpValidMap();
		objMapVerificationInvalidMap = new MapVerification(countryMapInvalid,continentMapInvalid);
		objMapVerificationValidMap = new MapVerification(countryMapValid,continentMapValid);
		
		
	}
	
	/**
	 * This test will test neighbors 
	 */
	@Test
	public void checkNeighborsInvalidMap() {
		
		objMapVerificationInvalidMap.Neighborcheck();
		assertTrue(objMapVerificationInvalidMap.ErrorList.size()>0);
	}
	/**
	 * this test will test if there is any unused continent in map
	 */
	@Test
	public void checkUsedContinentsInvalidMap() {
		objMapVerificationInvalidMap.ContinentUnusedCheck();
		assertTrue(objMapVerificationInvalidMap.ErrorList.size()>0);
	}
	/**
	 * this test will test if the country is in multiple Continent
	 */
	@Test
	public void CountryInMultiContinentCheckInvalidMap() {
		
		objMapVerificationInvalidMap.CountryInMultiContinentCheck();
		assertTrue(objMapVerificationInvalidMap.ErrorList.size()>0);
	}

	/**
	 * this test will test the neighbors in a valid map
	 */
	@Test
	public void checkNeighborsValidMap() {
		
		objMapVerificationValidMap.Neighborcheck();
		assertTrue(objMapVerificationValidMap.ErrorList.size()==0);
	}
	/**
	 * this test will test verify the Continent in valid map
	 */
	@Test
	public void checkUsedContinentsValidMap() {
		objMapVerificationValidMap.ContinentUnusedCheck();
		assertTrue(objMapVerificationValidMap.ErrorList.size()==0);
	}
	/**
	 * this test is to test the country in Multiple Continent in Valid map
	 */
	@Test
	public void CountryInMultiContinentCheckValidMap() {
		
		objMapVerificationValidMap.CountryInMultiContinentCheck();
		assertTrue(objMapVerificationValidMap.ErrorList.size()==0);
	}
	/**
	 * this test will test if there is no country in a Continent
	 */
	@Test
	public void NoContinentOrCountryValidMap() {
		objMapVerificationValidMap.NoContinentOrCountry();
		assertTrue(objMapVerificationValidMap.ErrorList.size()==0);
	}
	
	/**
	 * this method is to create instances for invalid map
	 */
	private void setUpInvalidMap() {
		
		Continent cont1 = new Continent(4, "Cont1");
		Continent cont2 = new Continent(5, "Cont2");
		Continent cont3 = new Continent(6, "Cont3");
		
		Country c1 = new Country("C1");
		c1.setContinent(cont1);
		c1.setArmyCount(10);
		Country c2 = new Country("C2");
		c2.setContinent(cont1);
		c2.setArmyCount(12);
		
		c1.setNeighbors(Arrays.asList(c2));
		c2.setNeighbors(Arrays.asList(c1));
		
		Country c3 = new Country("C1");
		c3.setContinent(cont2);
		c3.setArmyCount(5);
		
		Country c4 = new Country("C2");
		
		c4.setContinent(cont2);
		c4.setArmyCount(7);
		
		c3.setNeighbors(Arrays.asList(c4));
//		c4.setNeighbors(Arrays.asList(c3));

		cont1.setCountries(Arrays.asList(c1,c2));
		cont2.setCountries(Arrays.asList(c3,c4,c1));
		
		countryMapInvalid.put(1, c1);
		countryMapInvalid.put(2, c2);
		countryMapInvalid.put(3, c3);
		countryMapInvalid.put(4, c4);
		
		continentMapInvalid.put(1, cont1);
		continentMapInvalid.put(2, cont2);
		continentMapInvalid.put(3, cont3);
	}
	/**
	 * this method is for creating the instance for valid map test
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
		
		
		
		Country c3 = new Country("C1");
		c3.setContinent(cont2);
		c3.setArmyCount(5);
		
		Country c4 = new Country("C2");
		
		c4.setContinent(cont2);
		c4.setArmyCount(7);
		
		c3.setNeighbors(Arrays.asList(c4,c1));
		c4.setNeighbors(Arrays.asList(c3));
		c1.setNeighbors(Arrays.asList(c2,c3));
		c2.setNeighbors(Arrays.asList(c1));
		
		cont1.setCountries(Arrays.asList(c1,c2));
		cont2.setCountries(Arrays.asList(c3,c4));
		
		countryMapValid.put(1, c1);
		countryMapValid.put(2, c2);
		countryMapValid.put(3, c3);
		countryMapValid.put(4, c4);
		
		
		continentMapValid.put(1, cont1);
		continentMapValid.put(2, cont2);
	}
}

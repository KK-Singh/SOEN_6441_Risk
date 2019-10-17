/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import controller.MapVerification;
import service.MapService;
/**
 * @author Pegah
 *
 */
public class JunitMaptest {
	
	public MapService objMapService;
	public MapVerification objMapVerification;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
//		objMapService = new MapService();
	}

	@Test
	public void testIsMapConnected() throws IOException
	{
		//Read pre-defined map file 
		objMapService.getObject().readFile("./Asia.map");
		assertTrue(objMapService.mapValidate());
	}
//	
//	Neighborcheck();
//	ContinentUnusedCheck();
//	CountryInMultiContinentCheck();
//	NoContinentOrCountry();
	@Test
	public void checkNeighbors()
	{
		objMapVerification = new MapVerification(objMapService.getObject().countryMap, objMapService.getObject().continentMap);
		objMapVerification.Neighborcheck();
		assertEquals(0, objMapVerification.ErrorList.size());
	}
	
	@Test
	public void checkUsedContinents()
	{
		objMapVerification = new MapVerification(objMapService.getObject().countryMap, objMapService.getObject().continentMap);
		objMapVerification.ContinentUnusedCheck();
		assertEquals(0, objMapVerification.ErrorList.size());
	}
	
	@Test
	public void CountryInMultiContinentCheck()
	{
		objMapVerification = new MapVerification(objMapService.getObject().countryMap, objMapService.getObject().continentMap);
		objMapVerification.CountryInMultiContinentCheck();
		assertEquals(0, objMapVerification.ErrorList.size());
	}
	
	@Test
	public void NoContinentOrCountry()
	{
		objMapVerification = new MapVerification(objMapService.getObject().countryMap, objMapService.getObject().continentMap);
		objMapVerification.NoContinentOrCountry();
		assertEquals(0, objMapVerification.ErrorList.size());
	}

}

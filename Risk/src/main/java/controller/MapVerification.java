package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import model.Continent;
import model.Country;

/**
 * @author Pegah
 * Verification of map 
 */
public class MapVerification {
	HashMap<String,Country> map1;
	HashMap<String,Continent>map2;
	public static boolean error = false;
	
	
	
	// constructor
	
	public MapVerification(HashMap<String, Country> map1, HashMap<String, Continent> map2) {
		super();
		this.map1 = map1;
		this.map2 = map2;
	}




	ArrayList<String> correctionlist = new ArrayList<>();	
	ArrayList<String> ErrorList = new ArrayList<>();

	/**
	 * Checking map
	 */

	public void TwoWayCheck() {
		for (int i=0;i<map1.size();i++) {
			Country country = map1.get(map1.keySet().toArray()[i]);
			System.out.println(country.getName());
			for (int j=0;j<country.getNeighbors().size();j++) {
				if(!country.getNeighbors().get(j).getNeighbors().contains(country)) {
					country.getNeighbors().get(j).getNeighbors().add(country);
					correctionlist.add("two way error: coorected");
				}
				
			}
		}
		
	}
	
	

	/**
	 * Checking connectivity between countries.
	 * @param country name of country
	 *  @param queue que of the countries
	
	 */
	
	
	public void graphConectivityCheck(Country country, Set<Country> queue, Continent continent) {
		for(int i=0;i<country.getNeighbors().size();i++) {
			Country neighborCountries = country.getNeighbors().get(i);
			if(continent == null && !queue.contains(neighborCountries)) {
				queue.add(neighborCountries);
				graphConectivityCheck(neighborCountries, queue, continent);
			}else if (!queue.contains(neighborCountries)) {
				if(neighborCountries.getContinent().getName()==continent.getName() 
						&& neighborCountries.getNeighbors().size()!=0) {
					queue.add(neighborCountries);
					graphConectivityCheck(neighborCountries, queue, continent);
				}
			}
		}
	}
	 
	

	/**
	 * 	checking if there is no neighbor and if country has itself as neighbor
	
	 */
	
	
	public void Neighborcheck() {
		int n = map1.size();
		for (int i =0;i<n;i++) {
			Country country =map1.get(map1.keySet().toArray()[i]);
			if(country.getNeighbors().size()<=0) {
				ErrorList.add("There is no neighbors for:"+country.getName());
			}
			if (country.getNeighbors().contains(country)) {
				country.getNeighbors().remove(country);
				correctionlist.add("self being neighbor error is removed");
			}
		}
		
	}
	

	/**
	 * 	checking country In MultiContinent
	
	 */
	
	
	public void CountryInMultiContinentCheck() {
		int n = map2.size();
		for(int i =0; i<n;i++) {
			Set set= map2.keySet();
			Continent continent1 = map2.get(set.toArray()[i]);
			for(int j =0; j<n; j++) {
				Continent continent2  = map2.get(set.toArray()[j]);
				if (!continent1.equals(continent2)||i!=j) {
					if (continent1.getCountries().containsAll(continent2.getCountries())) {
						ErrorList.add("Same Country Found In MulTiple Continent");
					}
				}
			}
		}
	}
	

	
	
	
	public void NoContinentOrCountry() {
		if (map1.size() < 1 || map2.size() < 1) {
			ErrorList.add("NO COUNTRY OR CONTINENT");
		}

	}
	
	/**
	 * 	checking unused continent
	
	 */
	
	public void ContinentUnusedCheck() {
		
//		creating a list of continent to check
		ArrayList<Continent> templist= new ArrayList<>(map2.values());
		for (int i =0; i<map1.size();i++) {
			Country country = map1.get(map1.keySet().toArray()[i]);
			for (int j =0;j<country.getNeighbors().size();j++) {
				if (map2.containsValue(country.getContinent())) {
					if(templist.contains(country.getContinent())) {
						templist.remove(country.getContinent());
					}
				}else {
					ErrorList.add("continent not found");
					
				}
			}
		}
		if(templist.size()>0) {
			ErrorList.add("Some continent is without country");
		}
	}
	
//method to call all check methods from UI
	public void validateMethod() {
//		TwoWayCheck();
		Neighborcheck();
		ContinentUnusedCheck();
		CountryInMultiContinentCheck();
		NoContinentOrCountry();
		
		if(ErrorList.size()>0) {
			System.out.println(ErrorList);
//		UI part:	
		}
	}

}

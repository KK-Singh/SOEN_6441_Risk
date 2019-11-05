package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controller.MapVerification;
import model.Continent;
import model.Country;
/**
* This MapService file can contains the core functions
* for the risk game.
* @author  Yash
* @version 1.2
*/
public class MapService {

	public Map<Integer, Continent> continentMap = new HashMap<Integer, Continent>();
	public Map<Integer, Country> countryMap = new HashMap<Integer, Country>();
	private static MapService obj;
	
	
    /** MapService constructor */
	private MapService() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	* This method is used to get the MapService object
	* if object is not yet created then it create object first
	* and then return the MapService object
	* @return MapService object of MapService
	*/
	public static MapService getObject() {
		if (obj == null) {
			obj = new MapService();

		}
		return obj;
	}

	
	/**
	* This method is used to read the map file
	* and add continents, countries and neighbors of the countries
	* @param filePath path of the file that going to be read
	*/
	public void readFile(String filePath) throws IOException {

		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String input;
		int cont_id = 0;
		int country_id = 0;
		while ((input = br.readLine()) != null) {

			if (input.contains("Continents")) {
				while ((input = br.readLine()) != "") {
					if (input.trim().isEmpty())
						break;
					String[] cont = input.split("=");

					Continent c = new Continent(Integer.valueOf(cont[1]), cont[0].replaceAll("\\s", "").toLowerCase());
					continentMap.put(cont_id, c);
					cont_id++;
				}
			}

			if (input.contains("Territories")) {
				while ((input = br.readLine()) != null) {
					if (input.trim().isEmpty())
						continue;
					String[] countryIn = input.split(",");
					String continentName = countryIn[3];
					Country country = getCountry(countryIn[0].replaceAll("\\s", "").toLowerCase());
					if (country == null) {
						country = new Country(countryIn[0].replaceAll("\\s", "").toLowerCase());
						countryMap.put(country_id, country);
						country_id++;
					}

					Continent countryContinent = getContinent(continentName.replaceAll("\\s", "").toLowerCase());
					countryContinent.addCountry(country);

					List<Country> nearByCountries = new ArrayList<Country>();
					for (int i = 4; i < countryIn.length; i++) {
						Country c = getCountry(countryIn[i].replaceAll("\\s", "").toLowerCase());
						if (c == null) {
							c = new Country(countryIn[i].replaceAll("\\s", "").toLowerCase());
							countryMap.put(country_id, c);
							country_id++;
						}
						nearByCountries.add(c);
					}
					country.setNeighbors(nearByCountries);
					country.setContinent(countryContinent);
				}
			}

		}
		br.close();
	}

	
	/**
	* This method is used to save the map file
	* @param filePath path of the file that going to be read
	* @exception IOException if stream to file cannot be written to or closed. 
	*/
	public void saveFile(String filePath) throws Exception {

		File file = new File(filePath);
		PrintWriter pw = new PrintWriter(file);
		pw.println("[Continents]");
		continentMap.forEach((k, v) -> {
			pw.println(v.getName() + "=" + v.getArmyValue());
		});

		pw.println();
		pw.println("[Territories]");

		Iterator<Integer> ite = countryMap.keySet().iterator();
		String prevCont = null;
		int fKey = ite.next();
		prevCont = countryMap.get(fKey).getName();
		ite = countryMap.keySet().iterator();
		while (ite.hasNext()) {
			int key = ite.next();
			Country cont = countryMap.get(key);
			String curCont = cont.getContinent().getName();
			pw.print(cont.getName() + "," +"0,0,"+ cont.getContinent().getName() + ",");
			for (int i = 0; i < cont.getNeighbors().size(); i++) {
				pw.print(cont.getNeighbors().get(i).getName());
				if (i < cont.getNeighbors().size() - 1)
					pw.print(",");
			}
			pw.println();
			if (prevCont != curCont) {
				pw.println();
			}
			prevCont = curCont;
		}
		// countryMap.forEach((k, v) -> {
		//
		// pw.print(v.getName() + "," + v.getContinent().getName() + ",");
		// for (int i = 0; i < v.getNeighbors().size(); i++) {
		// pw.print(v.getNeighbors().get(i).getName());
		// if (i < v.getNeighbors().size() - 1)
		// pw.print(",");
		// }
		// pw.println();
		// });
		pw.println();
		pw.close();
	}

	// adding continent
	/**
	* This method is used to add continent in continent map object
	* @param continentname name of the continent
	* @param continentvalue continent value
	* @return String it returns statement of operation
	*/
	public String addContinent(String continentname, int continentvalue) {
		try {
			Continent continent = new Continent(continentvalue, continentname);
			int id = continentMap.size() + 1;
			continentMap.put(id, continent);
		} catch (Exception e) {
			e.printStackTrace();
			return continentname + " Cannot be added";
		}

		return continentname + " Added Successfully";
	}

	
	/**
	* This method is used to remove continent in continent map object
	* @param continentname name of the continent
	* @return String it returns statement of operation
	*/
	public String removeContinent(String continentname) {
		try {

			Iterator<Integer> ite = continentMap.keySet().iterator();
			List<Country> continentCountries = new ArrayList<Country>();
			while (ite.hasNext()) {
				int key = ite.next();
				if (continentMap.get(key).getName().equalsIgnoreCase(continentname)) {
					continentCountries = continentMap.get(key).getCountries();
					ite.remove();

					break;
				}
			}

			ite = countryMap.keySet().iterator();

			while (ite.hasNext()) {
				int key = ite.next();
				if (continentCountries.contains(countryMap.get(key))) {
					Country c = countryMap.get(key);
					List<Country> neighbours = c.getNeighbors();
					neighbours.forEach(cont -> {
						cont.getNeighbors().remove(c);
					});
					ite.remove();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return continentname + " Cannot be removed";
		}

		return continentname + " removed successfully";

	}
	
	
	/**
	* This method is used to add country in country map object
	* and set the continent for the country
	* @param countryName name of the country
	* @param continentName name of the continent
	* @return String it returns statement of operation
	*/
	public String addCountry(String countryName, String continentName) {
		// Continent continent = new Continent(continentvalue, continentname);
		try {
			Continent cont = getContinent(continentName);
			Country country = new Country(countryName);
			country.setContinent(cont);
			List<Country> countries = cont.getCountries();
			countries.add(country);
			cont.setCountries(countries);
			int id = countryMap.size() + 1;
			countryMap.put(id, country);
		} catch (Exception e) {
			e.printStackTrace();
			return countryName + " Cannot be added";
		}
		return countryName + " Added Successfully";
		// continentMap.put(id, continent);
	}
	
	
	/**
	* This method is used to remove country in country map object
	* @param countryName name of the country
	* @return String it returns statement of operation
	*/
	public String removeCountry(String countryName) {
		try {
			Iterator<Integer> ite = countryMap.keySet().iterator();
			Country countryToRemove = null;
			Continent continent;
			while (ite.hasNext()) {
				int key = ite.next();
				if (countryMap.get(key).getName().equalsIgnoreCase(countryName)) {
					countryToRemove = countryMap.get(key);
					ite.remove();
					break;
				}
			}
			continent = countryToRemove.getContinent();
			continent.getCountries().remove(countryToRemove);
			List<Country> neighbouringCountries = countryToRemove.getNeighbors();
			Country finalCountryToRemove = countryToRemove;
			neighbouringCountries.forEach(cont -> {
				cont.getNeighbors().remove(finalCountryToRemove);
			});

		} catch (Exception e) {
			e.printStackTrace();
			return countryName + " cannot be removed";
		}

		return countryName + " removed successfully";
	}


	/**
	* This method is used to add neighbor from country to another country
	* @param countryname name of the country
	* @param neighbourcountryname name of the neighbor country
	* @return String it returns neighbor country name and result of operation
	*/
	public String addNeighbour(String countryname, String neighbourcountryname) {
		try {
			Country country1 = getCountry(countryname);
			Country country2 = getCountry(neighbourcountryname);

			List<Country> neighbourList1 = country1.getNeighbors();
			List<Country> neighbourList2 = country2.getNeighbors();

			if (!neighbourList2.contains(country1)) {
				neighbourList2.add(country1);
			}

			if (!neighbourList1.contains(country2)) {
				neighbourList1.add(country2);

			}
		} catch (Exception e) {

			e.printStackTrace();
			return neighbourcountryname + " cannot be added";
		}
		return neighbourcountryname + " added successfully";

	}
	
	
	/**
	* This method is used to remove neighbor from country to another country
	* @param countryname name of the country
	* @param neighbourcountryname name of the neighbor country
	* @return String it returns neighbor country name and result of operation
	*/
	public String removeNeighbour(String countryname, String neighbourcountryname) {
		try {
			Country country1 = getCountry(countryname);
			Country country2 = getCountry(neighbourcountryname);
			List<Country> neighbourList1 = country1.getNeighbors();
			List<Country> neighbourList2 = country2.getNeighbors();

			if (neighbourList1.contains(country2)) {
				neighbourList1.remove(country2);

			}

			if (neighbourList2.contains(country1)) {
				neighbourList2.remove(country1);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return neighbourcountryname + " cannot be removed";
		}

		return neighbourcountryname + " removed successfully";
	}

	/**
	* This method is used to show the map on the console.
	*/
	public void showMap() {
		continentMap.forEach((k, v) -> {
			System.out.println("Continent is " + v);
			v.getCountries().forEach(cont -> {
				System.out.println("Countries in the continent are " + cont);
				cont.getNeighbors().forEach(temp -> System.out.println("Neighbouring Countries " + temp));
			});
		});
	}
	
	
	/**
	* This method is used to get country object
	* @param name name of the country
	* @return Country it returns country object if exists
	*/
	public Country getCountry(String name) {

		Iterator<Integer> ite = countryMap.keySet().iterator();

		while (ite.hasNext()) {
			int key = ite.next();
			if (countryMap.get(key).getName().equalsIgnoreCase(name)) {
				return countryMap.get(key);
			}
		}
		return null;

	}
	
	
	/**
	* This method is used to get continent object
	* @param name name of the continent
	* @return Continent it returns continent object if exists
	*/
	public Continent getContinent(String name) {
		Iterator<Integer> ite = continentMap.keySet().iterator();

		while (ite.hasNext()) {
			int key = ite.next();
			if (continentMap.get(key).getName().equalsIgnoreCase(name)) {
				return continentMap.get(key);
			}
		}
		return null;
	}

	
	/**
	* This method is used to validate the map
	* @return boolean it returns true if map is valid else false
	*/
	public boolean mapValidate() {
		MapVerification mapVerification = new MapVerification(countryMap, continentMap);
		if (!mapVerification.validateMethod()) {
			System.out.println("INVALID MAP");
			return false;
		}
		return true;
	}

	
	/**
	* This is java main method
	* @param args arguments for main method
	*/
	public static void main(String[] args) {

		MapService service = new MapService();
		try {
			service.readFile("C:\\Users\\Shivam\\Downloads\\RISK-1.0\\Resources\\Asia.map");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try {
		// service.saveFile(
		// "J:\\AAA-concordia course study\\SOEN 6441\\project
		// builds\\RISK-1.0\\RISK-1.0\\Resources\\Asia_temp.map");
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// continentMap.forEach((k,v)->{
		// System.out.println("Continent is "+v );
		// v.getCountries().forEach(cont -> System.out.println("Countries in the
		// continent are "+ cont));
		// });
		// String A= "NewCountry";
		// Country AAA= new Country(A);
		// service.addCountry(A, "Oceania");

		service.addContinent("NewContinent", 100);
		service.addCountry("NewCountry", "NewContinent");
		service.addCountry("NewCountry2", "NewContinent");
		service.addNeighbour("NewCountry", "NewCountry2");
		service.addNeighbour("Pakistan", "NewCountry");
		service.addNeighbour("Philippines", "NewCountry2");

		System.out.println("BEFORE------------------------------------");
		// continentMap.forEach((k, v) -> {
		// System.out.println("Continent is " + v);
		// v.getCountries().forEach(cont -> {
		// System.out.println("Countries in the continent are " + cont);
		// cont.getNeighbors().forEach(temp -> System.out.println("Neighbouring
		// Countries " + temp));
		// });
		// });

		// service.removeContinent("NewContinent");
		// service.removeCountry("NewCountry");
		// service.removeNeighbour("Pakistan", "NewCountry");
		// System.out.println("AFTER------------------------------------");
		//
		// continentMap.forEach((k, v) -> {
		// System.out.println("Continent is " + v);
		// v.getCountries().forEach(cont -> {
		// System.out.println("Countries in the continent are " + cont);
		// if(cont.getName().equalsIgnoreCase("Pakistan")) {
		// cont.getNeighbors().forEach(temp->System.out.println("Neighbouring Countries
		// "+ temp));
		// }
		// });
		// });

	}

}

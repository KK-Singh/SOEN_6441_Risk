package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * @author Tapan
 */

public class ConquestMapFile implements Serializable{

	/**
	 * This method is used to read the map file and add continents, countries and
	 * neighbors of the countries
	 * 
	 * @param filePath
	 *            path of the file that going to be read
	 * @param continentMap
	 *               Map of the continent 
	 *  @param countryMap
	 *               Map of the country       
	 * @exception IOException
	 *                if data from file cannot be readable to or closed.
	 */
	public void readFile(String filePath, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap)
			throws IOException {

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

					// String[] cont = input.split(" ");

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

					// String[] countryIn = input.split(" ");

					String continentName = countryIn[3];
					Country country = getCountry(countryIn[0].replaceAll("\\s", "").toLowerCase(),countryMap);
					if (country == null) {
						country = new Country(countryIn[0].replaceAll("\\s", "").toLowerCase());
						countryMap.put(country_id, country);
						country_id++;
					}

					Continent countryContinent = getContinent(continentName.replaceAll("\\s", "").toLowerCase(),continentMap);
					countryContinent.addCountry(country);

					List<Country> nearByCountries = new ArrayList<Country>();
					for (int i = 4; i < countryIn.length; i++) {
						Country c = getCountry(countryIn[i].replaceAll("\\s", "").toLowerCase(),countryMap);
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
	* This method is used to get country object
	* @param name name of the country
	* @param countryMap Map of the country 
	* @return Country it returns country object if exists
	*/
	public Country getCountry(String name, Map<Integer, Country> countryMap) {

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
	public Continent getContinent(String name, Map<Integer, Continent> continentMap) {
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
	 * This method is used to save the map file
	 * @param continentMap
	 *               Map of the continent
	 * @param filePath
	 *            path of the file that going to be read
	 * @exception IOException
	 *                if stream to file cannot be written to or closed.
	 */
	public void saveFile(String filePath, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap)
			throws Exception {

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
			pw.print(cont.getName() + "," + "0,0," + cont.getContinent().getName() + ",");
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


}
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DominationMapFile {

	public void readFile(String filePath, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap)
			throws IOException {

		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String input;
		int cont_id = 1;
		// int country_id = 0;
		while ((input = br.readLine()) != null) {
			// System.out.println(input);
			if (input.contains("continents")) {

				while ((input = br.readLine()) != "") {

					if (input.trim().isEmpty())
						break;

					String[] cont = input.split(" ");

					Continent c = new Continent(Integer.valueOf(cont[1]), cont[0].replaceAll("\\s", "").toLowerCase());
					continentMap.put(cont_id, c);
					cont_id++;
				}
			}

			if (input.contains("countries")) {
				while ((input = br.readLine()) != "") {
					if (input.trim().isEmpty())
						break;

					String[] countryIn = input.split(" ");
					int country_id = Integer.valueOf(countryIn[0]);
					Continent continent = continentMap.get(Integer.valueOf(countryIn[2]));
					Country country = new Country(countryIn[1].replaceAll("\\s", "").toLowerCase());
					country.setContinent(continent);
					countryMap.put(country_id, country);

				}
			}
			if (input.contains("borders")) {
				while ((input = br.readLine()) != null) {
					if (input.trim().isEmpty())
						continue;

					String[] borderslist = input.split(" ");
					List<Country> nearByCountries = new ArrayList<Country>();

					Country indexcountry = countryMap.get(Integer.valueOf(borderslist[0]));
					for (int i = 1; i < borderslist.length; i++) {

						Country c = countryMap.get(Integer.valueOf(borderslist[i]));
						nearByCountries.add(c);

					}
					indexcountry.setNeighbors(nearByCountries);

				}

			}
		}

		br.close();
	}

	public void saveFile(String filePath, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap)
			throws IOException {

		File file = new File(filePath);
		PrintWriter pw = new PrintWriter(file);
		pw.println("[Continents]");
		continentMap.forEach((k, v) -> {
			pw.println(v.getName() + " " + v.getArmyValue());
		});

		pw.println();
		pw.println("[Territories]");
		Iterator<Integer> ite = countryMap.keySet().iterator();

		while (ite.hasNext()) {
			int key = ite.next();
			Country curcountry = countryMap.get(key);
			String curcountryname = curcountry.getName();
			int conti_id = curcountry.getCountry_id();
			pw.print(key + " " + curcountryname + " " + conti_id);

		}

		pw.println();
		pw.println("[borders]");

		ite = countryMap.keySet().iterator();
		while (ite.hasNext()) {
			int key = ite.next();
			Country indexcountry = countryMap.get(key);
			pw.print(key);
			for (int i = 0; i < indexcountry.getNeighbors().size(); i++) {
				pw.print(" " + indexcountry.getNeighbors().get(i).getCountry_id());
			}
			pw.println();

		}

	}
}

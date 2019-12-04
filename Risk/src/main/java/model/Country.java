package model;

import java.io.Serializable;
import java.util.*;

/**
 * @author Gurwinder Kaur
 */

public class Country implements Serializable {
/**
 * country_id : Represents continents ID
 */
	private int country_id;
	/**
	 *name : Represents country name
	 */
	private String name;
	/**
	 * armyCount : Represents army count
	 */
	private int armyCount;
	/**
	 * neighbors : Represents neighbors of a country
	 */
	private List<Country> neighbors;
	/**
	 * countryOwner : Represents owner of a country
	 */
	private Player countryOwner;
	/**
	 * continent : Represents continent
	 */
	private Continent continent;

	/**
	 * constructor for initialization of country
	 * 
	 * @param name : name of the country
	 */
	public Country(String name) {
		this.armyCount = 0;
		this.name = name;
		this.neighbors = new ArrayList<>();
	}

	/**
	 * This method gets the country id
	 * 
	 * @return country_id : gets the country id
	 */
	public int getCountry_id() {
		return country_id;
	}

	/**
	 * This method sets the Country id
	 * 
	 * @param country_id : sets the country id
	 */
	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}

	/**
	 * This method gets the country name
	 * 
	 * @return name : name of a country
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName() to set name
	 * 
	 * @param name : sets the country name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getCountryOwner() to return CountryOwner
	 * 
	 * @return countryOwner : Owner of a country
	 */
	public Player getCountryOwner() {
		return countryOwner;
	}

	/**
	 * This method sets the country owner
	 * 
	 * @param owner_player : sets the country owner
	 */

	public void setCountryOwner(Player owner_player) {
		this.countryOwner = owner_player;
	}

	/**
	 * This method get the continent
	 * 
	 * @return continent : return the continent
	 */

	public Continent getContinent() {
		return continent;
	}

	/**
	 * This method set the continent
	 * 
	 * @param continent : sets the continent
	 */

	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	/**
	 * This method get the list of neighbors
	 * 
	 * @return neighbors : neighbor of a country
	 */

	public List<Country> getNeighbors() {
		return neighbors;
	}

	/**
	 * This method sets neighbors of a country
	 * 
	 * @param neighbors: name of neighbor of a country
	 */
	public void setNeighbors(List<Country> neighbors) {
		this.neighbors = neighbors;
	}

	/**
	 * This method gets the army count of the country
	 * 
	 * @return armyCount : counts no. of armies placed
	 */

	public int getArmyCount() {
		return armyCount;
	}

	/**
	 * This method sets the army count of the country.
	 * 
	 * @param no_of_armies : no of armies to be placed on the country.
	 */

	public void setArmyCount(int no_of_armies) {
		this.armyCount = no_of_armies;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Country Name " + this.name;
	}

}

package model;
import java.util.*;

/**
 * This entity class represent a Continent in game.
 * 
 * @author Team
 */

public class Continent {
	
	/**
	 * Represents continents ID.
	 */
	private int continent_id;
	
	
	/**
	 * continents name.
	 */
	private String name;
	
	/**
	 * List of countries.
	 */
	private List<Country> countries;
	
	/**
	 * Continents control value.
	 */
	private int control_value;
	
	/**
	 * Creates a continent with a given name and Army Value.
	 * 
	 * @param name:
	 *            Name for the new continent
	 *
	 * @param continent Army Value (control value):
	 *            Control value for this continent
	 */
	
	public Continent(int army_value, String name) {
		this.name=name;
		this.control_value =army_value;
		this.countries= new ArrayList<>();
		
	}
	
	
//	get and set methods
	

	public void setName(String name) {
		this.name=name;
	}
	
	//Gets Continent name
	public String getName() {
		return name;
	}
		
	
	//Set list of countries to the continent
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
		
	//Gets countries list of continent
	public List<Country> getCountries() {
		return countries;
	}

	public int getArmyValue() {
		return control_value;
	}
		

	public void setArmyValue(int army_value) {
		this.control_value=army_value;
	}
	
	
	// method to add country to the list.
	public void addCountry(Country country) {
		this.countries.add(country);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Continent Name "+ this.name;
	}
}

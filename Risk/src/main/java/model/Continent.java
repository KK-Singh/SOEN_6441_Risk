package model;
import java.io.Serializable;
import java.util.*;

/**
 * This entity class represent a Continent in game. 
 * @author Gurwinder Kaur
 */

public class Continent implements Serializable {
	
	/**
	 * Represents continents ID.
	 */
	private int continent_id;
	
	/**
	 * Represents continents name.
	 */
	private String name;
	
	/**
	 * Represents List of countries.
	 */
	private List<Country> countries;
	
	/**
	 * Represents Continents control value.
	 */
	private int control_value;
	
	/**
	 * This method creates a continent with a given name and Army Value.
	 * 
	 * @param name:Name for the new continent
	 * @param continent Army Value (control value): Control value for this continent
	 */
	
	public Continent(int army_value, String name) {
		this.name=name;
		this.control_value =army_value;
		this.countries= new ArrayList<>();
		
	}
	
	
/**
 * This method set the value of name
 * @param name : continent name
 */
	

	public void setName(String name) {
		this.name=name;
	}
	
	/**
	 * This method gets the Continent name
	 * @return name : continent name ;
	 */
	public String getName() {
		return name;
	}		
	
	/**
	 * This method  set list of countries to the continent
	 * @param countries : countries of type List<Country> countries
	 */
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
		
	/**
	 * This method gets the list of continent
	 * @return countries : List of countries
	 */
	public List<Country> getCountries() {
		return countries;
	}
/**
 * This method get army control value
 * @return control_vale : control value of the army
 */
	public int getArmyValue() {
		return control_value;
	}
		
/**
 * This method set the  army value
 * @param control_value : army value
 */
	public void setArmyValue(int army_value) 
	{
		this.control_value=army_value;
	}
	
	/**
	 * This method add country to the list
	 * @param country: list of countries
	 */
	public void addCountry(Country country) {
		this.countries.add(country);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Continent Name "+ this.name;
	}
}

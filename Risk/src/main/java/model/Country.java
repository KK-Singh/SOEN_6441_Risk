package model;
import java.util.*;



public class Country {
		
	/**
	 * unique id of the country
	 */
	private int country_id;
	private String name;
	private int armyCount;
	
	private List<Country> neighbors;
	private Player countryOwner;
	private Continent continent;
	
	
	
	/**
	 * constructor for initialization of country
	 * 
	 * @param name : name of the country, type String
	 */
	public Country(String name) {
	
		this.armyCount = 0;
		this.name = name;
		this.neighbors = new ArrayList<>();
	}


//	get and set methods:
	
	
	public int getCountry_id() {
		return country_id;
	}


	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Player getCountryOwner() {
		return countryOwner;
	}


	public void setCountryOwner(Player owner_player) {
		this.countryOwner = owner_player;
	}


	public Continent getContinent() {
		return continent;
	}


	public void setContinent(Continent continent) {
		this.continent = continent;
	}


	public List<Country> getNeighbors() {
		return neighbors;
	}


	public void setNeighbors(List<Country> neighbors) {
		this.neighbors = neighbors;
	}


	public int getArmyCount() {
		return armyCount;
	}


	public void setArmyCount(int no_of_armies) {
		this.armyCount = no_of_armies;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Country Name "+ this.name;
	}

}

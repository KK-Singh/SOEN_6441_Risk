package model;
import java.util.*;



public class Country {
		
	/**
	 * unique id of the country
	 */
	private int country_id;
	private String name;
	private int no_of_armies;
	
	private List<Country> neighbors;
	private Player owner_player;
	private Continent continent;
	
	
	
	/**
	 * constructor for initialization of country
	 * 
	 * @param name : name of the country, type String
	 */
	public Country(String name) {
	
		this.no_of_armies = 0;
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


	public Player getOwner_player() {
		return owner_player;
	}


	public void setOwner_player(Player owner_player) {
		this.owner_player = owner_player;
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


	public int getNo_of_armies() {
		return no_of_armies;
	}


	public void setNo_of_armies(int no_of_armies) {
		this.no_of_armies = no_of_armies;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Country Name "+ this.name;
	}

}

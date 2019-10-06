package model;
import java.util.*;



public class Country {
		
	/**
	 * unique id of the country
	 */
	private int country_id;
	private String name;
	private int continent_id;
	private List<Country> neighbors;
	private Player owner_player;
	private Continent continent;
	private int no_of_armies;
	
	
	/**
	 * constructor for initialization of country
	 * 
	 * @param name : name of the country, type String
	 */
	public Country(String name) {
	
		this.no_of_armies = 0;
		this.name = name;
		this.owner_player = new Player(0);
		this.neighbors = new ArrayList<>();
	}


//	get and set methods:
	
	
	public int getCountry_id() {
		return country_id;
	}


	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}


	public String getCountryName() {
		return name;
	}


	public void setCountryName(String name) {
		this.name = name;
	}


	public int getContinent_id() {
		return continent_id;
	}


	public void setContinent_id(int continent_id) {
		this.continent_id = continent_id;
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





}

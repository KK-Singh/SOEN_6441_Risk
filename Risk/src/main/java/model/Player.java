package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
	
	private String name;
	private int player_id;
	private List<Card> cards;
	private int army;
	private int army_left;
	private List<Country> occupied_countries;
	private List<Country> alloccupied_countries;
	private List<Continent>occupied_continents;
	
	
	//Default Constructor
	public Player(String name) {
		this.name = name;
		this.alloccupied_countries = new ArrayList<>();
		this.occupied_countries = new ArrayList<>();
		this.cards = new ArrayList<>();
	}




// get and set methods

	public String getPlayerName() {
		return name;
	}






	public void setPlayerName(String name) {
		this.name = name;
	}






	public int getPlayer_id() {
		return player_id;
	}






	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}


	public List<Card> getCards() {
		return cards;
	}






	public void setCards(List<Card> cards) {
		this.cards = cards;
	}






	public int getArmy() {
		return army;
	}






	public void setArmy(int army) {
		this.army = army;
	}






	public int getArmy_left() {
		return army_left;
	}






	public void setArmy_left(int army_left) {
		this.army_left = army_left;
	}






	public List<Country> getOccupied_countries() {
		return occupied_countries;
	}






	public void setOccupied_countries(List<Country> occupied_countries) {
		this.occupied_countries = occupied_countries;
	}






	public List<Country> getAlloccupied_countries() {
		return alloccupied_countries;
	}






	public void setAlloccupied_countries(List<Country> alloccupied_countries) {
		this.alloccupied_countries = alloccupied_countries;
	}




	public void addOccupiedCountry(Country occopiedcountry) {
		this.alloccupied_countries.add(occopiedcountry);
	}

	public List<Continent> getOccupied_continents() {
		return occupied_continents;
	}






	public void setOccupied_continents(List<Continent> occupied_continents) {
		this.occupied_continents = occupied_continents;
	}
	
	
	
	
	
	
	
	
	

}

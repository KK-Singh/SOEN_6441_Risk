package model;

public class Card {

	String typeOfCard;
	
	Country cardCountry;
	
	public Card(String typeOfCard, Country cardCountry) {
		this.typeOfCard = typeOfCard;
		this.cardCountry = cardCountry;
	}

	public String getTypeOfCard() {
		return typeOfCard;
	}

	public void setTypeOfCard(String typeOfCard) {
		this.typeOfCard = typeOfCard;
	}

	public Country getCardCountry() {
		return cardCountry;
	}

	public void setCardCountry(Country cardCountry) {
		this.cardCountry = cardCountry;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Card Type "+ this.typeOfCard + "Card Country "+ this.cardCountry.getName();
	}
	
}

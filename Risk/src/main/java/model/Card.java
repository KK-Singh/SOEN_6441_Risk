package model;

import java.io.Serializable;

/**
 @author Gurwinder Kaur 
 */
public class Card implements Serializable
{
	/**
	 * typeOfCard : Represents type of card
	
	 */
	String typeOfCard;	
	/**
	 * cardCountry : Represents a country card
	 */
	Country cardCountry;
	/**
	 * constructor for initialization of card
	 * @param typeOfCard : type of card, cardCountry:country card
	 */
		public Card(String typeOfCard, Country cardCountry) {
		this.typeOfCard = typeOfCard;
		this.cardCountry = cardCountry;
	}		
	/**
	 * This method get the type of card 
	 * @return typeOfCard : gets the type of card 
	 */
	public String getTypeOfCard() {
		return typeOfCard;
	}
	/**
	 * This method to set value for typeOfCard
	 * @param typeOfCard : set the type of card 
	 */
	public void setTypeOfCard(String typeOfCard) {
		this.typeOfCard = typeOfCard;
	}
    /**
     * this method get the country card
     * @return cardCountry : get the country card
     */
	public Country getCardCountry() {
		return cardCountry;
	}
	/**
	 * This method set the Country card
	 * @param cardCountry : set the Country card
	 */
	public void setCardCountry(Country cardCountry) {
		this.cardCountry = cardCountry;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Card Type "+ this.typeOfCard + "Card Country "+ this.cardCountry.getName();
	}
	
}

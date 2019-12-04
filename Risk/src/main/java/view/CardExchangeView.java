package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import model.Card;
import model.CardViewModel;
import model.Country;
import model.Player;
/**
* This PhaseView file can contains the observer method
* of the risk game.
* @author  Yash
* @version 1.2
*/
public class CardExchangeView implements Observer,Serializable {
	
	/** Player current player object*/
	private Player curPlayer;

	
	/** CardViewModel cardview model object*/
	CardViewModel cardViewModel;
	
	/** boolean flag for country ownership*/
	boolean ifCardIsOfOwnedCountry = false;

	/** Country card owned country object*/
	Country cardOwnedCountry;


	/**
	* This method is used to update the observers of the cardviewmodel
	* @param o cardviewmodel observable object
	* @param arg cardviewmodel observable object
	*/
	@Override
	public void update(Observable o, Object arg) {
		cardViewModel = (CardViewModel) o;

		curPlayer = cardViewModel.getCurPlayer();
		List<Card> playerCards = new LinkedList<>(cardViewModel.getCardOfCurrentPlayer(curPlayer));
		int totalCards = playerCards.size();

		System.out.println("-------------------CARD EXCHANGE VIEW START------------------");

		System.out.println("Player " + curPlayer.getPlayerName() + " will exchnage the cards");
		System.out.println("Player " + curPlayer.getPlayerName() + " has " + totalCards + " to exchange");
		System.out.println("Type of cards player " + curPlayer.getPlayerName() + " has : ");
		playerCards.forEach(card -> {
			System.out.println(card);
		});

		exchangeCards();

		System.out.println("-------------------CARD EXCHANGE VIEW END------------------");
	}
	
	
	/**
	* This method is used to exchange the cards
	* and it print the response as per the operations on 
	* the console.
	*/
	private void exchangeCards() {
		BufferedReader br = StartGame.br;
		String input =null;
		System.out.println("Enter Command to exchange the cards");
		while (true) {
			try {
				input = br.readLine();
				if (validateCardExchangeCommand(input)) {
					if(input.split(" ")[1].equalsIgnoreCase("-none")) {
						System.out.println("No Cards Exchanged");
						return;
					}
					Map<String,Integer> typeOfCardMapping = formMappingFromInput(input);
					removePlayerCards(typeOfCardMapping);
					cardViewModel.setTotalExchanges(cardViewModel.getTotalExchange()+1);
					if(ifCardIsOfOwnedCountry) {
						cardViewModel.setArmyCountOfPlayer(curPlayer, 2, cardOwnedCountry);
					}else {
						cardViewModel.setArmyCountOfPlayer(curPlayer, 0, null);
					}
					System.out.println("Cards Exchanged");
					
					cardViewModel.setCurrentPlayerView(curPlayer);
					cardViewModel.setCardOwnedCountry(cardOwnedCountry);
					
				} else {
					System.out.println("Enter valid Card Exchange command");
				}
				
			} catch (IOException e) {
				System.out.println("Enter Valid Command");
//				e.printStackTrace();
			}
			System.out.println("Want to exchange more cards(y/n)");
			try {
				input = br.readLine();
				if(input.equalsIgnoreCase("n"))
					break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Enter Valid Command");
//				e.printStackTrace();
			}
			
		}

	}
	
	
	/**
	* This method is used to remove card from the player
	* @param typeOfCardMapping map object of mapping of cards
	*/
	private void removePlayerCards(Map<String,Integer> typeOfCardMapping) {
		Queue<Card> cardToRemove = new LinkedList<>();
		Iterator<Card> ite = cardViewModel.getCardOfCurrentPlayer(curPlayer).iterator();
		
		while(ite.hasNext()) {
			Card playerCard = ite.next();
			if(typeOfCardMapping.get(playerCard.getTypeOfCard())!=null &&  typeOfCardMapping.get(playerCard.getTypeOfCard())!=0) {
				ite.remove();
				cardToRemove.add(playerCard);
				if(curPlayer.getPlayerCountries().contains(playerCard.getCardCountry())) {
					ifCardIsOfOwnedCountry = true;
					cardOwnedCountry = playerCard.getCardCountry();
				}
			}
		}
		
		cardViewModel.getAllCards().addAll(cardToRemove);
	}
	
	
	/**
	* This method is used to form a map from the input string
	* @param inout input string
	* @return Map return the hasmap object
	*/
	private Map<String,Integer> formMappingFromInput(String input){
		String [] arr = input.split(" ");
		Map<String,Integer> mapping = new HashMap<String, Integer>();
		mapping.put(cardViewModel.INFANTARY_STRING, Integer.parseInt(arr[1]));
		mapping.put(cardViewModel.CAVALRY_STRING, Integer.parseInt(arr[2]));
		mapping.put(cardViewModel.ARTILLERY_STRING, Integer.parseInt(arr[3]));
		return mapping;
		
	}
	
	
	/**
	* This method is used to check if card exchange is possible or not
	* @param card1 value of card1
	* @param card2 value of card2
	* @param card3 value of card3
	* @return boolean true if exchange is possible else false
	*/
	private boolean ifExchangePossible(int card1, int card2, int card3) {
		if(card1 ==1 && card2==1 && card3==1) {
			return true;
		}else if(card1==3 || card2==3 || card3==3) {
			return true;
		}else {
			return false;
		}
		
	}
		
	
	/**
	* This method is used to validate card exchange command
	* @param input input card exchange command
	* @return boolean true if command is valid else false
	*/
	private boolean validateCardExchangeCommand(String input) {
		String[] arr = input.split(" ");
		if (!arr[0].equalsIgnoreCase("exchangecards")) {
			return false;
		}
		if(arr[1].equalsIgnoreCase("-none")) {
			return true;
		}
		try {
			int card1 = Integer.parseInt(arr[1]);
			int card2 = Integer.parseInt(arr[2]);
			int card3 = Integer.parseInt(arr[3]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}

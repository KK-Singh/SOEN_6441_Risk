package view;

import java.io.BufferedReader;
import java.io.IOException;
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

public class CardExchangeView implements Observer {

	private Player curPlayer;

	CardViewModel cardViewModel;

	boolean ifCardIsOfOwnedCountry = false;

	Country cardOwnedCountry;

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
	
	private Map<String,Integer> formMappingFromInput(String input){
		String [] arr = input.split(" ");
		Map<String,Integer> mapping = new HashMap<String, Integer>();
		mapping.put(cardViewModel.INFANTARY_STRING, Integer.parseInt(arr[1]));
		mapping.put(cardViewModel.CAVALRY_STRING, Integer.parseInt(arr[2]));
		mapping.put(cardViewModel.ARTILLERY_STRING, Integer.parseInt(arr[3]));
		return mapping;
		
	}
	
	private boolean ifExchangePossible(int card1, int card2, int card3) {
		if(card1 ==1 && card2==1 && card3==1) {
			return true;
		}else if(card1==3 || card2==3 || card3==3) {
			return true;
		}else {
			return false;
		}
		
	}
	
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

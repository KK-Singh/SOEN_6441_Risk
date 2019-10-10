package controller;
import java.util.*;
import model.Continent;
import model.Country;
import model.Player;
/**
 * This controller class has all the methods we need for the Reinforcement Phase
 * of the Risk game
 * 
 * @author Pegah
 */
public class ReinforcementController {
	/**
	 * this method checks for the number of armies that are not moved
	 * 
	 * @param country: country object
	 * @return String message
	 */
		public String increaseArmies(Country country) {
			Player player = country.getOwner_player();
			if (player.getArmy_left() == 0) {
				return "NO ARMIES LEFT IN THIS COUNTRY! PLEASE CHOOSE FINISH REINFORCEMENT";
			} else {
				updateNewValues(player, country);
				return "";
			}
		}
		/**
		 * this method gets a list of countries that the player owns
		 * 
		 * @param player: player object for comparing which player owned the country
		 * @return List of countries owned by the player
		 */
		public List<Country> getPlayersCountries(Player player) {
			List<Country> countries = new ArrayList<Country>();
			for (Map.Entry<String, Country> entry : ReadingFiles.CountryNameObject.entrySet()) {
				if (entry.getValue().getOwner_player().equals(player)) {
					countries.add(entry.getValue());
				}
			}
			return countries;
		}
		/**
		 * this method compute the number of armies each player gets for reinforcement
		 * 
		 * @param player: player object for which player's armies has been computed
		 */

		public void NumberOfReinforcementArmies(Player player) {
			int allPlayersCountries = player.getAlloccupied_countries().size();
			float allArmiesForReinforcement = (float) allPlayersCountries / 3;
			int armies = 0;
			if (allArmiesForReinforcement < 3.0) {
				armies = armies + 3;
			} else {
				armies = armies + (int) allArmiesForReinforcement;
			}
			armies = armies + numberOfArmiesByValueOfContinent(player);
			player.setArmy_left(armies);
		}
		/**
		 * this method checks that the player owns the entire continent or not
		 * 
		 * @param player: player object for which player has been checked
		 * @return List of continents
		 */

		public List<Continent> checkContinentConquered(Player player) {
			List<Continent> continents = new ArrayList<Continent>();
			for (Map.Entry<String, Continent> entry : ReadingFiles.ContinentNameObject.entrySet()) {
				List<Country> tempCountry = entry.getValue().getCountries();
				int counter = 0;
				for (int i = 0; i < entry.getValue().getCountries().size(); i++) {
					if (entry.getValue().getCountries().get(i).getOwner_player().equals(player))
						counter++;
				}
				if (tempCountry.size() == counter)
					continents.add(entry.getValue());
			}
			return continents;
		}
		/**
		 * this method compute the number of armies according to the continent value
		 * 
		 * @param player: player object 
		 * @return number of armies
		 */
		public int numberOfArmiesByValueOfContinent(Player player) {
			List<Continent> continents = checkContinentConquered(player);
			int armies = 0;
			for (int i = 0; i < continents.size(); i++) {
				armies = armies + continents.get(i).getControlValue();
			}
			return armies;
		}
		
		/**
		 * this method updates the number of armies player owns and number armies which is left in country
		 * 
		 * @param player: player object 
		 * @param country: country object
		 */
		public void updateNewValues(Player player, Country country) {
			country.setNo_of_armies(country.getNo_of_armies() + 1);
			player.setArmy_left(player.getArmy_left() - 1);
		}
		
		/**
		 * this method checks player has moved all his armies into position or not
		 * 
		 * @param player: player object
		 */

		public String checkReinforcementCompleted(Player player) {
			if (player.getArmy_left() == 0) {
				return "DEPLOYMENT HAS BEEN FINISHED!";
			} else
				return null;
		}
	}

}

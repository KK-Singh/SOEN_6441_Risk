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
		public String increaseArmies(Country country, Player player) {
			player = country.getOwner_player();
			if (player.getArmy_left() == 0) {
				return "NO ARMIES LEFT IN THIS COUNTRY! PLEASE CHOOSE FINISH REINFORCEMENT";
			} else {
				country.setNo_of_armies(country.getNo_of_armies() + 1);
				player.setArmy_left(player.getArmy_left() - 1);
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
			int armies = 0;
			List<Continent> continents = checkContinentConquered(player);
			int allPlayersCountries = player.getAlloccupied_countries().size();
			float allArmiesForReinforcement = (float) allPlayersCountries / 3;
			if (allArmiesForReinforcement < 3.0) {
				armies = armies + 3;
			} else {
				armies = armies + (int) allArmiesForReinforcement;
			}
			for (int i = 0; i < continents.size(); i++) {
				armies = armies + continents.get(i).getControlValue();
			}
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
				List<Country> playerCountries = getPlayersCountries(player);
				Collections.sort(tempCountry);
				Collections.sort(playerCountries);
				if(tempCountry.equals(playerCountries)) {
					continents.add(entry.getValue());
				}
				}
			return continents;
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

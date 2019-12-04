package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import ControllerHelper.MapControllerHelper;
import model.Continent;
import model.Country;

/**
 * MapController class implements the methods of MapService object
 * @author Pegah
 */
public class MapController implements Serializable {

	MapControllerHelper serviceObj = MapControllerHelper.getObject();
	
	/**
	 * This method is used for reading the files
	 * @param filePath : this is the path of file
	 * @return String
	 */
	public String readFile(String filePath) {
		try {
			serviceObj.readFile(filePath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return e.getMessage();
		}
		return "File Read Successfully";
	}
	/**
	 * This method is used for saving the files
	 * @param filePath : this is the path of file
	 * @return String
	 */
	public String saveFile(String filePath) {
		try {
			serviceObj.saveFile(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return e.getMessage();
		}
		return "Successfully Saved File";
	}
	/**
	 * This method is used for adding the continents
	 * @param continentname : this is continent name
	 * @param continentvalue : this is continent value
	 * @return MapService object
	 */
	public String addContinent(String continentname, int continentvalue) {
		return serviceObj.addContinent(continentname, continentvalue);
	}
	/**
	 * This method is used for removing the continents
	 * @param continentname : this is continent name
	 * @return MapService object
	 */
	public String removeContinent(String continentname) {
		return serviceObj.removeContinent(continentname);

	}
	/**
	 * This method is used for adding countries
	 * @param countryName : this is country name
	 * @param continentName : this is continent name
	 * @return MapService object
	 */
	
	public String addCountry(String countryName, String continentName) {
		return serviceObj.addCountry(countryName, continentName);
		
	}
	/**
	 * This method is used for removing the countries
	 * @param countryName : this is country name
	 * @return MapService object
	 */
	public String removeCountry(String countryName) {
		return serviceObj.removeCountry(countryName);

	}
	/**
	 * This method is used for adding neighbours
	 * @param countryname : this is country name
	 * @param neighbourcountryname : this is neighbours name of countries  
	 * @return MapService object
	 */
	public String addNeighbour(String countryname, String neighbourcountryname) {
		return serviceObj.addNeighbour(countryname, neighbourcountryname);

	}
	/**
	 * This method is used for removing the neighbours
	 * @param countryname : this is country name
	 * @param neighbourcountryname : this is neighbours name of countries
	 * @return  MapService object
	 */
	public String removeNeighbour(String countryname, String neighbourcountryname) {
		return serviceObj.removeNeighbour(countryname, neighbourcountryname);

	}
	/**
	 * This method is used for showing the map
	 */
	public void showMap() {
		serviceObj.showMap();
	}
	/**
	 * This method is used for validation of the map
	 * @return  MapService object
	 */
	public boolean validateMap() {
		return serviceObj.mapValidate();
	}
	/**
	 * This method is used for getting countries map
	 * @return countryMap
	 */
	public Map<Integer, Country> getCountryMap(){
		return this.serviceObj.countryMap;
	}
	/**
	 * This method is used for getting continents map
	 * @return continentMap
	 */
	public Map<Integer,Continent> getContinentMap(){
		return this.serviceObj.continentMap;
	}
}

package controller;

import java.io.IOException;
import java.util.Map;

import model.Continent;
import model.Country;
import service.MapService;

public class MapController {

	MapService serviceObj = MapService.getObject();
	
	
	public String readFile(String filePath) {
		try {
			serviceObj.readFile(filePath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		return "File Read Successfully";
	}

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

	public String addContinent(String continentname, int continentvalue) {
		serviceObj.addContinent(continentname, continentvalue);
		return continentname+ " Added Successfully";
	}
	
	public String removeContinent(String continentname) {
		serviceObj.removeContinent(continentname);
		return continentname + " Removed Successfully";
	}
	
	
	public String addCountry(String countryName, String continentName) {
		serviceObj.addCountry(countryName, continentName);
		return countryName + " Added Successfully";
	}
	
	public String removeCountry(String countryName) {
		serviceObj.removeCountry(countryName);
		return countryName + " Removed Successfully";
	}
	
	public String addNeighbour(String countryname, String neighbourcountryname) {
		serviceObj.addNeighbour(countryname, neighbourcountryname);
		return neighbourcountryname + " Added Successfully";
	}
	
	public String removeNeighbour(String countryname, String neighbourcountryname) {
		serviceObj.removeNeighbour(countryname, neighbourcountryname);
		return neighbourcountryname + " Removed Successfully";
	}
	
	public void showMap() {
		serviceObj.showMap();
	}
	
	public boolean validateMap() {
		return serviceObj.mapValidate();
	}
	
	public Map<Integer, Country> getCountryMap(){
		return this.serviceObj.countryMap;
	}
	
	public Map<Integer,Continent> getContinentMap(){
		return this.serviceObj.continentMap;
	}
}

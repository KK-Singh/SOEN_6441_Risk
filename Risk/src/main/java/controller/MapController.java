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
		return serviceObj.addContinent(continentname, continentvalue);
	}
	
	public String removeContinent(String continentname) {
		return serviceObj.removeContinent(continentname);

	}
	
	
	public String addCountry(String countryName, String continentName) {
		return serviceObj.addCountry(countryName, continentName);
		
	}
	
	public String removeCountry(String countryName) {
		return serviceObj.removeCountry(countryName);

	}
	
	public String addNeighbour(String countryname, String neighbourcountryname) {
		return serviceObj.addNeighbour(countryname, neighbourcountryname);

	}
	
	public String removeNeighbour(String countryname, String neighbourcountryname) {
		return serviceObj.removeNeighbour(countryname, neighbourcountryname);

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

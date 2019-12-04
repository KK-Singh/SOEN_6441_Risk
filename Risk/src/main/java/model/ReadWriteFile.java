package model;

import java.io.IOException;
import java.util.Map;

/**
 * This ReadWriteFile file can contains the core functions for reading and writing file.
 * 
 * @author Yash
 * @version 1.2
 */
public interface ReadWriteFile {
	
	
	/**
	 * This method is used for reading the map from the file
	 * @param filePath : to specify path of the file
	 * @param continentMap : this is a map of continent
	 * @param countryMap : this is a map of continent
	 */
	public void readFile(String filePath, Map<Integer,Continent> continentMap, Map<Integer,Country> countryMap) throws IOException;
	
	
	/**
	 * This method is used for writing the map data to the file
	 * @param filePath : to specify path of the file
	 * @param continentMap : this is a map of continent
	 * @param countryMap : this is a map of continent
	 */
	public void writeFile(String filePath, Map<Integer,Continent> continentMap, Map<Integer,Country> countryMap) throws Exception;
}

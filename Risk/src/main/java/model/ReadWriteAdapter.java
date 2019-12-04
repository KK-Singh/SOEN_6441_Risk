package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * This ReadWriteAdapter file can contains read write over-ride functions
 * and read write adaptor and function to check file type
 * 
 * @author Yash
 * @version 1.2
 */

public class ReadWriteAdapter implements ReadWriteFile,Serializable {

	DominationMapFile domFile;
	ConquestMapFile conqFile;
	
	
	/**
	 * This is a read write adaptor
	 * @param domFile : this is a domination map file object
	 * @param conqFile : this is a conquest map file object
	 */
	public ReadWriteAdapter(DominationMapFile domFile, ConquestMapFile conqFile) {
		super();
		this.domFile = domFile;
		this.conqFile = conqFile;
	}
	
	
	/**
	 * This method is used for reading the map from the file
	 * @param filePath : to specify path of the file
	 * @param continentMap : this is a map of continent
	 * @param countryMap : this is a map of continent
	 */
	@Override
	public void readFile(String filePath, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap)
			throws IOException {
		// TODO Auto-generated method stub
		int index = checkfileType(filePath);
		if(index==1) {
			this.domFile.readFile(filePath, continentMap, countryMap);
		}else {
			this.conqFile.readFile(filePath, continentMap, countryMap);
		}

	}
	
	
	/**
	 * This method is used for writing the map data to the file
	 * @param filePath : to specify path of the file
	 * @param continentMap : this is a map of continent
	 * @param countryMap : this is a map of continent
	 */
	@Override
	public void writeFile(String filePath, Map<Integer, Continent> continentMap, Map<Integer, Country> countryMap)
			throws Exception {
		// TODO Auto-generated method stub
		int index = checkfileType(filePath);
		if(index==1) {
			this.domFile.saveFile(filePath, continentMap, countryMap);
		}else {
			this.conqFile.saveFile(filePath, continentMap, countryMap);
		}

	}
	
	
	/**
	 * This method is used for checking type of file
	 * @param filePath : to specify path of the file
	 * @return int : it will return 1 if input type is country
	 * and 2 for territories else it will return -1
	 */
	private int checkfileType(String filePath) throws IOException {
		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String input = null;
		while ((input = br.readLine()) != null) {
			if(input.toLowerCase().contains("countries")) {
				return 1;
			}else if(input.toLowerCase().contains("territories")) {
				return 2;
			}
		}
		
		return -1;

	}

}

package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class ReadWriteAdapter implements ReadWriteFile,Serializable {

	DominationMapFile domFile;
	ConquestMapFile conqFile;

	public ReadWriteAdapter(DominationMapFile domFile, ConquestMapFile conqFile) {
		super();
		this.domFile = domFile;
		this.conqFile = conqFile;
	}

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

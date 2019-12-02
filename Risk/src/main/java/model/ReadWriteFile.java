package model;

import java.io.IOException;
import java.util.Map;

public interface ReadWriteFile {

	public void readFile(String filePath, Map<Integer,Continent> continentMap, Map<Integer,Country> countryMap) throws IOException;
	
	public void writeFile(String filePath, Map<Integer,Continent> continentMap, Map<Integer,Country> countryMap) throws Exception;
}

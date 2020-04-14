package nz.ac.vuw.engr300.importers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Static access class to load JSON data into the application. 
 * Access this using a static method 'load' to import data.
 * 
 * @author Nathan Duckett
 *
 */
public class JsonImporter {
	
	/**
	 * Load the specified JSON file into the application.
	 * 
	 * @param fileName String filename to import the JSON data from.
	 * @return JsonElement with the contents of the file in Java JSON format.
	 */
	public static JsonElement load(String fileName) {
		try (FileReader fr = new FileReader(new File(fileName))) {
			return JsonParser.parseReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

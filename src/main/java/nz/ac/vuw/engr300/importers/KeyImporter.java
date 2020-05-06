package nz.ac.vuw.engr300.importers;

import java.io.FileNotFoundException;

import com.google.gson.JsonObject;

/**
 * Provide basic lazy implementation to access API keys from configuration documents.
 * 
 * @author Nathan Duckett
 * @author Tim Salisbury
 *
 */
public class KeyImporter {
	private static JsonObject keyIndex;
	
	/**
	 * Get the key value from the 'keys.json' file for the specified API name.
	 * 
	 * @param keyName API name to retrieve the key value from.
	 * @return String containing the API key for the application.
	 */
	public static String getKey(String keyName) {
		if (keyIndex == null) {
			try {
				keyIndex = JsonImporter.load("src/main/resources/keys.json").getAsJsonObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return keyIndex.get(keyName).getAsString();
	}
}

package nz.ac.vuw.engr300.importers;

import java.io.FileNotFoundException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

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
	 * @throws FileNotFoundException Throws FileNotFoundException if the keys.json can't be found.
	 */
	public static String getKey(String keyName) throws FileNotFoundException {
		if (keyIndex == null) {
			try {
				keyIndex = JsonImporter.load("src/main/resources/keys.json").getAsJsonObject();
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException("Your 'keys.json' file is missing and is required for this operation.");
			} catch (JsonParseException e) {
				throw new RuntimeException("There was an error parsing 'keys.json'", e);
			}
		}
		
		return keyIndex.get(keyName).getAsString();
	}
}

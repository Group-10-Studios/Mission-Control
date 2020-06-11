package nz.ac.vuw.engr300.importers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import java.io.FileNotFoundException;

/**
 * Provide basic lazy implementation to access API keys from configuration
 * documents.
 *
 * @author Nathan Duckett
 * @author Tim Salisbury
 */
public class KeyImporter {
    private static JsonObject keyIndex;

    /**
     * Get the key value from the 'keys.json' file for the specified API name.
     *
     * @param keyName API name to retrieve the key value from.
     * @return String containing the API key for the application.
     * @throws FileNotFoundException Throws FileNotFoundException if the keys.json
     *                               can't be found.
     */
    public static String getKey(String keyName) throws KeyNotFoundException {
        if (keyIndex == null) {
            try {
                keyIndex = JsonImporter.load("src/main/resources/keys.json").getAsJsonObject();
            } catch (FileNotFoundException e) {
                throw new KeyNotFoundException("keys.json not found!", e);
            } catch (JsonParseException e) {
                throw new KeyNotFoundException("Failed to parse keys.json!", e);
            }
        }

        if (!keyIndex.has(keyName)) {
            throw new KeyNotFoundException(keyName + " not found in keys.json!");
        }
        return keyIndex.get(keyName).getAsString();
    }
}

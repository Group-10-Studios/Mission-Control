package nz.ac.vuw.engr300.importers;

import com.google.gson.JsonObject;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests to verify the KeyImporter works as expected.
 *
 * @author Ahad Rahman
 */

public class KeyImporterTest {

    /**
     * Tests whether the API string is correct when requesting the weather API key.
     * @throws KeyNotFoundException when can't find the 'weather' api key.
     */
    @Test
    public void test_getApiKeyForWeather() throws KeyNotFoundException {
        String actual = KeyImporter.getKey("weather");
        String expected = "";
        try {
            JsonObject expectedJson = JsonImporter.load("src/main/resources/keys.json").getAsJsonObject();
            expected = expectedJson.get("weather").getAsString();
        } catch (FileNotFoundException e) {
            fail("Keys.json not found");
        }
        assertEquals(expected, actual);
    }

    /**
     * Tests whether the API string is correct when requesting the maps API key.
     * @throws KeyNotFoundException when can't find the 'maps' api key.
     */
    @Test
    public void test_getApiKeyForMaps() throws KeyNotFoundException {
        String actual = KeyImporter.getKey("maps");
        String expected = "";
        try {
            JsonObject expectedJson = JsonImporter.load("src/main/resources/keys.json").getAsJsonObject();
            expected = expectedJson.get("maps").getAsString();
        } catch (FileNotFoundException e) {
            fail("Keys.json not found");
        }
        assertEquals(expected, actual);
    }

    /**
     * Tests whether the expected KeyNotFoundException is thrown when requesting a key that does not exist.
     */
    @Test
    public void test_getInvalidApiKey() {
        try {
            KeyImporter.getKey("api_key_that_does_not_exist");
            fail("Expected a KeyNotFound Exception");
        } catch (KeyNotFoundException e) {
            assert (true);
        }
    }

}

package nz.ac.vuw.engr300.importers;

import com.google.gson.JsonObject;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class KeyImporterTest {

    @Test
    public void test_getAPIKeyForWeather() throws KeyNotFoundException {
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

}

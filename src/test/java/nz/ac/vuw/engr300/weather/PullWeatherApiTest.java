package nz.ac.vuw.engr300.weather;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.weather.importers.PullWeatherApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PullWeatherApiTest {

    public static String API_KEY;
    public static final String FILEPATH = "src/test/resources/test-weather-data";

    /**
     * Initialize the API_KEY so that the test cases can use it during execution.
     */
    @BeforeAll
    public static void initTestKeys() {
        try {
            API_KEY = KeyImporter.getKey("weather");
        } catch (KeyNotFoundException e) {
            System.err.println("Can't run tests as the 'keys.json' file is missing");
            System.exit(1);
        }
    }

    @Test
    public void test_ValidLongitudeLatitude() {
        double latitude = -64.378432; // Latitude should be between -85 and 85
        double longitude = 165.764319; // Latitude should be between -180 and 180
        try {
            PullWeatherApi.importWeatherData(API_KEY, latitude, longitude, FILEPATH);
        } catch (Error e) {
            fail();
        }
    }

    @Test
    public void test_InvalidLongitude() {
        double latitude = -64.378432; // Latitude should be between -85 and 85
        double longitude = 195.764319; // Latitude should be between -180 and 180
        try {
            PullWeatherApi.importWeatherData(API_KEY, latitude, longitude, FILEPATH);
            fail();
        } catch (IllegalArgumentException e) {
            assert (true);
        }
    }

    @Test
    public void test_InvalidLatitude() {
        double latitude = -104.378432; // Latitude should be between -85 and 85
        double longitude = 175.764319;
        try {
            PullWeatherApi.importWeatherData(API_KEY, latitude, longitude, FILEPATH);
            fail();
        } catch (IllegalArgumentException e) {
            assert (true);
        }
    }

    @Test
    public void test_InvalidFilepath() {
        double latitude = -64.378432; // Latitude should be between -85 and 85
        double longitude = 165.764319; // Latitude should be between -180 and 180
        try {
            PullWeatherApi.importWeatherData("invalid/filepath", latitude, longitude, FILEPATH);
            fail();
        } catch (Error e) {
            assert (true);
        }
    }

}

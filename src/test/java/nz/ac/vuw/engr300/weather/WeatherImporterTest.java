package nz.ac.vuw.engr300.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import nz.ac.vuw.engr300.importers.JsonImporter;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify incoming weather data. All test cases are based off loading
 * the WeatherTestFile.json file in test resources. This was collected on 16/4
 * from OpenWeatherMap API manually for the 5 day/3 hour forecast.
 * 
 * @author Nathan Duckett
 *
 */
class WeatherImporterTest {
    private WeatherImporter weatherData = null;
    private static final String FILENAME = "src/test/resources/WeatherTestFile.json";

    /**
     * Beginning step before each test to import the data into a fresh
     * WeatherImporter object.
     */
    @BeforeEach
    void import_data() {
        try {
            this.weatherData = new WeatherImporter(FILENAME);
        } catch (FileNotFoundException e) {
            fail("Unable to find the WeatherTestFile.json test file");
        }
    }

    /**
     * Test data size matches as expected.
     * 
     * @throws FileNotFoundException If the test file is missing - Should fail
     *                               before this point in the import_data
     */
    @Test
    void test_data_size() throws FileNotFoundException {
        JsonElement manualLoad = JsonImporter.load("src/test/resources/WeatherTestFile.json");

        int manualCount = manualLoad.getAsJsonObject().get("list").getAsJsonArray().size();

        assertEquals(manualCount, weatherData.size());
    }

    /**
     * Test the first entry within the data file matches the expected results.
     */
    @Test
    void test_data_01() {
        WeatherData data = this.weatherData.getWeather(0);

        assertEquals(5.81, data.getWindSpeed());
        assertEquals(319, data.getWindAngle());
        assertEquals(1586995200, data.getTimestamp());
        assertEquals(287.72, data.getTemp());
        assertEquals(1003, data.getPressure());
        assertEquals(70, data.getHumidity());
        assertEquals("Clouds", data.getCondition().getWeatherState());
        assertEquals("few clouds", data.getCondition().getWeatherDescription());
    }

    /**
     * Test manually importing all data and checking it matches what is retrieved
     * from the file.
     * 
     * @throws FileNotFoundException If the test file is missing - Should fail
     *                               before this point in the import_data
     */
    @Test
    void test_all() throws FileNotFoundException {
        JsonElement manualLoad = JsonImporter.load("src/test/resources/WeatherTestFile.json");

        // Process manually loaded data.
        JsonObject resContent = manualLoad.getAsJsonObject();
        JsonArray weatherStuff = resContent.get("list").getAsJsonArray();

        // Loop all data and check the extracted values - this assumes this code is
        // correct.
        int pos = 0;
        for (JsonElement content : weatherStuff) {
            // Get the data we have imported and check they all match the loaded values.
            WeatherData thisData = this.weatherData.getWeather(pos++);

            JsonObject accessContent = content.getAsJsonObject();
            long timestamp = accessContent.get("dt").getAsLong();
            assertEquals(timestamp, thisData.getTimestamp());

            JsonObject windData = accessContent.get("wind").getAsJsonObject();
            double angle = windData.get("deg").getAsDouble();
            double speed = windData.get("speed").getAsDouble();
            assertEquals(angle, thisData.getWindAngle());
            assertEquals(speed, thisData.getWindSpeed());

            JsonObject main = accessContent.get("main").getAsJsonObject();
            double temp = main.get("temp").getAsDouble();
            double pressure = main.get("pressure").getAsDouble();
            double humidity = main.get("humidity").getAsDouble();
            assertEquals(temp, thisData.getTemp());
            assertEquals(pressure, thisData.getPressure());
            assertEquals(humidity, thisData.getHumidity());

            // Access to get first object from weather array
            JsonObject weatherDetails = accessContent.get("weather").getAsJsonArray().get(0).getAsJsonObject();
            String conditionState = weatherDetails.get("main").getAsString();
            String conditionDesc = weatherDetails.get("description").getAsString();
            assertEquals(conditionState, thisData.getCondition().getWeatherState());
            assertEquals(conditionDesc, thisData.getCondition().getWeatherDescription());

        }
    }

    /**
     * Test trying to get weather data from a negative position.
     */
    @Test
    void test_get_invalid_pos_negative() {
        assertThrows(IllegalArgumentException.class, () -> weatherData.getWeather(-1));
    }

    /**
     * Test trying to get weather data from a positive position which exceeds the
     * list size.
     */
    @Test
    void test_get_invalid_pos_positive() {
        assertThrows(IllegalArgumentException.class, () -> weatherData.getWeather(this.weatherData.size()));
    }

    /**
     * Test the ordering functionality of the weather works as expected.
     */
    @Test
    void test_ordering_weather() {
        this.weatherData.orderWeather();

        long lastTimeStamp = this.weatherData.getWeather(0).getTimestamp();
        for (int i = 1; i < this.weatherData.size(); i++) {
            long nextTimeStamp = this.weatherData.getWeather(i).getTimestamp();
            assertTrue(lastTimeStamp < nextTimeStamp);
            lastTimeStamp = nextTimeStamp;
        }
    }
}

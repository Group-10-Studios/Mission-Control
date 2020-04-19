package nz.ac.vuw.engr300.weather;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nz.ac.vuw.engr300.importers.JsonImporter;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

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
	 * Beginning step before each test to import the data into a fresh WeatherImporter object.
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
	 * @throws FileNotFoundException If the test file is missing 
	 * - Should fail before this point in the import_data
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
	}
	
	/**
	 * Test manually importing all data and checking it matches what is retrieved from the file.
	 * @throws FileNotFoundException If the test file is missing 
	 * - Should fail before this point in the import_data
	 */
	@Test
	void test_all() throws FileNotFoundException {
		JsonElement manualLoad = JsonImporter.load("src/test/resources/WeatherTestFile.json");
		
		// Process manually loaded data.
		JsonObject resContent = manualLoad.getAsJsonObject();
		JsonArray weatherStuff = resContent.get("list").getAsJsonArray();
		
		// Loop all data and check the extracted values - this assumes this code is correct.
		int pos = 0;
		for (JsonElement content : weatherStuff) {
			JsonObject accessContent = content.getAsJsonObject();
			long timestamp = accessContent.get("dt").getAsLong();
			
			JsonObject windData = accessContent.get("wind").getAsJsonObject();
			double angle = windData.get("deg").getAsDouble();
			double speed = windData.get("speed").getAsDouble();
			
			// Get the data we have imported and check they all match the loaded values.
			WeatherData thisData = this.weatherData.getWeather(pos++);
			assertEquals(timestamp, thisData.getTimestamp());
			assertEquals(angle,thisData.getWindAngle());
			assertEquals(speed, thisData.getWindSpeed());
		}
	}
	
	/**
	 * Test trying to get weather data from a negative position.
	 */
	@Test
	void test_get_invalid_pos_negative() {
		try {
			this.weatherData.getWeather(-1);
			fail("No Exception was thrown retrieving invalid position");
		} catch (IllegalArgumentException e) {
		}
	}
	
	/**
	 * Test trying to get weather data from a positive position which exceeds
	 * the list size.
	 */
	@Test
	void test_get_invalid_pos_positive() {
		try {
			this.weatherData.getWeather(this.weatherData.size());
			fail("No Exception was thrown retrieving invalid position");
		} catch (IllegalArgumentException e) {
		}
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

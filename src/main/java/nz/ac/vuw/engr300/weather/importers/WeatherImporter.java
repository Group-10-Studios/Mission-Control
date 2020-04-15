package nz.ac.vuw.engr300.weather.importers;

import java.io.FileNotFoundException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nz.ac.vuw.engr300.importers.JsonImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

/**
 * WeatherImporter is to help import the weather into the application. Processes the JSON data
 * and creates WeatherData objects which can be built on to simulate the rocket with the data.
 * 
 * @author Nathan Duckett
 *
 */
public class WeatherImporter {
	private List<WeatherData> weatherDetails;
	
	/**
	 * Create a new WeatherImporter from the provided filename.
	 * @param fileName File name which contains the required weather information.
	 * @throws FileNotFoundException thrown if the filename doesn't lead to an expected file.
	 */
	public WeatherImporter(String fileName) throws FileNotFoundException {
		JsonElement weatherInformation = JsonImporter.load(fileName);
		
		processWeather(weatherInformation);
	}
	
	/**
	 * Process the incoming weather information. Retrieve each entry and
	 * get the required content into weather data.
	 * 
	 * @param weatherInformation JsonElement containing all of the loaded weather data.
	 */
	private void processWeather(JsonElement weatherInformation) {
		JsonArray weatherStuff = weatherInformation.getAsJsonArray();
		
		//TODO: Fix structure of data to match what it is saved to when importing.
		for (JsonElement content : weatherStuff) {
			JsonObject accessContent = content.getAsJsonObject();
			long timestamp = accessContent.get("dt").getAsLong();
			
			JsonObject windData = accessContent.get("wind").getAsJsonObject();
			double angle = windData.get("deg").getAsDouble();
			double speed = windData.get("speeed").getAsDouble();
			
			this.weatherDetails.add(new WeatherData(timestamp, angle, speed));
		}
	}
}

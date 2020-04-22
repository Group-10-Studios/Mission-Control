package nz.ac.vuw.engr300.weather.importers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
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
		this.weatherDetails = new ArrayList<WeatherData>();
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
		JsonObject resContent = weatherInformation.getAsJsonObject();
		JsonArray weatherStuff = resContent.get("list").getAsJsonArray();
		
		for (JsonElement content : weatherStuff) {
			JsonObject accessContent = content.getAsJsonObject();
			long timestamp = accessContent.get("dt").getAsLong();
			
			JsonObject windData = accessContent.get("wind").getAsJsonObject();
			double angle = windData.get("deg").getAsDouble();
			double speed = windData.get("speed").getAsDouble();
			
			this.weatherDetails.add(new WeatherData(timestamp, speed, angle));
		}
	}
	
	/**
	 * Get the size of the data imported.
	 * 
	 * @return int value representing the number of data entries contained.
	 */
	public int size() {
		return this.weatherDetails.size();
	}
	
	/**
	 * Get the weather data at the specified position.
	 * 
	 * @param position Position in the list to retrieve the weather data from.
	 * @return WeatherData from the loaded information.
	 */
	public WeatherData getWeather(int position) {
		if (position < 0 || position >= this.weatherDetails.size()) {
			throw new IllegalArgumentException("The position " + position + " is not within the valid data range.");
		}
		return this.weatherDetails.get(position);
	}
	
	
	/**
	 * Order the weather in order of the timestamp increasing.
	 */
	public void orderWeather() {
		Collections.sort(this.weatherDetails);
	}
}

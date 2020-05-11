package nz.ac.vuw.engr300.weather.importers;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import nz.ac.vuw.engr300.importers.KeyImporter;

/**
 * PullWeatherAPI connects to OpenWeatherAPI and pulls down the weather forecast data. Data can be pulled every 3 hours.
 * @author Jake Mai
 * @author Ahad Rahman
 */

public class PullWeatherApi {

    public static void main(String[] args) {
        String apiKey = KeyImporter.getKey("weather");
        double latitude = -41.300442;
        double longitude = 174.780319;
        String filepath = "src/main/resources/weather-data";
        importWeatherData(apiKey, latitude, longitude, filepath);
    }

    public static void importWeatherData(String apiKey, double latitude, double longitude, String filepath) {
        if (latitude < -85.0 || latitude > 85.0){
            throw new IllegalArgumentException("Invalid latitude");
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Invalid longitude");
        }
        String returnedData = "";
        try {

            String apiCall = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

            // Fetch data
            URL useThisURL = new URL(apiCall);
            URLConnection connect = useThisURL.openConnection();
            BufferedReader bReader = new BufferedReader((new InputStreamReader(connect.getInputStream())));
            returnedData += bReader.readLine();

            // Write data to json
            String filename = latitude + "-" + longitude + "-weather.json";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath + "/" + filename));
            writer.write(returnedData);

            writer.close();
            bReader.close();

        } catch (FileNotFoundException e) {
            throw new Error("Filepath not valid");
        } catch (IOException e){
            throw new Error("API request to OpenWeather failed");
        }
    }
}

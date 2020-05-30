package nz.ac.vuw.engr300.weather.importers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import nz.ac.vuw.engr300.importers.KeyImporter;
import org.apache.log4j.Logger;

/**
 * PullWeatherAPI connects to OpenWeatherAPI and pulls down the weather forecast
 * data. Data can be pulled every 3 hours.
 * 
 * @author Jake Mai
 * @author Ahad Rahman
 */

public class PullWeatherApi {
    private static final Logger LOGGER = Logger.getLogger(PullWeatherApi.class);

    /**
     * Make a request to the weather API and download the information into the
     * application.
     * 
     * @param args Application Arguments
     */
    public static void main(String[] args) {
        String apiKey = null;
        try {
            apiKey = KeyImporter.getKey("weather");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
            System.out.println("Unable to retrieve weather as your keys.json file is missing");
            System.exit(1);
        }
        double latitude = -41.300442;
        double longitude = 174.780319;
        String filepath = "src/main/resources/weather-data";
        importWeatherData(apiKey, latitude, longitude, filepath);
    }

    /**
     * Import weather data. Downloads a weather data file into resources based on
     * the information provided.
     * 
     * @param apiKey    OpenWeatherMap API key
     * @param latitude  Latitude of the position we require weather for.
     * @param longitude Longitude of the position we require weather for.
     * @param filepath  Filepath for the root folder where to save this data.
     */
    public static void importWeatherData(String apiKey, double latitude, double longitude, String filepath) {
        if (latitude < -85.0 || latitude > 85.0) {
            throw new IllegalArgumentException("Invalid latitude");
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Invalid longitude");
        }
        String returnedData = "";
        try {

            String apiCall = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude
                    + "&appid=" + apiKey;

            // Fetch data
            URL useThisUrl = new URL(apiCall);
            URLConnection connect = useThisUrl.openConnection();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(connect.getInputStream(),
                    StandardCharsets.UTF_8)));
            returnedData += bufferedReader.readLine();

            // Write data to json
            String filename = latitude + "-" + longitude + "-weather.json";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath + "/" + filename,
                    StandardCharsets.UTF_8));
            writer.write(returnedData);

            writer.close();
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            throw new Error("Filepath not valid");
        } catch (IOException e) {
            throw new Error("API request to OpenWeather failed");
        }
    }
}

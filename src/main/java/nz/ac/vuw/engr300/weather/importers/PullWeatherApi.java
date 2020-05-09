package nz.ac.vuw.engr300.weather.importers;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nz.ac.vuw.engr300.importers.KeyImporter;


public class PullWeatherApi {

    public static void main(String[] args) {
        String apiKey = KeyImporter.getKey("weather");
        double latitude = -141.300442;
        double longitude = 174.780319;
        importWeatherData(apiKey, latitude, longitude);
    }

    public static void importWeatherData(String apiKey, double latitude, double longitude) {
        String returnedData = "";
        try {

            String apiCall = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+apiKey;

            // Fetch data
            URL useThisURL = new URL(apiCall);
            URLConnection connect = useThisURL.openConnection();
            BufferedReader bReader = new BufferedReader((new InputStreamReader(connect.getInputStream())));
            returnedData += bReader.readLine();

            // Write data to json
            String filename = latitude+"-"+longitude+"-weather.json";
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/weather-data/"+filename));
            writer.write(returnedData);

            writer.close();
            bReader.close();

        } catch (IOException e){
            throw new Error("API request to OpenWeather failed");
        }

    }
}

package nz.ac.vuw.engr300.weather.importers;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nz.ac.vuw.engr300.importers.KeyImporter;

/**
 * PullWeatherAPI connects to OpenWeatherAPI and pulls down the weather forecast data. Data can be pulled every 3 hours.
 * @author Jake Mai
 */

public class PullWeatherApi {

    public static void main(String[] args) throws MalformedURLException {
       String apiKey = KeyImporter.getKey("weather");
       String cityID = "2179538"; //Wellington
       String returnedData = "";

        /**
         * Make the API call with the cityID for Wellington and API Key
         * Write the weather data into output.json at src/main/resources/
         */
        try{
            String apiCall = "https://api.openweathermap.org/data/2.5/forecast?id="+cityID+"&appid="+apiKey;

            // Fetch data
            URL useThisURL = new URL(apiCall);
            URLConnection connect = useThisURL.openConnection();
            BufferedReader bReader = new BufferedReader((new InputStreamReader(connect.getInputStream())));
            returnedData += bReader.readLine();

            // Write data to json
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output.json"));
            writer.write(returnedData);

            writer.close();
            bReader.close();

       } catch (IOException e){
           System.out.println("Error " + e);
       }
    }
}

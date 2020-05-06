package nz.ac.vuw.engr300.weather.importers;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nz.ac.vuw.engr300.importers.KeyImporter;


public class PullWeatherApi {

    public static void main(String[] args) throws MalformedURLException {
       String apiKey = KeyImporter.getKey("weather");
       String cityID = "2179538"; //Wellington
       String returnedData = "";

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
           System.out.println("Error "+ e);
       }
    }
}

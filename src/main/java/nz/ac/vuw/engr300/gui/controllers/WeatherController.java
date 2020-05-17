package nz.ac.vuw.engr300.gui.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

import java.io.FileNotFoundException;

public class WeatherController {

    /**
     * Represents a separate weather controller in the GUI
     * @author: Nalin Aswani
     * @author: Jake Mai.
     */
    @FXML private Label lbWeather;

    public WeatherController(Label wl) {
        this.lbWeather = wl;
    }

    /**
     * updateWindSpeed creates a new instance of WeatherImporter, process the information from
     * output.json and creates a new instance of Weather Data to grab the specific weather condition date (windspeed)
     * the windspeed data will be converted to metric and displayed on the GUI.     
     */
    public void updateWindSpeed(){
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/output.json");
            WeatherData w = wi.getWeather(0);
            
            // windspeed's unit extracted from weather data is meter per second
            // To convert it to km/h: windspeed * 60 * 60 /1000 = windspeed * 3600/1000 = windpseed * 3.6
            Double winSpeedMetric = Math.round((w.getWindSpeed() * 3.6) * 100.0) / 100.0;
            lbWeather.setText("Windspeed: " + winSpeedMetric + " km/h");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

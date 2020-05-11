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
    @FXML private Label weatherLabel;
    public WeatherController(Label wl) {
        this.weatherLabel = wl;
    }

    public void updateWindSpeed(){
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/output.json");
            WeatherData w = wi.getWeather(0);
            weatherLabel.setText("Windspeed: " + w.getWindSpeed());
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

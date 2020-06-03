package nz.ac.vuw.engr300.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

import java.io.FileNotFoundException;

public class WarningsController {

    /**
     * Represents a separate warnings controller in the GUI.
     * This is for the panel on the right hand side of the UI
     *
     * @author: Nalin Aswani
     */
    @FXML
    private Label lbWarning1;
    @FXML
    private Label lbWarning2;
    private double maxWindSpeed = 5.0; //The maximum wind speed for UI to not throw warning.

    public WarningsController(Label w1, Label w2) {
        this.lbWarning1 = w1;
        this.lbWarning2 = w2;
    }

    /**
     * Checks if the given wind speed is below a certain threshold.
     * If it is not, then throw a warning in the warnings panel.
     */
    public void checkWindSpeed() {
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/output.json");
            WeatherData w = wi.getWeather(0);

            // windspeed's unit extracted from weather data is meter per second
            // To convert it to km/h: windspeed * 60 * 60 /1000 = windspeed * 3600/1000 =
            // windpseed * 3.6
            Double winSpeedMetric = Math.round((w.getWindSpeed() * 3.6) * 100.0) / 100.0;
            if (winSpeedMetric > maxWindSpeed) {
                lbWarning1.setText("WINDSPEED WARNING: Expected below " + maxWindSpeed +
                        "km/h, actual was " + winSpeedMetric + "km/h");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package nz.ac.vuw.engr300.gui.controllers;

import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

/**
 * Represents a separate weather controller in the GUI.
 *
 * @author: Nalin Aswani
 * @author: Jake Mai.
 */
public class WeatherController {

    private final RocketDataAngle windCompass;
    @FXML
    private Label lbWindSpeed, lbWindAngle;

    public WeatherController(Label wl, Label wa, RocketDataAngle windCompass) {
        this.lbWindSpeed = wl;
        this.lbWindAngle = wa;
        this.windCompass = windCompass;
    }

    /**
     * updateWindSpeed creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * grab the specific weather condition date (windspeed) the windspeed data will
     * be converted to metric and displayed on the GUI.
     */
    public void updateWindSpeed() {
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/weather-data/weather-output.json");
            WeatherData w = wi.getWeather(0);

            // windspeed's unit extracted from weather data is meter per second
            // To convert it to km/h: windspeed * 60 * 60 /1000 = windspeed * 3600/1000 =
            // windpseed * 3.6
            Double winSpeedMetric = Math.round((w.getWindSpeed() * 3.6) * 100.0) / 100.0;
            windCompass.setAngle(w.getWindAngle());
            lbWindSpeed.setText("Windspeed: " + winSpeedMetric + " km/h");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Test Wind Angle
    public void updateWindAngle() {
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/weather-data/weather-output.json");
            WeatherData w = wi.getWeather(0);

            Double windAngleMetric = Math.round((w.getWindAngle() ) * 100.0) / 100.0;
            windCompass.setAngle(w.getWindAngle());
            lbWindAngle.setText("WindAngle: " + windAngleMetric + " degrees");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

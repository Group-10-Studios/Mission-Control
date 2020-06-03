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
 * @author Nalin Aswani
 * @author Jake Mai
 */
public class WeatherController {

    private final RocketDataAngle windCompass;
    @FXML
    private Label lbWindSpeed, lbWeatherTemp, lbWeatherHumidity, lbWeatherPressure;

    public WeatherController(Label wl, Label wa, Label wh, Label wp, RocketDataAngle windCompass) {
        this.lbWindSpeed = wl;
        this.lbWeatherTemp = wa;
        this.lbWeatherHumidity = wh;
        this.lbWeatherPressure = wp;
        this.windCompass = windCompass;
    }

    /**
     * updateWindSpeed creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * grab the specific weather condition date (wind speed), the wind speed data will
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
            // Display wind angle in the compass tile.
            windCompass.setAngle(w.getWindAngle());
            lbWindSpeed.setText("Windspeed: " + winSpeedMetric + " km/h");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * updateTemp method creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * retrieve the specific weather condition date (temperature) the temperature data will
     * be converted to metric and displayed on the GUI.
     */
    public void updateTemp() {
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/weather-data/weather-output.json");
            WeatherData w = wi.getWeather(0);
            // Convert temperature from Kelvin to Celcius: temp - 273.15, displayed up to 1 decimal place.
            Double tempMetric = Math.round((w.getTemp() - 273.15 ) * 10.0) / 10.0;
            lbWeatherTemp.setText("Temperature: " + tempMetric + " degrees");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * updateHumidity method creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * retrieve the specific weather condition date (humidity) the humidity data will
     * be converted to metric and displayed on the GUI.
     */
    public void updateHumidity() {
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/weather-data/weather-output.json");
            WeatherData w = wi.getWeather(0);
            // Humidity is displayed in percentage, up to 1 decimal place.
            Double humid = Math.round((w.getHumidity() ) * 10.0) / 10.0;
            System.out.println(w.getPressure());
            lbWeatherHumidity.setText("Humidity: " + humid + "%");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * updatePressure method creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * retrieve the specific weather condition date (weather pressure) the pressure data will
     * be converted to metric and displayed on the GUI.
     */
    public void updatePressure() {
        try {
            WeatherImporter wi = new WeatherImporter("src/main/resources/weather-data/weather-output.json");
            WeatherData w = wi.getWeather(0);
            // Pressure is displayed in millibar, up to 1 decimal place
            Double pressure = Math.round((w.getPressure() ) * 10.0) / 10.0;
            lbWeatherPressure.setText("Air Pressure: " + pressure + "mb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

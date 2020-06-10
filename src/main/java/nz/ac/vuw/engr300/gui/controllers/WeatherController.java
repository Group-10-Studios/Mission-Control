package nz.ac.vuw.engr300.gui.controllers;

import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import org.apache.commons.text.WordUtils;

/**
 * Represents a separate weather controller in the GUI.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 */
public class WeatherController {

    private final RocketDataAngle windCompass;
    @FXML
    private Label lbWindSpeed;
    private Label lbWeatherTemp;
    private Label lbWeatherHumidity;
    private Label lbWeatherPressure;
    private Label lbWeatherStatus;

    private static WeatherData w; //this is all the weather data stored

    /**
     * Create a new WeatherController that stores weather data.
     * @param wl represents Wind Speed
     * @param wa represents Temperature
     * @param wh represents Air Humidity
     * @param wp represents Air Pressure
     * @param ws represents weather status
     * @param windCompass represents Wind Direction
     */
    public WeatherController(Label wl, Label wa, Label wh, Label wp, Label ws, RocketDataAngle windCompass) throws FileNotFoundException {
        this.lbWindSpeed = wl;
        this.lbWeatherTemp = wa;
        this.lbWeatherHumidity = wh;
        this.lbWeatherPressure = wp;
        this.lbWeatherStatus = ws;
        this.windCompass = windCompass;

        setWeatherData();
    }

    /**
     * updateWeatherInfo creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * grab the specific weather condition: windspeed, temperature, humidity,
     * air pressure and weather status. The data will be formatted accordingly and
     * displayed on the UI.
     */

    public void updateWeatherInfo() {
        // Original windspeed data from weather data is measured in meter per second
        // Windspeed is converted to km/h: windspeed * 60 * 60 /1000 = windspeed * 3600/1000 =
        // windpseed * 3.6
        WeatherData currentData = getWeatherData();
        Double winSpeedMetric = Math.round((currentData.getWindSpeed() * 3.6) * 100.0) / 100.0;
        // Wind angle is displayed in the compass tile.
        windCompass.setAngle(currentData.getWindAngle());
        lbWindSpeed.setText("Windspeed: " + winSpeedMetric + " km/h");

        // Temperature is converted from Kelvin to Celcius: temp - 273.15, displayed up to 1 decimal place.
        Double tempMetric = Math.round((currentData.getTemp() - 273.15) * 10.0) / 10.0;
        lbWeatherTemp.setText("Temperature: " + tempMetric + " degrees");

        // Air Humidity is displayed in percentage, up to 1 decimal place.
        Double humid = Math.round((currentData.getHumidity()) * 10.0) / 10.0;
        lbWeatherHumidity.setText("Humidity: " + humid + " %");

        // Air Pressure is displayed in millibar, up to 1 decimal place
        Double pressure = Math.round((currentData.getPressure()) * 10.0) / 10.0;
        lbWeatherPressure.setText("Air Pressure: " + pressure + " mb");

        // Sky forecast (rainy, cloudy, etc.)
        String forecast = currentData.getCondition().getWeatherDescription();
        String formattedForecast = WordUtils.capitalize(forecast);
        lbWeatherStatus.setText("Weather status: " + formattedForecast);
    }

    public WeatherData getWeatherData(){
        return w;
    }

    public static void setWeatherData() throws FileNotFoundException {
        WeatherImporter wi = new WeatherImporter("src/main/resources/weather-data/weather-output.json");
        w = wi.getWeather(0);
    }
}
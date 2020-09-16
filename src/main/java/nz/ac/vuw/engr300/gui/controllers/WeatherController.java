package nz.ac.vuw.engr300.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nz.ac.vuw.engr300.App;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.views.NavigationView;
import nz.ac.vuw.engr300.model.LaunchParameters;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import org.apache.commons.text.WordUtils;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

//import javafx.stage.StageStyle;


/**
 * Represents a separate weather controller in the GUI.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 */
public class WeatherController {
    private static final Logger LOGGER = Logger.getLogger(App.class);
    private WeatherData weather; //this is all the weather data stored
    /**
     * Store the navigation view for updating directly on information panel content updates.
     */
    private NavigationView navigationView;

    private static final WeatherController instance = new WeatherController();

    /**
     * Private constructor to prevent weather controller being created outside of in here.
     */
    private WeatherController() {
        try {
            // Build default names from LaunchParameters.
            double lat = LaunchParameters.getInstance().getLatitude().getValue();
            double lon = LaunchParameters.getInstance().getLongitude().getValue();
            String defaultFileName = buildWeatherFileFromLocation(lat, lon);
            setWeatherData(defaultFileName);
        } catch (FileNotFoundException e) {
            LOGGER.error("Weather file not found", e);
        }
    }

    /**
     * Build a file-name quickly from a lat/long position for place specific weather information.
     *
     * @param lat Latitude of this weather data.
     * @param lon Longitude of this weather data.
     * @return String containing a full path to the weather data file to be loaded.
     */
    public String buildWeatherFileFromLocation(double lat, double lon) {
        return "src/main/resources/weather-data/" + lat + "-" + lon +".json";
    }

    /**
     * Get the application's instance of WeatherController.
     *
     * @return WeatherController instance.
     */
    public static WeatherController getInstance() {
        return instance;
    }

    /**
     * Register the provided NavigationView to this weather controller to allow updating GUI when
     * a change is detected within the weather information.
     *
     * @param view NavigationView to update on weather data change.
     */
    public void registerInformationView(NavigationView view) {
        this.navigationView = view;
    }

    /**
     * Set the weather data within this WeatherController to the latest data value from the provided file.
     *
     * @param fileName Full path to the weather data file to be loaded.
     * @throws FileNotFoundException Thrown if the file specified does not exist.
     */
    public void setWeatherData(String fileName) throws FileNotFoundException {
        WeatherImporter wi = new WeatherImporter(fileName);
        weather = wi.getWeather(0);
        // Must be run on JavaFX thread.
        Platform.runLater(() -> this.navigationView.setupWeather());
    }

    /**
     * Creates a new instance of WeatherImporter, process the
     * information from output.json and creates a new instance of Weather Data to
     * grab the specific weather condition: windspeed, temperature, humidity,
     * air pressure and weather status. The data will be formatted accordingly and
     * displayed on the UI.
     */
    public void updateWeatherInfo(Label label, String metric) {
        WeatherData currentData = getWeatherData();
        if (currentData == null) {
            return;
        }
        label.setId("WeatherInfo-" + metric);
        switch (metric) {
            case "windspeed":
                Double winSpeedMetric = Math.round((currentData.getWindSpeed() * 3.6) * 100.0) / 100.0;
                label.setText("Windspeed: " + winSpeedMetric + " km/h");
                break;
            case "weathertemp":
                // Temperature is converted from Kelvin to Celcius: temp - 273.15, displayed up to 1 decimal place.
                Double tempMetric = Math.round((currentData.getTemp() - 273.15) * 10.0) / 10.0;
                label.setText("Temperature: " + tempMetric + " degrees");
                break;
            case "humidity":
                // Air Humidity is displayed in percentage, up to 1 decimal place.
                Double humid = Math.round((currentData.getHumidity()) * 10.0) / 10.0;
                label.setText("Humidity: " + humid + " %");
                break;
            case "airpressure":
                // Air Pressure is displayed in millibar, up to 1 decimal place
                Double pressure = Math.round((currentData.getPressure()) * 10.0) / 10.0;
                label.setText("Air Pressure: " + pressure + " mb");
                break;
            case "forecast":
                // Sky forecast (rainy, cloudy, etc.)
                String forecast = currentData.getCondition().getWeatherDescription();
                String formattedForecast = WordUtils.capitalize(forecast);
                label.setText("Weather status: " + formattedForecast);
                break;
            default:
                break;
        }
    }

    public WeatherData getWeatherData() {
        return weather;
    }
}
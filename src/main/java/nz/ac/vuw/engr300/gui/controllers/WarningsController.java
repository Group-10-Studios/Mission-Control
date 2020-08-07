package nz.ac.vuw.engr300.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.weather.model.WeatherData;

public class WarningsController {

    /**
     * Represents a separate warnings controller in the GUI.
     * This is for the pane on the right hand side of the UI
     *
     * @author: Nalin Aswani
     */

    private static final double maxWindSpeed = 15.0; //The maximum wind speed for UI to not throw warning.
    private static final Paint colourGreen = Color.web("#008000", 0.8);
    private static final Paint colourRed = Color.web("#ff0000", 0.8);
    private WeatherData weatherData;

    @FXML
    private Pane pnWarnings;
    // private boolean anyWarnings;

    private ObservableList<RocketAlert> items;

    /**
     * Warnings controller will create a list of Rocket Alerts.
     * E.g for weather or for simulation runs.
     * @param p the pane we want to append warnings to.
     */
    public WarningsController(Pane p) {
        this.pnWarnings = p;
        // anyWarnings = false;
        ListView<RocketAlert> list = new ListView<>();
        list.setStyle("-fx-background-insets: 0 ;");
        p.heightProperty().addListener((observableValue, number, t1) -> {
            list.setPrefHeight(t1.doubleValue());
        });
        p.widthProperty().addListener((observableValue, number, t1) -> {
            list.setPrefWidth(t1.doubleValue());
        });
        items = FXCollections.observableArrayList();
        list.setItems(items);

        pnWarnings.getChildren().add(list);
    }

    private void setDataForWarnings(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    /**
     * Checks if the given wind speed is below a certain threshold.
     * If it is not, then throw a warning in the warnings pane.
     */
    private void checkWindSpeed() {
        // windspeed's unit extracted from weather data is meter per second
        // To convert it to km/h: windspeed * 60 * 60 /1000 = windspeed * 3600/1000 =
        // windpseed * 3.6
        Double winSpeedMetric = Math.round((weatherData.getWindSpeed() * 3.6) * 100.0) / 100.0;
        if (winSpeedMetric > maxWindSpeed) {
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.ERROR, "WINDSPEED WARNING:",
                    "Expected = " + maxWindSpeed,
                    "Was actually = " + winSpeedMetric + "km/h");
            items.add(ra);
            // anyWarnings = true;
        } else {
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.WARNING,
                    "Windspeed: ", winSpeedMetric + "km/h");
            items.add(ra);
        }
    }

    /**
     * Checks the weather data against warning thresholds and displays an alert if they exceed.
     *
     * @param data  The weather data to check.
     */
    public void checkAllData(WeatherData data) {
        if (data == null) {
            return;
        }
        this.weatherData = data;
        checkWeatherCondition();
        checkWindSpeed();
    }

    /**
     * Checks if the given weather condition is idea (e.g not raining)
     * If it is not, then throw a warning in the warnings pane.
     */
    private void checkWeatherCondition() {
        String currentWeather = weatherData.getCondition().getWeatherDescription();

        if (currentWeather.contains("rain")) {
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.ERROR, "WEATHER WARNING:",
                    "Forecast = " + currentWeather,
                    "Not safe to launch.");
            items.add(ra);
            // anyWarnings = true;
        } else {
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.ALERT, "Weather forecast:",
                    "Forecast = " + currentWeather, "Safe to launch.");
            items.add(ra);
        }
    }

    /**
     * Creates a new rocket alert which is appended to the warnings pane.
     * @param alert The alert level we want the alert as (ERROR, WARNING, OR ALERT)
     * @param title The title we want the rocket alert to be.
     * @param description The description of the rocket alert.
     */
    public void addRocketAlert(RocketAlert.AlertLevel alert, String title, String... description) {
        Platform.runLater(() -> items.add(0, new RocketAlert(alert, title, description)));
    }

    /**
     * Checks if there were any warnings across all the rocket alerts.
     * @return True if there were any warnings.
     */
    public boolean hasWarnings() {
        for (RocketAlert r : items) {
            if (r.getAlertLevel() == RocketAlert.AlertLevel.WARNING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there were any errors across all the rocket alerts.
     * @return True if there were any errors.
     */
    public boolean hasErrors() {
        for (RocketAlert r : items) {
            if (r.getAlertLevel() == RocketAlert.AlertLevel.ERROR) {
                return true;
            }
        }
        return false;
    }
}


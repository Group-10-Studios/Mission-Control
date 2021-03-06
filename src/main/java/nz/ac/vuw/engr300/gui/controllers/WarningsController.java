package nz.ac.vuw.engr300.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import org.apache.commons.lang3.ObjectUtils;

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

    private static final WarningsController controllerInstance = new WarningsController();

    @FXML
    private Pane pnWarnings;

    private Boolean anyWarnings;

    private ObservableList<RocketAlert> items;

    private WarningsController() {

    }

    /**
     * Gets the only instance of the Controller created (Singleton approach).
     * @return WarningsController controllerInstance.
     */
    public static WarningsController getInstance() {
        return controllerInstance;
    }

    private Label warningCheckStatus;

    /**
     * Set the go/no go warning status label to be updated on warnings.
     *
     * @param warningCheckStatus Warning label to be updated when errors/warnings arrive.
     */
    public void setWarningCheckStatus(Label warningCheckStatus) {
        this.warningCheckStatus = warningCheckStatus;
    }


    /**
     * Pulling the Weather Data from the Weather Controller for the Warnings Pane.
     */
    public void setDataForWarnings() {
        if (checkItemsExist()) {
            this.weatherData = WeatherController.getInstance().getWeatherData();
        }
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
     */
    public void checkAllData() {
        if (checkItemsExist()) {
            if (weatherData != null) {
                checkWeatherCondition();
                checkWindSpeed();
            }
            if (this.warningCheckStatus != null) {
                if (hasWarnings() || hasErrors()) {
                    warningCheckStatus.setText("Errors exist! (Not safe to launch)");
                } else {
                    warningCheckStatus.setText("No errors exist! (Safe to launch)");
                }
            }
        }
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
     * This method checks for any warnings to point out in the weather data.
     * e.g If wind speed is too high, or if forecast is raining.
     *
     * @return True if there were any warnings.
     */
    public boolean checkForAnyWarnings() {
        if (checkItemsExist()) {
            //ensures that the warnings start off as false before running this method
            anyWarnings = false;
            checkWindSpeed();
            checkWeatherCondition();

            //if there are any warnings after running the check methods, then this returns true
            //if there were not it returns false.
            return anyWarnings;
        }
        return false;
    }

    /**
     * Creates a new rocket alert which is appended to the warnings pane.
     *
     * @param alert       The alert level we want the alert as (ERROR, WARNING, OR ALERT)
     * @param title       The title we want the rocket alert to be.
     * @param description The description of the rocket alert.
     */
    public void addRocketAlert(RocketAlert.AlertLevel alert, String title, String... description) {
        if (checkItemsExist()) {
            Platform.runLater(() -> items.add(0, new RocketAlert(alert, title, description)));
        }
    }

    /**
     * Checks if there were any warnings across all the rocket alerts.
     *
     * @return True if there were any warnings.
     */
    public boolean hasWarnings() {
        if (checkItemsExist()) {
            for (RocketAlert r : items) {
                if (r.getAlertLevel() == RocketAlert.AlertLevel.WARNING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if there were any errors across all the rocket alerts.
     *
     * @return True if there were any errors.
     */
    public boolean hasErrors() {
        if (checkItemsExist()) {
            for (RocketAlert r : items) {
                if (r.getAlertLevel() == RocketAlert.AlertLevel.ERROR) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Set the alert list for the RocketAlert.
     *
     * @param items Observable RocketAlert list which draws out alerts to the GUI.
     */
    public void setAlertList(ObservableList<RocketAlert> items) {
        this.items = items;
    }

    /**
     * Check if the RocketAlert list exists.
     *
     * @return boolean indicating it exists.
     */
    public boolean checkItemsExist() {
        return items != null;
    }

    /**
     * Clear all of the events in the RocketAlert list.
     */
    public void clearAllEvents() {
        this.items.clear();
    }
}


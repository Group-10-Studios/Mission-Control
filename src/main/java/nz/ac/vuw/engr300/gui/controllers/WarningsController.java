package nz.ac.vuw.engr300.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import java.io.FileNotFoundException;

public class WarningsController {

    /**
     * Represents a separate warnings controller in the GUI.
     * This is for the panel on the right hand side of the UI
     *
     * @author: Nalin Aswani
     */

    private static final double maxWindSpeed = 15.0; //The maximum wind speed for UI to not throw warning.
    private static final Paint colourGreen = Color.web("#008000", 0.8);
    private static final Paint colourRed = Color.web("#ff0000", 0.8);
    private static WeatherData w;
    //    @FXML
//    private Label lbWarning1;
//    @FXML
//    private Label lbWarning2;
    @FXML
    private Pane pnWarnings;
    private boolean anyWarnings;

    private ObservableList<RocketAlert> items;

    /**
     * //     * @param w1 The first warnings label.
     * //     * @param w2 The second warnings label.
     *
     * @throws FileNotFoundException in case there is an error reading the weather importer.
     */
    public WarningsController(WeatherData wGiven, Region p) throws FileNotFoundException {
//        this.lbWarning1 = w1;
//        this.lbWarning2 = w2;
        this.pnWarnings = (Pane) p;
        anyWarnings = false;
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
        setDataForWarnings(wGiven);
    }

    private static void setDataForWarnings(WeatherData wGiven) {
        w = wGiven;
    }

    /**
     * Checks if the given wind speed is below a certain threshold.
     * If it is not, then throw a warning in the warnings panel.
     */
    public void checkWindSpeed() {
        // windspeed's unit extracted from weather data is meter per second
        // To convert it to km/h: windspeed * 60 * 60 /1000 = windspeed * 3600/1000 =
        // windpseed * 3.6
        Double winSpeedMetric = Math.round((w.getWindSpeed() * 3.6) * 100.0) / 100.0;
        if (winSpeedMetric > maxWindSpeed) {
//            lbWarning1.setText("WINDSPEED WARNING:\n  Expected less than " + maxWindSpeed +
//                    "km/h,\n  Actual was " + winSpeedMetric + "km/h.");
//            lbWarning1.setTextFill(colourRed); //Sets it red if warnings.
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.WARNING, "WINDSPEED WARNING:",
                    "Expected = " + maxWindSpeed,
                    "Was actually = " + winSpeedMetric + "km/h");
            items.add(ra);
            anyWarnings = true;
        } else {
//            lbWarning1.setText("Windspeed: Everything is ok!\n  Less than " + maxWindSpeed +
//                    "km/h,\n  Actual was " + winSpeedMetric + "km/h.");
//            lbWarning1.setTextFill(colourGreen); //Sets it green if no warnings
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.WARNING,
                    "Windspeed: ", winSpeedMetric + "km/h");
            items.add(ra);
        }
    }

    /**
     * Checks if the given weather condition is idea (e.g not raining)
     * If it is not, then throw a warning in the warnings panel.
     */
    public void checkWeatherCondition() {
        String currentWeather = w.getCondition().getWeatherDescription();

        if (currentWeather.contains("rain")) {
//            lbWarning2.setText("WEATHER WARNING:\n  Forecast = " + currentWeather +
//                    "\n  Not safe to launch.");
//            lbWarning2.setTextFill(colourRed); //Sets it red if warnings.
            RocketAlert ra = new RocketAlert(RocketAlert.AlertLevel.WARNING, "WEATHER WARNING:",
                    "Forecast = " + currentWeather,
                    "Not safe to launch.");
            items.add(ra);
            anyWarnings = true;
        } else {
//            lbWarning2.setText("Weather forecast: Everything is ok!\n  Forecast = " + currentWeather +
//                    "\n  Safe to launch.");
//            lbWarning2.setTextFill(colourGreen); //Sets it green if no warnings
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
        //ensures that the warnings start off as false before running this method
        anyWarnings = false;
        checkWindSpeed();
        checkWeatherCondition();

        //if there are any warnings after running the check methods, then this returns true
        //if there were not it returns false.
        return anyWarnings;
    }
}


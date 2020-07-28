package nz.ac.vuw.engr300.gui.controllers;

import javafx.scene.layout.Pane;

public class NavigationController {

    private WeatherController weatherC;
    private ButtonController buttonC;

    public NavigationController() {
        weatherC = new WeatherController();
        buttonC = new ButtonController();
    }

    public WeatherController getWeatherC() {
        return weatherC;
    }
}

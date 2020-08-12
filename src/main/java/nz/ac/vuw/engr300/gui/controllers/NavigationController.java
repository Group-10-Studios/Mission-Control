package nz.ac.vuw.engr300.gui.controllers;

import nz.ac.vuw.engr300.model.LaunchParameters;

/**
 * Controller responsible for the controllers that make up the left side panel.
 *
 * @author Ahad Rahman
 */
public class NavigationController {



    private WeatherController weatherController;
    private ButtonController buttonController;

    /**
     * Creates instances of the necessary controllers.
     */
    public NavigationController() {
        weatherController = WeatherController.getInstance();
        buttonController = new ButtonController();
    }

    public WeatherController getWeatherController() {
        return weatherController;
    }

    public ButtonController getButtonController() {
        return buttonController;
    }

}

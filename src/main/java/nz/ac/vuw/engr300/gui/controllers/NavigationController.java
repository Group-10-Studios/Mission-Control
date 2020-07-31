package nz.ac.vuw.engr300.gui.controllers;

import nz.ac.vuw.engr300.gui.views.GraphView;

public class NavigationController {

    private WeatherController weatherController;
    private ButtonController buttonController;
    private GraphController graphController;

    public NavigationController() {
        weatherController = new WeatherController();
        buttonController = new ButtonController();
        graphController = new GraphController();
    }

    public WeatherController getWeatherController() {
        return weatherController;
    }
    public ButtonController getButtonController() {
        return buttonController;
    }
    public GraphController getGraphController() {
        return graphController;
    }
}

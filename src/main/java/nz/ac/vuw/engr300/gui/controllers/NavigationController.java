package nz.ac.vuw.engr300.gui.controllers;

/**
 * Controller responsible for the controllers that make up the left side panel.
 *
 * @author Ahad Rahman
 */
public class NavigationController {

    private WeatherController weatherController;
    private ButtonController buttonController;
    private GraphController graphController;

    /**
     * Creates instances of the necessary controllers.
     */
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

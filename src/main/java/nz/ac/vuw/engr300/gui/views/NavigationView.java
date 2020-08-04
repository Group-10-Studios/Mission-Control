package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.controllers.ButtonController;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.controllers.NavigationController;
import nz.ac.vuw.engr300.gui.controllers.WeatherController;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import java.io.FileNotFoundException;

import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;

/**
 * Represents the left panel, which displays navigation controls.
 *
 * @author Tim Salisbury, Ahad Rahman
 */
public class NavigationView implements View {

    private final GridPane root;
    private final GraphView graphView;
    public NavigationController navigationC;

    public Button pastFlightsButton = new Button("Past Flights");
    public Button runSimButton = new Button("Run Simulation");


    /**
     * Add Batteries and warning sections of right hand side panel.
     * @param root The root Gridpane where we will be adding nodes to.
     */
    public NavigationView(GridPane root, GraphView graphView) {
        this.graphView = graphView;
        this.root = root;
        navigationC = new NavigationController();
        setupRoot();
        setupWeather();
        setupButtons();
        setupSimulationButtons();
    }

    /**
     * Display the weather at the top of the left panel.
     */
    private void setupWeather() {
        try {
            WeatherController.setWeatherData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        WeatherController weatherC = navigationC.getWeatherController();

        Label l1 = new Label();
        Label l2 = new Label();
        Label l3 = new Label();
        Label l4 = new Label();
        Label l5 = new Label();
        weatherC.updateWeatherInfo(l1, "windspeed");
        weatherC.updateWeatherInfo(l2, "weathertemp");
        weatherC.updateWeatherInfo(l3, "humidity");
        weatherC.updateWeatherInfo(l4, "airpressure");
        weatherC.updateWeatherInfo(l5, "forecast");

        VBox weatherVBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10), l1, l2, l3, l4, l5);
        addNodeToGrid(weatherVBox, root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
    }

    /**
     * Display the navigation buttons on the left panel.
     */
    public void setupButtons() {
        ButtonController buttonC = navigationC.getButtonController();
        buttonC.updateButtons(graphView);
        VBox buttons = UiUtil.createMinimumVerticalSizeVBox(buttonC.getPnNavButtons());
        addNodeToGrid(buttons, root, 1, 0);
    }


    /**
     * Displays the Run Flights and Simulation Buttons on the bottom of the left panel.
     */
    private void setupSimulationButtons() {
        GraphController graphC = graphView.getController();

        pastFlightsButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
        runSimButton.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
                CornerRadii.EMPTY, Insets.EMPTY)));

        //TODO: Find out how to get the graphs
        runSimButton.setOnAction(e -> graphC.runSim(graphView.getGraphs()));
        pastFlightsButton.setOnAction(e -> graphC.runSim(graphView.getGraphs()));

        VBox vbox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10),
                pastFlightsButton, runSimButton);
        // Literally just for setting background colour
        vbox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Set it to hug the warnings above it
        GridPane.setValignment(vbox, VPos.TOP);
        addNodeToGrid(vbox, root, 2, 0, Insets.EMPTY);
    }

    /**
     * Set up the root panel on the right hand side which wraps around other panes.
     */
    private void setupRoot() {
        assert this.root != null;

        this.root.setBackground(new Background(new BackgroundFill(Colours.SHADE_COLOUR,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // One column, 100 percent of width
        UiUtil.addPercentColumns(this.root, 100);
        // 20 for batteries, 60 for warnings, 20 for go/no go
        UiUtil.addPercentRows(this.root, 20, 60, 20);
    }

}

package nz.ac.vuw.engr300.gui.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.gui.controllers.ButtonController;
import nz.ac.vuw.engr300.gui.controllers.InformationController;
import nz.ac.vuw.engr300.gui.controllers.NavigationController;
import nz.ac.vuw.engr300.gui.controllers.WeatherController;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import java.awt.*;
import java.io.FileNotFoundException;

import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;

/**
 * Represents the left panel, which displays navigation controls.
 *
 * @author Tim Salisbury
 */
public class NavigationView implements View {

    private final GridPane root;

    public NavigationController navigationC;


    /**
     * Add Batteries and warning sections of right hand side panel.
     * @param root The root Gridpane where we will be adding nodes to.
     */
    public NavigationView(GridPane root) {
        this.root = root;
        navigationC = new NavigationController();
        setupRoot();
        setupWeather();
        setupButtons();
    }

    /**
     * Create new Warnings pane on the right hand side root panel.
     */
    private void setupWeather(){
        try {
            WeatherController.setWeatherData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        WeatherController weatherC = navigationC.getWeatherC();

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







//
//        //Setup the 5 labels that we want, with empty text
//        String[] metrics = new String[] {"windspeed", "weathertemp", "humidity", "airpressure", "forecast"};
//
//        for (int i = 0; i < metrics.length; i++) {
//            Label label = new Label();
//            weatherC.updateWeatherInfo(label, metrics[i]);
//        }
//
//        // VBox .. put the labels inside there
//
//
//
//
//
//
//        // For the warnings controller
//        navigationC = new NavigationController();
    }

    public void setupButtons(){
        Pane pnButtons = new Pane();

        ButtonController buttonC = navigationC.getButtonC();
        buttonC.updateButtons();

        ListView<Button> list = new ListView<>();
        ObservableList<Button> observableList = FXCollections.observableArrayList();
        observableList.addAll(buttonC.getPnNavButtons());
        list.setItems((observableList));

        addNodeToGrid(list, root, 1, 0);

    }


    /**
     * Create Go/NoGo button at the bottom of the right hand side root panel using VBox.
     */
    private void setupGoNoGo() {
//        goButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
//                CornerRadii.EMPTY, Insets.EMPTY)));
//        noGoButton.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
//                CornerRadii.EMPTY, Insets.EMPTY)));
//
//        goButton.setOnAction(e -> infController.onGo(e));
//        noGoButton.setOnAction(e -> infController.onNoGo(e));
//
//        // Create and populate go no go at bottom of right hand side
//        VBox goNoGoVBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10),
//                goButton, noGoButton);
//        // Literally just for setting background colour
//        goNoGoVBox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
//                CornerRadii.EMPTY, Insets.EMPTY)));
//
//        // Set it to hug the warnings above it
//        GridPane.setValignment(goNoGoVBox, VPos.TOP);
//        addNodeToGrid(goNoGoVBox, root, 2, 0, Insets.EMPTY);
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

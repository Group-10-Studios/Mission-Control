package nz.ac.vuw.engr300.gui.components;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.Colours;

/**
 *  A hybrid between RocketDataAngle and RocketDataLineChart. This component has the functionality of both, with
 *  a button to switch between the two display modes.
 *
 * @author Tim Salisbury
 */
public class RocketDataAngleLineChart extends StackPane implements RocketGraph {
    private boolean compassView = true;

    private GraphType type;

    private final RocketDataAngle angleComponent;
    private final RocketDataLineChart graphComponent;

    /**
     * Constructs a new RocketDataAngleLineChart.
     *
     * @param xlabel        The x label for the line chart component.
     * @param ylabel        The y label for the line chart component.
     * @param isCompass     Whether or not the angle component is a compass or angle.
     * @param graphType     The GraphType this graph shows to represent its' ID.
     */
    public RocketDataAngleLineChart(String xlabel, String ylabel, boolean isCompass, GraphType graphType) {
        this.graphComponent = new RocketDataLineChart(xlabel, ylabel, graphType);
        this.angleComponent = new RocketDataAngle(isCompass, graphType);

        // Make sure width and height remain the same - Don't remove
        widthProperty().addListener(e -> {
            graphComponent.setPrefWidth(this.getWidth());
            angleComponent.setPrefWidth(this.getWidth());
        });
        heightProperty().addListener(e -> {
            graphComponent.setPrefHeight(this.getHeight());
            angleComponent.setPrefHeight(this.getHeight());
        });

        // Initial view
        this.getChildren().add(angleComponent);
        Button switchViewButton = new Button();

        // Load the icon
        ImageView buttonIcon = new ImageView("file:src/main/resources/icons/switch_graph_mode_icon.png");
        switchViewButton.setGraphic(buttonIcon);
        StackPane.setMargin(switchViewButton, new Insets(10));
        StackPane.setAlignment(switchViewButton, Pos.TOP_RIGHT);
        switchViewButton.setOnAction(actionEvent -> {
            compassView = !compassView;
            this.getChildren().remove(0);
            this.getChildren().add(0, (compassView ? angleComponent : graphComponent));
        });

        this.getChildren().add(switchViewButton);

        this.setGraphType(graphType);
        this.setId(graphType.getGraphID());
    }


    /**
     * This function will add a value to the line graph.
     *
     * @param x The x value
     * @param y The y value
     */
    public void addValue(double x, double y) {
        this.graphComponent.addValue(x, y);
        this.angleComponent.setAngle(y);
    }


    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
    }

    @Override
    public GraphType getGraphType() {
        return type;
    }

    @Override
    public void clear() {

    }
}

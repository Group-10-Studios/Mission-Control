package nz.ac.vuw.engr300.gui.components;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.Colours;

import java.util.Formatter;

import static nz.ac.vuw.engr300.gui.util.Colours.PRIMARY_COLOUR;


/**
 * A component that displays rocket data (two double values) as a line graph.
 *
 * @author Tim Salisbury
 */
public class RocketDataLineChart extends LineChart<Number, Number> implements RocketGraph {
    private boolean isVisible = true;

    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    private static double upperXBound = 10.0;

    private GraphType type;

    /**
     * Constructs a new RocketDataLineChart, note this will be most likely
     * initialized in fxml code. For example, {@code
     *      <RocketDataLineChart xLabel="Time (s)" yLabel
    ="Altitude"/>
     * }
     *
     * @param xlabel    The x label.
     * @param ylabel    The y label.
     * @param graphType The GraphType this graph shows to represent its' ID.
     */
    public RocketDataLineChart(@NamedArg("xLabel") String xlabel,
                               @NamedArg("yLabel") String ylabel, GraphType graphType) {
        super(new NumberAxis(), new NumberAxis());
        this.setLegendVisible(false);
        this.getXAxis().setLabel(xlabel);
        this.getYAxis().setLabel(ylabel);
        this.getData().add(series);
        this.setAnimated(false);
        this.setCreateSymbols(false);
        ((NumberAxis) this.getXAxis()).setUpperBound(upperXBound);
        this.getXAxis().setAutoRanging(false);

        this.setBackground(new Background(new BackgroundFill(Colours.BACKGROUND_COLOUR,
                CornerRadii.EMPTY, Insets.EMPTY)));

        this.series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: "
                + Colours.toHexString(PRIMARY_COLOUR) + ";");
        this.setGraphType(graphType);

        this.setId(graphType.getGraphID());
        this.setCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void clear() {
        this.series.getData().clear();
        ((NumberAxis) this.getXAxis()).setUpperBound(upperXBound);
        this.getXAxis().setAutoRanging(false);
    }



    /**
     * This function will add a value to the line graph.
     *
     * @param x The x value
     * @param y The y value
     */
    public void addValue(double x, double y) {
        if (!this.getXAxis().isAutoRanging() && x > upperXBound) {
            this.getXAxis().setAutoRanging(true);
        }
        final XYChart.Data<Number, Number> data = new XYChart.Data<>(x, y);
        data.setNode(new HoveredThresholdNode(x, y));
        Platform.runLater(() -> series.getData().add(data));
    }

    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
        this.setTitle(g.getLabel());
    }

    @Override
    public GraphType getGraphType() {
        return this.type;
    }

    @Override
    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    @Override
    public boolean isGraphVisible() {
        return this.isVisible;
    }

    /** a node which displays a value on hover, but is otherwise empty */
    class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(double priorValue, double value) {
            setStyle("-fx-background-color: #4267B2;");
            setPrefSize(1, 1);

            final Label label = createDataThresholdLabel(priorValue, value);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }

        private Label createDataThresholdLabel(double priorValue, double value) {
            final Label label = new Label(priorValue + ", " + value);
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");

            if (priorValue == 0) {
                label.setTextFill(Color.DARKGRAY);
            } else if (value > priorValue) {
                label.setTextFill(Color.FORESTGREEN);
            } else {
                label.setTextFill(Color.FIREBRICK);
            }

            label.setMinSize(150, Label.USE_PREF_SIZE);
            return label;
        }
    }
}

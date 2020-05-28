package nz.ac.vuw.engr300.gui.components;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import nz.ac.vuw.engr300.gui.model.GraphType;

import java.awt.*;

/**
 * A component that displays rocket data (two double values) as a line graph.
 *
 * @author Tim Salisbury
 */
public class RocketDataLineChart extends LineChart<Number, Number> implements RocketGraph {

    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    private static double upperXBound = 10.0;
    
    private GraphType type;

    /**
     * Constructs a new RocketDataLineChart, note this will be most likely initialized in fxml code.
     * For example,
     * {@code
     *      <RocketDataLineChart title="Altitude over Time" xLabel="Time (s)" yLabel="Altitude"/>
     * }
     *
     * @param xLabel    The x label
     * @param yLabel    The y label
     */
    public RocketDataLineChart(@NamedArg("xLabel") String xLabel,
                               @NamedArg("yLabel") String yLabel) {
        super(new NumberAxis(), new NumberAxis());
        this.setLegendVisible(false);
        this.getXAxis().setLabel(xLabel);
        this.getYAxis().setLabel(yLabel);
        this.getData().add(series);
        this.setAnimated(false);
        this.setCreateSymbols(false);
        ((NumberAxis) this.getXAxis()).setUpperBound(upperXBound);
        this.getXAxis().setAutoRanging(false);

        series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #4267B2;");
    }

    /**
     * Clears the current graph, and resets the X scale.
     */
    public void clear(){
        this.series.getData().clear();
        ((NumberAxis) this.getXAxis()).setUpperBound(upperXBound);
        this.getXAxis().setAutoRanging(false);
    }

    /**
     * This function will add a value to the line graph.
     *
     * @param x     The x value
     * @param y     The y value
     */
    public void addValue(double x, double y){
        if(!this.getXAxis().isAutoRanging() && x > upperXBound){
            this.getXAxis().setAutoRanging(true);
        }
        Platform.runLater(()->series.getData().add(new Data<>(x, y)));
    }
    
    @Override
    public void setGraphType(GraphType g) {
    	this.type = g;
    }
    
    @Override
    public GraphType getGraphType() {
    	return this.type;
    }
}

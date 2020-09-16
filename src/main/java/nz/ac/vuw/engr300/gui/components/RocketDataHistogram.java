package nz.ac.vuw.engr300.gui.components;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.Colours;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nz.ac.vuw.engr300.gui.util.Colours.PRIMARY_COLOUR;

/**
 * A component that displays rocket data (two double values) as a histogram.
 *
 * @author Tim Salisbury, Ahad Rahman, Jake Mai
 */
public class RocketDataHistogram extends BarChart<String, Number> implements RocketGraph {

    private static final int NUM_BINS = 10;
    private GraphType type;
    private boolean isVisible = true;
    private XYChart.Series<String, Number> series = new XYChart.Series<>();

    /**
     * Constructs a new RocketDataHistogram.
     * @param graphType The graphType.
     * @param data The data being displayed.
     */
    public RocketDataHistogram(GraphType graphType, List<Number> data) {
        super(new CategoryAxis(), new NumberAxis());
        this.getXAxis().setLabel("Range");
        this.getYAxis().setLabel("Frequency");
        this.setCategoryGap(0);
        this.setBarGap(0);
        this.setLegendVisible(false);
        this.setGraphType(graphType);

        List<Double> dataVal = data.stream().map(Number::doubleValue).collect(Collectors.toList());
        double max = Math.ceil((Collections.max(dataVal) + 1) / 10d) * 10d;
        double min = Math.floor(Collections.min(dataVal) / 10d) * 10d;
        double range = (max - min) / NUM_BINS;
        Double[][] bins = generateGroupings(dataVal, range);

        for (int i = 0; i < bins.length; i++) {
            series.getData().add(new XYChart.Data<>(String.format("[%.1f - %.1f) ", (i * range), ((i + 1) * range)),
                   bins[i].length));
        }
        this.getData().addAll(series);

        for (Node n : this.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: " + Colours.toHexString(PRIMARY_COLOUR) + ";");
        }
    }

    /**
     * Generates the bins for the histogram.
     * @param data The incoming data to be displayed.
     * @param range The range from the min and max of the data.
     * @return 2D double array of the bins.
     */
    private static Double[][] generateGroupings(List<Double> data, double range) {
        Double[][] bins = new Double[NUM_BINS][];

        for (int i = 0; i < NUM_BINS; i++) {
            int finalI = i;
            bins[i] = data.stream()
                    .filter(value -> value >= finalI * range
                            && value < (finalI + 1) * range)
                    .toArray(Double[]::new);
        }
        return bins;
    }

    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
        this.setTitle(g.getLabel());
    }

    @Override
    public void clear() {
        this.series.getData().clear();
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

}

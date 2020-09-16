package nz.ac.vuw.engr300.gui.components;

import javafx.beans.NamedArg;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import nz.ac.vuw.engr300.gui.model.GraphType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RocketDataHistogram extends BarChart<String, Number> {

    private static final int NUM_BINS = 10;

    public RocketDataHistogram(@NamedArg("xLabel") String xlabel,
                               @NamedArg("yLabel") String ylabel, GraphType graphType,
                               List<Number> data) {
        super(new CategoryAxis(), new NumberAxis());
        this.getXAxis().setLabel(xlabel);
        this.getYAxis().setLabel(ylabel);
        this.setCategoryGap(0);
        this.setBarGap(0);
//        data.stream().map(value -> value.doubleValue())
//        2, 2, 3, 10, 200
//        ((MAX + 1)) =
//        199 / 10 = 19.9 => RANGE, with 10 bins in total
//        1) [0-20) = {1, 2, 3, 10}
//        2) [10-20) = {}
//        3) [20-30) = {}
//        4)
//        ...
//        10) [190-200) = {200}


    }

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

    public static void main(String[] args) {

        List<Double> values = new ArrayList<>(Arrays.asList(0.425d, 13.55462d, 14.1235d, 20.12357d, 39.2357));
        double max = Math.ceil((Collections.max(values) + 1) / 10d) * 10d;
        double min = Math.floor(Collections.min(values) / 10d) * 10d;

        double range = (max - min) / NUM_BINS;
        Double[][] bins = generateGroupings(values, range);

        for (int y = 0; y < bins.length; y++) {
            System.out.printf("[%f - %f): ", (y * range), ((y + 1) * range));
            for (int x = 0; x < bins[y].length; x++) {
                System.out.printf("%f ", bins[y][x]);
            }
            System.out.println();
        }
    }
}

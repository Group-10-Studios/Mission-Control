package nz.ac.vuw.engr300.gui.components;

import javafx.beans.NamedArg;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import nz.ac.vuw.engr300.gui.model.GraphType;

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
//        1, 2, 3, 10, 200
//        200 - 1 = 199
//        199 / 10 = 19.9 => RANGE, with 10 bins in total
//        1) [0-20) = {1, 2, 3, 10}
//        2) [10-20) = {}
//        3) [20-30) = {}
//        4)
//        ...
//        10) [190-200) = {200}

    }

    private Number[][] generateGroupings(List<Number> data, double range) {
        Number[][] bins = new Number[NUM_BINS][];


    }
}

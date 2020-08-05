package nz.ac.vuw.engr300.gui.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.GraphView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller to handle the buttons that are linked to the graph on the left side panel.
 *
 *
 * @author Ahad Rahman
 */
public class ButtonController {

    private static final double BUTTON_HEIGHT = 30;

    private List<Button> pnNavButtons = new ArrayList<>();


    /**
     * Updates the buttons and sets them to the appropriate graph.
     * @param graphView GraphView to hold the graphs
     */
    public void updateButtons(GraphView graphView) {
        List<String> labels = Stream.of(GraphType.values()).map(g -> g.getLabel()).collect(Collectors.toList());
        ButtonSelected buttonSelected = new ButtonSelected();
        int y = 5;
        reorderGraphs(labels, graphView);
        for (String label : labels) {
            Button b = new Button(label);
            b.setId("btn" + label.replace(" ", ""));
            b.setLayoutY(y);
            b.setOnAction(e -> {
                GraphType thisGraph = GraphType.fromLabel(label);
                for (RocketGraph chart : graphView.getGraphs()) {
                    // Get the chart as a region to set the Border
                    Region chartRegion = (Region) chart;
                    if (chart.getGraphType() == thisGraph) {
                        graphView.highlightGraph(chartRegion, thisGraph);
                    } else {
                        chartRegion.setBorder(null);
                    }
                }
            });
            pnNavButtons.add(b);
            y += BUTTON_HEIGHT;
        }

    }

    public Button[] getPnNavButtons() {
        return pnNavButtons.toArray(new Button[pnNavButtons.size()]);
    }

    /**
     * Reorders the graphs on the center panel.
     * @param labels The labels we are comparing the graphs to.
     */
    private void reorderGraphs(List<String> labels, GraphView graphView) {
        // List<RocketGraph> originalGraphs = graphView.getGraphs();
        List<RocketGraph> updatedGraphs = new ArrayList<>(graphView.getGraphs());
        for (int i = 0; i < labels.size(); i++) {
            for (int j = 0; j < updatedGraphs.size(); j++) {
                if (updatedGraphs.get(j).getGraphType().getLabel().equals(labels.get(i))) {
                    RocketGraph temp = updatedGraphs.get(j);
                    updatedGraphs.remove(j);
                    updatedGraphs.add(i, temp);
                }
            }
        }

        graphView.updateGraphs(updatedGraphs);
    }

    /**
     * Records relative y coordinates.
     *
     * @author Ahad Rahman
     */
    static class ButtonSelected {
        double originalY;
        double nextY;
    }
}

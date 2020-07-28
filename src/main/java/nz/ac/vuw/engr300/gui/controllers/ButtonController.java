package nz.ac.vuw.engr300.gui.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ButtonController {

    private List<Button> pnNavButtons;

    public void updateButtons(){
        List<String> labels = Stream.of(GraphType.values()).map(g -> g.getLabel()).collect(Collectors.toList());

        for(String label : labels){
            Button b = new Button(label);
            b.setId("btn" + label.replace(" ", ""));

            b.setOnAction(e -> {
                GraphType thisGraph = GraphType.fromLabel(label);
//                for (RocketGraph chart : this.graphs) {
//                    // Get the chart as a region to set the Border
//                    Region chartRegion = (Region) chart;
//                    if (chart.getGraphType() == thisGraph && thisGraph != this.highlightedGraph) {
//                        chartRegion.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID,
//                                new CornerRadii(5.0), new BorderWidths(2.0))));
//                        this.highlightedGraph = thisGraph;
//                    } else if (chart.getGraphType() == thisGraph && thisGraph == this.highlightedGraph) {
//                        // Ensure the clicked type is thisGraph and check if it is already clicked.
//                        chartRegion.setBorder(null);
//
//                        // Set the highlighted graph to null if already highlighted before.
//                        // This is for turning off highlighting to re-enable.
//                        this.highlightedGraph = null;
//                    } else {
//                        chartRegion.setBorder(null);
//                    }
//                }
            });
        }

    }
    public List<Button> getPnNavButtons() {
        return pnNavButtons;
    }
}

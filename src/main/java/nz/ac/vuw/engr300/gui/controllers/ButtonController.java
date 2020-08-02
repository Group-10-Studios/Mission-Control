package nz.ac.vuw.engr300.gui.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.GraphView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller to handle the buttons that are linked to the graph on the left side panel
 *
 *
 * @author Ahad Rahman
 */
public class ButtonController {

    private static final double BUTTON_HEIGHT = 30;

    private List<Button> pnNavButtons = new ArrayList<>();

    public void updateButtons(GraphView graphView){
        List<String> labels = Stream.of(GraphType.values()).map(g -> g.getLabel()).collect(Collectors.toList());
        ButtonSelected buttonSelected = new ButtonSelected();
        int y = 5;
        for(String label : labels){
            Button b = new Button(label);
            b.setId("btn" + label.replace(" ", ""));
            b.setLayoutY(y);
//            b.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    b.toFront();
//                    buttonSelected.originalY = b.getLayoutY();
//                    buttonSelected.nextY = b.getLayoutY() - mouseEvent.getSceneY();
//                }
//            });
//            b.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    b.setLayoutY(mouseEvent.getSceneY() + buttonSelected.nextY);
//                }
//            });
//            b.setOnMouseReleased(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    double distanceMoved = Math.abs(buttonSelected.originalY - b.getLayoutY());
//                    // If the user has dragged the button past the halfway point of a button
//                    // boundary
//                    if (distanceMoved > BUTTON_HEIGHT / 2) {
//                        String buttonBeingMovedLabel = b.getText();
//                        int indexOfButtonBeingMoved = labels.indexOf(buttonBeingMovedLabel);
//                        int indexFurther = (int) Math.floor(distanceMoved / (BUTTON_HEIGHT - 1));
//                        int indexToReplace;
//                        // Figuring out which direction the user is dragging. If this is true, the user
//                        // is dragging downwards
//                        if (buttonSelected.originalY - b.getLayoutY() < 0) {
//                            indexToReplace = indexOfButtonBeingMoved + indexFurther;
//                            if (indexToReplace > labels.size() - 1) {
//                                // If the user drags beyond the list, replace the last button
//                                indexToReplace = labels.size() - 1;
//                            }
//                        } else {
//                            indexToReplace = indexOfButtonBeingMoved - indexFurther;
//                            if (indexToReplace < 0) {
//                                indexToReplace = 0; // If the user drags above the list, replace the first button
//                            }
//                        }
//                        String btnBeingReplaced = labels.get(indexToReplace);
//                        for (Button bt : pnNavButtons) {
//                            if (bt.getText().equals(btnBeingReplaced)) {
//                                b.setLayoutY(bt.getLayoutY());
//                                bt.setLayoutY(buttonSelected.originalY);
//                            }
//                        }
//                        // Swap the two butons
//                        Collections.swap(labels, indexOfButtonBeingMoved, indexToReplace);
//                        reorderGraphs(labels, graphView);
//                    } else {
//                        // If the user barely drags the button (by mistake), then put it back
//                        b.setLayoutY(buttonSelected.originalY);
//                    }
//                }
//            });
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
//        List<RocketGraph> originalGraphs = graphView.getGraphs();
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

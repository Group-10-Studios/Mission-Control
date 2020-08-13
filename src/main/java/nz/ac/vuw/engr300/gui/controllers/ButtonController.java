package nz.ac.vuw.engr300.gui.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import nz.ac.vuw.engr300.gui.components.NavigationButton;
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

    private List<NavigationButton> pnNavButtons = new ArrayList<>();


    /**
     * Updates the buttons and sets them to the appropriate graph.
     */
    public void updateButtons() {
        List<String> labels = Stream.of(GraphType.values()).map(g -> g.getLabel()).collect(Collectors.toList());
        ButtonSelected buttonSelected = new ButtonSelected();
        int y = 5;
        for (String label : labels) {
            NavigationButton nb = new NavigationButton(label);
            pnNavButtons.add(nb);
            NavigationButton b = nb;
            
            b.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    b.toFront();
                    buttonSelected.originalY = b.getLayoutY();
                    buttonSelected.nextY = b.getLayoutY() - mouseEvent.getSceneY();
                }
            });
            b.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    b.setLayoutY(mouseEvent.getSceneY() + buttonSelected.nextY);
                }
            });
            b.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double distanceMoved = Math.abs(buttonSelected.originalY - b.getLayoutY());
                    // If the user has dragged the button past the halfway point of a button
                    // boundary
                    if (distanceMoved > BUTTON_HEIGHT / 2) {
                        String buttonBeingMovedLabel = nb.getLabel();
                        int indexOfButtonBeingMoved = labels.indexOf(buttonBeingMovedLabel);
                        int indexFurther = (int) Math.floor(distanceMoved / (BUTTON_HEIGHT - 1));
                        int indexToReplace;
                        // Figuring out which direction the user is dragging. If this is true, the user
                        // is dragging downwards
                        if (buttonSelected.originalY - b.getLayoutY() < 0) {
                            indexToReplace = indexOfButtonBeingMoved + indexFurther;
                            if (indexToReplace > labels.size() - 1) {
                                // If the user drags beyond the list, replace the last button
                                indexToReplace = labels.size() - 1;
                            }
                        } else {
                            indexToReplace = indexOfButtonBeingMoved - indexFurther;
                            if (indexToReplace < 0) {
                                indexToReplace = 0; // If the user drags above the list, replace the first button
                            }
                        }
                        String btnBeingReplaced = labels.get(indexToReplace);
                        for (NavigationButton bt : pnNavButtons) {
                            if (bt.getLabel().equals(btnBeingReplaced)) {
                                b.setLayoutY(bt.getLayoutY());
                                bt.setLayoutY(buttonSelected.originalY);
                            }
                        }
                        // Swap the two butons
                        Collections.swap(labels, indexOfButtonBeingMoved, indexToReplace);
                        reorderGraphs(labels);
                    } else {
                        // If the user barely drags the button (by mistake), then put it back
                        b.setLayoutY(buttonSelected.originalY);
                    }
                }
            });
        }

    }

    public NavigationButton[] getPnNavButtons() {
        return pnNavButtons.toArray(new NavigationButton[pnNavButtons.size()]);
    }

    /**
     * Reorders the graphs on the center panel.
     * @param labels The labels we are comparing the graphs to.
     */
    private void reorderGraphs(List<String> labels) {
        GraphController gc = GraphController.getInstance();
        // List<RocketGraph> originalGraphs = graphView.getGraphs();
        List<RocketGraph> updatedGraphs = new ArrayList<>(gc.getAllGraphs());
        for (int i = 0; i < labels.size(); i++) {
            for (int j = 0; j < updatedGraphs.size(); j++) {
                if (updatedGraphs.get(j).getGraphType().getLabel().equals(labels.get(i))) {
                    RocketGraph temp = updatedGraphs.get(j);
                    updatedGraphs.remove(j);
                    updatedGraphs.add(i, temp);
                }
            }
        }

        gc.setGraphs(updatedGraphs);
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

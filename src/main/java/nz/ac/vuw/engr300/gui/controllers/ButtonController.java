package nz.ac.vuw.engr300.gui.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import nz.ac.vuw.engr300.gui.components.NavigationButton;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.GraphView;
import nz.ac.vuw.engr300.gui.views.NavigationView;

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

    private static final ButtonController controllerInstance = new ButtonController();
    private NavigationView view;

    /**
     * Private constructor to prevent initialization outside singleton.
     */
    private ButtonController() {

    }

    /**
     * Get the instance of the button controller.
     *
     * @return ButtonControler singleton instance for the application.
     */
    public static ButtonController getInstance() {
        return controllerInstance;
    }

    /**
     * Updates the buttons and sets them to the appropriate graph.
     */
    public void updateButtons() {
        pnNavButtons.clear();
        List<String> labels = GraphMasterList.getInstance().getGraphs().stream()
                .map(GraphType::getLabel).collect(Collectors.toList());
        //ButtonSelected buttonSelected = new ButtonSelected();
        int y = 5;
        for (String label : labels) {
            NavigationButton nb = new NavigationButton(label);
            pnNavButtons.add(nb);
        }

        // Draw the buttons on the view.
        view.drawButtons();
    }


    /**
     * Get all the Navigation buttons within this controller to be displayed.
     *
     * @return Array of Navigation Buttons to be displayed.
     */
    public NavigationButton[] getPnNavButtons() {
        return pnNavButtons.toArray(new NavigationButton[pnNavButtons.size()]);
    }

    /**
     * Attach a view to this controller for updating the contents during changes.
     *
     * @param view Attached view to be updated on controller updates.
     */
    public void attachView(NavigationView view) {
        this.view = view;
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

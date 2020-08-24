package nz.ac.vuw.engr300.gui.controllers;

import nz.ac.vuw.engr300.gui.components.NavigationButton;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to handle the buttons that are linked to the graph on the left side panel.
 *
 *
 * @author Ahad Rahman
 */
public class ButtonController {

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
        for (String label : labels) {
            NavigationButton nb = new NavigationButton(label);
            pnNavButtons.add(nb);
        }
        // Draw the buttons on the view.
        view.drawButtons();
    }

    /**
     * Reorder the buttons based on the up and down arrow configuration buttons.
     * @param buttonToMove The name of the button to be moved.
     * @param moveUp True indicates the buttonToMove is moving upwards.
     */
    public void reorderButtons(String buttonToMove, boolean moveUp) {
        List<String> labels = GraphMasterList.getInstance().getGraphs().stream()
                .map(GraphType::getLabel).collect(Collectors.toList());
        int indexOfGraphBeingMoved = labels.indexOf(buttonToMove);
        int indexToSwap = moveUp ? indexOfGraphBeingMoved - 1 : indexOfGraphBeingMoved + 1;
        if (indexToSwap == -1) {
            indexToSwap = labels.size() - 1;
        } else if (indexToSwap == labels.size()) {
            indexToSwap = 0;
        }
        GraphMasterList.getInstance().swapGraphs(indexOfGraphBeingMoved, indexToSwap);
        updateButtons();
        GraphController.getInstance().syncGraphOrder();
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

}

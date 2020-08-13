package nz.ac.vuw.engr300.gui.components;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import nz.ac.vuw.engr300.gui.controllers.ButtonController;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.model.GraphType;

/**
 * NavigationButton is a wrapper class to allow for drawing multiple buttons within our Navigation sidebar.
 * This allows us to create our highlight graph buttons, and hide/show buttons for the graphs.
 *
 * @author Nathan Duckett
 */
public class NavigationButton extends GridPane {
    /**
     * Define the visible state of the show/hide button.
     */
    private static final String VISIBLE_STATE = "V";
    /**
     * Define the hidden state of the show/hide button.
     */
    private static final String HIDDEN_STATE = "H";


    private final GraphController graphController;
    private final ButtonController buttonController;
    private final Button graphButton;
    private final Button hideButton;
    private final Button moveUpButton;
    private final Button moveDownButton;

    /**
     * Create a new NavigationButton instance based on the label provided.
     *
     * @param label Label which corresponds to a GraphType contained in our graphs.
     */
    public NavigationButton(String label) {
        super();
        // Create and initialize fields
        this.graphButton = new Button(label);
        this.hideButton = new Button(VISIBLE_STATE);
        this.moveUpButton = new Button("Up");
        this.moveDownButton = new Button("Down");
        this.graphController = GraphController.getInstance();
        this.buttonController = ButtonController.getInstance();

        // Configure graphButton to match spec
        graphButton.setId("btn" + label.replace(" ", ""));
        graphButton.setOnAction(e -> {
            GraphType thisGraph = GraphType.fromLabel(label);
            this.graphController.highlight(thisGraph);
        });
        // Set max width to ensure sizes are all equal.
        graphButton.setMaxWidth(Double.POSITIVE_INFINITY);

        // Configure hide button information.
        this.hideButton.setOnAction(e -> {
            graphController.hideGraph(label);
            if (graphController.getGraph(label).isGraphVisible()) {
                hideButton.setText(VISIBLE_STATE);
            } else {
                hideButton.setText(HIDDEN_STATE);
            }
        });

        this.moveUpButton.setOnAction(e -> {
            buttonController.reorderButtons(this.graphButton.getText(), true);
        });

        this.moveDownButton.setOnAction(e -> {
            buttonController.reorderButtons(this.graphButton.getText(), false);
        });

        configureConstraints();
        this.add(graphButton, 0, 0);
        this.add(hideButton, 1, 0);
        this.add(moveUpButton, 2, 0);
        this.add(moveDownButton, 3, 0);
    }
    
    public String getLabel() {
        return this.graphButton.getText();
    }

    /**
     * Configure the internal column constraints to display the buttons.
     */
    private void configureConstraints() {
        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(100);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(40);
        this.getColumnConstraints().add(left);
        this.getColumnConstraints().add(right);
    }
}

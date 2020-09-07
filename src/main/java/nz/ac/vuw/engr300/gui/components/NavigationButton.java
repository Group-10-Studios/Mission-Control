package nz.ac.vuw.engr300.gui.components;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import nz.ac.vuw.engr300.gui.controllers.ButtonController;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.model.GraphType;

/**
 * NavigationButton is a wrapper class to allow for drawing multiple buttons within our Navigation sidebar.
 * This allows us to create our highlight graph buttons, and hide/show buttons for the graphs.
 *
 * @author Nathan Duckett, Ahad Rahman
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

        this.moveUpButton = new Button();
        this.moveUpButton.setGraphic(new ImageView("file:src/main/resources/icons/up_arrow.png"));

        this.moveDownButton = new Button();
        this.moveDownButton.setGraphic(new ImageView("file:src/main/resources/icons/down_arrow.png"));

        this.graphController = GraphController.getInstance();
        this.buttonController = ButtonController.getInstance();

        // Configure graphButton to match spec
        graphButton.setId(buttonLabel(label, ""));
        graphButton.setOnAction(e -> {
            this.graphController.highlight(label);
        });
        // Set max width to ensure sizes are all equal.
        graphButton.setMaxWidth(Double.POSITIVE_INFINITY);

        // Configure hide button information.
        hideButton.setId(buttonLabel(label, "Vis"));
        this.hideButton.setOnAction(e -> {
            graphController.hideGraph(label);
            if (graphController.getGraphByGraphType(label).isGraphVisible()) {
                hideButton.setText(VISIBLE_STATE);
            } else {
                hideButton.setText(HIDDEN_STATE);
            }
        });

        this.moveUpButton.setId(buttonLabel(label, "Up"));
        this.moveUpButton.setOnAction(e -> {
            buttonController.reorderButtons(this.graphButton.getText(), true);
        });

        this.moveDownButton.setId(buttonLabel(label, "Down"));
        this.moveDownButton.setOnAction(e -> {
            buttonController.reorderButtons(this.graphButton.getText(), false);
        });


        configureConstraints();
        this.add(graphButton, 0, 0);
        this.add(hideButton, 1, 0);
        this.add(moveUpButton, 2, 0);
        this.add(moveDownButton, 3, 0);

    }

    /**
     * Build a button label with the label and a prefix for a specific type of button.
     *
     * @param label Label to put in the button, this should be based on the GraphType label value.
     * @param prefix Prefix to append to the button ID to represent its' type e.g. Up, Down or Vis.
     * @return String containing a constructed Button ID.
     */
    private String buttonLabel(String label, String prefix) {
        return "btn" + prefix + label.replace(" ", "");
    }

    /**
     * Configure the internal column constraints to display the buttons.
     */
    private void configureConstraints() {
        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(80);
        ColumnConstraints extraButtonConstraints = new ColumnConstraints();
        extraButtonConstraints.setPercentWidth(30);
        this.getColumnConstraints().add(left);
        this.getColumnConstraints().add(extraButtonConstraints);
        this.getColumnConstraints().add(extraButtonConstraints);
        this.getColumnConstraints().add(extraButtonConstraints);
    }
}

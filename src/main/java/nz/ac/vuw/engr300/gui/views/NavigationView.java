package nz.ac.vuw.engr300.gui.views;

import javafx.scene.layout.GridPane;

/**
 * Represents the left panel, which displays navigation controls.
 *
 * @author Tim Salisbury
 */
public class NavigationView implements View {

    private final GridPane root;

    public NavigationView(GridPane root) {
        this.root = root;
    }
}

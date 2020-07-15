package nz.ac.vuw.engr300.gui.views;

import javafx.scene.layout.GridPane;

/**
 * Represents the right panel, which displays general information.
 *
 * @author Tim Salisbury
 */
public class InformationView implements View {
    private GridPane root;

    public InformationView(GridPane root) {
        this.root = root;
    }
}

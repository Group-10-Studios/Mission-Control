package nz.ac.vuw.engr300.gui.views;

import javafx.scene.layout.GridPane;

/**
 * Represents the center panel, which displays graphs.
 *
 * @author Tim Salisbury
 */
public class GraphView implements View {
    private GridPane root;

    public GraphView(GridPane root) {
        this.root = root;
    }
}

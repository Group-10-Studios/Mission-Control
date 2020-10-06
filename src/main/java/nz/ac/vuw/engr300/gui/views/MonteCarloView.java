package nz.ac.vuw.engr300.gui.views;

import javafx.scene.layout.GridPane;
import nz.ac.vuw.engr300.communications.importers.MonteCarloImporter;
import nz.ac.vuw.engr300.gui.util.UiUtil;

public class MonteCarloView implements View {

    private final GridPane root;
    private final MonteCarloImporter monteCarloImporter;


    public MonteCarloView(GridPane root, MonteCarloImporter monteCarloImporter) {
        this.root = root;
        this.monteCarloImporter = monteCarloImporter;

        UiUtil.addPercentRows(root, 50, 50);
    }

    public void initialize() {

    }
}

package nz.ac.vuw.engr300.gui.views;

import javafx.scene.layout.GridPane;
import nz.ac.vuw.engr300.communications.importers.MonteCarloImporter;
import nz.ac.vuw.engr300.gui.components.RocketDataHistogram;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import java.util.stream.Collectors;

/**
 * Represents the Monte Carlo tab.
 *
 * @author Tim Salisbury
 * @author Ahad Rahman
 * @author Joshua Harwood
 */
public class MonteCarloView implements View {

    private final GridPane root;
    private final MonteCarloImporter monteCarloImporter;


    /**
     *
     * @param root                  The root tab to populate.
     * @param monteCarloImporter    The monte carlo importer.
     */
    public MonteCarloView(GridPane root, MonteCarloImporter monteCarloImporter) {
        this.root = root;
        this.monteCarloImporter = monteCarloImporter;

        UiUtil.addPercentRows(root, 50, 50);
    }

    /**
     * Initalizes the monte carlo view.
     */
    public void initialize() {
        root.getChildren().clear();
        RocketDataHistogram altitudeHistogram = new RocketDataHistogram(
                new GraphType("Monte Carlo Max Altitude", "histogram"),
                monteCarloImporter.getTable().getColumnData("Max Altitude").stream().mapToDouble(object
                        -> (Double)object).boxed().collect(Collectors.toList()));

        RocketDataHistogram groundHitSpeedHistogram = new RocketDataHistogram(
                new GraphType("Monte Carlo Ground Hit Velocity", "histogram"),
                monteCarloImporter.getTable().getColumnData("Ground Hit Velocity").stream().mapToDouble(object
                        -> (Double)object).boxed().collect(Collectors.toList()));

        root.add(altitudeHistogram, 0, 0);
        root.add(groundHitSpeedHistogram, 0, 1);
    }
}

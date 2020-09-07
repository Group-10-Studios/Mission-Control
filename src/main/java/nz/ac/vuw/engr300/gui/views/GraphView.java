package nz.ac.vuw.engr300.gui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import nz.ac.vuw.engr300.communications.importers.CsvConfiguration;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.exceptions.TomTomRequestFailedException;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataLocation;
import nz.ac.vuw.engr300.gui.components.RocketDataAngleLineChart;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.controllers.ButtonController;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.layouts.DynamicGridPane;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import org.apache.log4j.Logger;

/**
 * Represents the center panel, which displays graphs.
 *
 * @author Tim Salisbury
 * @author Nathan Duckett
 */
public class GraphView implements View {

    private static final Logger LOGGER = Logger.getLogger(GraphView.class);

    private final GridPane root;
    private final DynamicGridPane contentPane;
    private List<RocketGraph> graphs;
    private final GraphController controller;
    /**
     * Define the table name to match graphs to within config. This defaults to the value below.
     */
    private String tableName = "incoming-avionics";

    /**
     * Create new GraphView.
     * 
     * @param root Root GridPane to add content inside.
     */
    public GraphView(GridPane root) {
        this.root = root;
        createGraphs();
        this.contentPane = new DynamicGridPane(allGraphs());
        // Set internal gridPane ID for dynamic GUI testing.
        this.contentPane.setId("gridPane");
        this.controller = GraphController.getInstance();

        attachContentToScrollPane();
        this.controller.attachView(this);
        this.controller.setGraphs(graphs);
        this.controller.subscribeGraphs(tableName, false);
    }
    
    /**
     * Attach the contentPane within a scroll pane.
     */
    public void attachContentToScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.contentPane);
        // Required to prevent horizontal scrolling and ensure it grows with width of root.
        scrollPane.setFitToWidth(true);
        UiUtil.addNodeToGrid(scrollPane, root, 0, 0);

        // Bind scrollPane visible height to the contentPane row size
        // Updates row constraints based on the visible height to fill the correct space.
        scrollPane.heightProperty().addListener((ov, n, t1) -> {
            this.contentPane.updateConstraints(t1.intValue());
            // Ensure contents are updated with new constraints
            this.contentPane.updateContents();
        });
        this.contentPane.updateConstraints((int) scrollPane.getHeight());
    }

    /**
     * Get all of the RocketGraph's within this view.
     *
     * @return Unmodifiable list view of the rocket graphs.
     */
    public List<RocketGraph> getGraphs() {
        return Collections.unmodifiableList(this.graphs);
    }

    /**
     * Update the graph contents to the provided list of graphs in this list.
     *
     * @param graphs List of rocket graphs
     */
    public void updateGraphs(List<RocketGraph> graphs) {
        this.graphs = graphs;
        this.contentPane.clearGridContents();
        this.contentPane.addGridContents(allGraphs());
    }

    /**
     * Update the graph definition to match the provided tableName from within the configuration file.
     *
     * @param newTableName Table name to refer against for the graph structure.
     * @param isSimulation Boolean flag to indicate whether this table is built for simulation mode or serial mode.
     */
    public void updateGraphStructureDefinition(String newTableName, boolean isSimulation) {
        this.tableName = newTableName;
        // Clear all registered graphs
        GraphMasterList.getInstance().clearRegisteredGraphs();
        this.controller.resetObservers();
        // Create graphs from description
        createGraphs();
        // Reload dynamic Grid pane contents
        this.contentPane.clearGridContents();
        this.contentPane.addGridContents(allGraphs());
        // Update controller with new graphs and subscribe to data
        this.controller.setGraphs(graphs);
        this.controller.subscribeGraphs(tableName, isSimulation);
        ButtonController.getInstance().updateButtons();
    }

    /**
     * Manually binds the graph type to the graphs. This could maybe be automated
     * later but for now can set the values.
     */
    private void createGraphs() {
        this.graphs = new ArrayList<>();
        CsvTableDefinition tableDefinition = CsvConfiguration.getInstance().getTable(tableName);
        GraphMasterList masterList = GraphMasterList.getInstance();
        for (String headerName : tableDefinition.getTitles()) {
            CsvTableDefinition.Column column = tableDefinition.getColumn(headerName);
            if (column.getGraphType() == null) {
                continue;
            }
            switch (column.getGraphType()) {
                case "angle": {
                    GraphType gt = new GraphType(column.getName());
                    masterList.registerGraph(gt);
                    this.graphs.add(new RocketDataAngleLineChart(
                            "Time (s)",
                            column.getName() + " (" + column.getDataUnit() + ")",
                            false,
                            gt
                    ));
                    break;
                }
                case "line": {
                    GraphType gt = new GraphType(column.getName());
                    masterList.registerGraph(gt);
                    this.graphs.add(new RocketDataLineChart(
                            "Time (s)",
                            column.getName() + " (" + column.getDataUnit() + ")",
                            gt
                    ));
                    break;
                }
                default: {
                    // Do nothing as no valid graph was specified.
                    break;
                }
            }
        }
        RocketDataLocation rl = new RocketDataLocation(-41.229428, 174.800303, 400, 400, new GraphType("Rocket Location"));
        rl.updateAngleDistanceInfo(-41.229402, 174.800724);
        this.graphs.add(rl);

    }
    
    /**
     * Get all the graphs within the GraphView as a Region array format to allow manipulation.
     *
     * @return Region array of graphs.
     */
    private Region[] allGraphs() {
        return this.graphs.stream().filter(RocketGraph::isGraphVisible).map(g -> (Region) g).toArray(Region[]::new);
    }

    /**
     * Get the currently equipped table name for incoming data.
     *
     * @return String table name this data is mapped against.
     */
    public String getTableName() {
        return this.tableName;
    }
}

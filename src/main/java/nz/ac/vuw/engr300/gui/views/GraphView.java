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
import nz.ac.vuw.engr300.gui.components.*;
import nz.ac.vuw.engr300.gui.controllers.ButtonController;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.layouts.DynamicGridPane;
import nz.ac.vuw.engr300.gui.model.BatteryMasterList;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import nz.ac.vuw.engr300.model.LaunchParameters;
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
        this.controller.subscribeGraphs(tableName, GraphController.SubscriptionType.SERIAL);
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
     * @param graphType SubscriptionType of the graphs to know what kind of data they subscribe to.
     */
    public void updateGraphStructureDefinition(String newTableName, GraphController.SubscriptionType graphType) {
        this.tableName = newTableName;
        // Clear all registered graphs
        GraphMasterList.getInstance().clearRegisteredGraphs();
        // Clear any external graphs (NOTE: Must be done here to prevent errors of non-existent graphs)
        GraphMasterList.getInstance().clearUnregisteredGraphs();
        this.controller.resetObservers();
        // Create graphs from description
        createGraphs();
        // Reload dynamic Grid pane contents
        this.contentPane.clearGridContents();
        this.contentPane.addGridContents(allGraphs());
        // Update controller with new graphs and subscribe to data
        this.controller.setGraphs(graphs);
        this.controller.subscribeGraphs(tableName, graphType);
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

        boolean seenOtherMapDefinition = false;

        for (String headerName : tableDefinition.getTitles()) {
            CsvTableDefinition.Column column = tableDefinition.getColumn(headerName);
            if (column.getGraphType() == null) {
                // Check if we have the other corresponding data field then add a map
                if (seenOtherMapDefinition && (column.getName().equals("lat") || column.getName().equals("long"))) {
                    GraphType gt = new GraphType("Location", "map");
                    masterList.registerGraph(gt);
                    // Create a new RocketDataLocation graph - Uses the current lat/long from LaunchParameters
                    // Defaulting to image width/height of 400 as we don't specify this elsewhere and is default.
                    this.graphs.add(new RocketDataLocation(
                            LaunchParameters.getInstance().getLatitude().getValue(),
                            LaunchParameters.getInstance().getLongitude().getValue(),
                            400, 400,
                            gt
                    ));
                } else if (column.getName().equals("lat") || column.getName().equals("long")) {
                    // Mark true that we have seen the other field
                    seenOtherMapDefinition = true;
                }

                continue;
            }
            switch (column.getGraphType()) {
                case "angle": {
                    GraphType gt = new GraphType(column.getName(), column.getGraphType());
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
                    GraphType gt = new GraphType(column.getName(), column.getGraphType());
                    masterList.registerGraph(gt);
                    this.graphs.add(new RocketDataLineChart(
                            "Time (s)",
                            column.getName() + " (" + column.getDataUnit() + ")",
                            gt
                    ));
                    break;
                }
                case "battery": {
                    // Similar to the previous graphs this will register itself to the battery master list.
                    // This list stores the actual batteries not just references and handles data setting
                    BatteryMasterList.getInstance().registerBattery(new RocketBattery(column.getName(),
                            column.getDataUnit()));
                    break;
                }
                case "histogram": {
                    GraphType gt = new GraphType(column.getName(), column.getGraphType());
                    masterList.registerGraph(gt);
                    this.graphs.add(new RocketDataHistogram(
                            gt,
                            new ArrayList<>()
                    ));
                    break;
                }
                default: {
                    // Do nothing as no valid graph was specified.
                    break;
                }
            }
        }
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

    /**
     * Update the currently equipped table name for incoming data.
     *
     * @param tableName String table name this data is mapped against.
     */
    public void updateTableName(String tableName) {
        this.tableName = tableName;
    }
}

package nz.ac.vuw.engr300.gui.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nz.ac.vuw.engr300.communications.importers.CsvConfiguration;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.importers.PastFlightImporter;
import nz.ac.vuw.engr300.communications.importers.SerialCommunications;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.communications.model.RocketData;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataAngleLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataLocation;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.BatteryMasterList;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.GraphView;
import nz.ac.vuw.engr300.gui.views.View;
import nz.ac.vuw.engr300.model.LaunchParameters;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the GraphController for interacting with the graph components for their required actions.
 * All functionality the user can have will be created in here to distinguish from the view code.
 *
 * @author Nathan Duckett
 */
public class GraphController {
    /**
     * Define the SubscriptionType this GraphController will listen to.
     */
    public enum SubscriptionType {
        SERIAL, SIMULATION, PAST_FLIGHT
    }

    private static final Logger LOGGER = Logger.getLogger(GraphController.class);
    private final OpenRocketImporter simulationImporter = new OpenRocketImporter();
    private final PastFlightImporter pastFlightImporter = new PastFlightImporter();
    private final SerialCommunications serialCommunications = new SerialCommunications();
    private static final GraphController instance = new GraphController();
    private final List<Stage> extractedGraphs = new ArrayList<>();

    private List<RocketGraph> graphs;
    private List<RocketGraph> allGraphs;
    private View view;
    private String highlightedGraphLabel;
    private SubscriptionType simulationMode = SubscriptionType.SERIAL;

    /**
     * Private constructor to prevent Graph controller being created outside of in here.
     */
    private GraphController() {
        this.allGraphs = new ArrayList<>();
    }

    /**
     * Get the GraphController instance.
     *
     * @return GraphController instance for the app.
     */
    public static GraphController getInstance() {
        return instance;
    }

    /**
     * Set the graph list stored within this controller which corresponds to the list in the view.
     *
     * @param graphs List of RocketGraphs to be displayed to the user.
     */
    public void setGraphs(List<RocketGraph> graphs) {
        this.graphs = graphs;
        // Makes assumption that view is type GraphView. This can be fixed later but for now keeping generic
        // for expandability if required later.
        GraphView gv = (GraphView) view;
        gv.updateGraphs(graphs);
        // Must first alias the unregisteredGraphs before clearing all graphs.
        List<RocketGraph> unregisteredGraphs = getGraphsBasedOnMasterList(GraphMasterList.getInstance()
                .getUnregisteredGraphs());

        // Clear the current all graphs and add the graphs back in their correct order.
        allGraphs.clear();
        allGraphs.addAll(graphs);
        allGraphs.addAll(unregisteredGraphs);
    }

    /**
     * Makes sure the graphs in GraphMasterList syncs its changes with graph list in the view.
     */
    public void syncGraphOrder() {
        setGraphs(getGraphsBasedOnMasterList(GraphMasterList.getInstance().getGraphs()));
    }

    /**
     * Get the graphs from the instance (RocketGraph contents) based on a list of GraphTypes from the master list.
     * This will create a list of references to these graphs, in the order matching the GraphType retrieved from
     * the masterListCopy. This can be used to reorder the graphs, or alias their copies to keep within allGraphs.
     *
     * @param masterListCopy An extracted list of GraphTypes from the Master list. This contains an ordered list to
     *                      retrieve the specific GraphType's corresponding graph.
     * @return List of RocketGraph objects in the order of the provided GraphType's.
     */
    private List<RocketGraph> getGraphsBasedOnMasterList(List<GraphType> masterListCopy) {
        List<RocketGraph> listOfGraphs = new ArrayList<>();
        for (GraphType g : masterListCopy) {
            listOfGraphs.add(getGraphByGraphType(g.getLabel()));
        }

        return listOfGraphs;
    }

    /**
     * Attach a view to this controller specifically for calling update view when a change has been made.
     *
     * @param view View to be attached to this controller.
     */
    public void attachView(View view) {
        this.view = view;
    }

    /**
     * Hide or show a specific graph based on its' label.
     *
     * @param label The label of the graph to be updated to be either visible or hidden.
     */
    public void hideGraph(String label) {
        RocketGraph graph = getGraphByGraphType(label);
        if (graph == null) {
            throw new RuntimeException("An invalid graph was selected with the label <"
                    + label + "> which doesn't exist.");
        }
        graph.toggleVisibility();

        // Makes assumption that view is type GraphView. This can be fixed later but for now keeping generic
        // for expandability if required later.
        GraphView gv = (GraphView) view;
        gv.updateGraphs(graphs);
    }

    /**
     * Subscribe all of the graphs to their appropriate data sources from the Simulation Listener.
     *
     * @param tableName String definition name from the communications.json file to match against incoming data.
     * @param type The SubscriptionType to be used for this graph structure.
     */
    public void subscribeGraphs(String tableName, SubscriptionType type) {
        this.simulationMode = type;
        // Must reset extracted graphs before subscribing new graphs.
        resetExtractedGraphs();
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable(tableName);
        if (type == SubscriptionType.SIMULATION) {
            simulationImporter.subscribeObserver(this::graphSimulationSubscription);
        } else if (type == SubscriptionType.SERIAL) {
            // Ensure both graphs are subscribed and battery listeners.
            serialCommunications.subscribeObserver(data -> {
                graphSubscription(data, table);
                BatteryMasterList.getInstance().updateBatteryValue(data, table);
            });
        } else if (type == SubscriptionType.PAST_FLIGHT) {
            // Ensure both graphs are subscribed and battery listeners.
            pastFlightImporter.subscribeObserver(data -> {
                graphSubscription(data, table);
                BatteryMasterList.getInstance().updateBatteryValue(data, table);
            });
        }
        LOGGER.debug("All graphs have been subscribed for " + type);
    }

    /**
     * Handle translating the incoming {@code List<Object>} data which is coming in from the serial observer.
     *
     * @param data Incoming data from the serialCommunications observer to be drawn on the graphs.
     * @param table The CSV table definition to match the data against.
     */
    private void graphSubscription(List<Object> data, CsvTableDefinition table) {
        long timestamp = table.matchValueToColumn(data.get(table.getCsvIndexOf("timestamp")),
                "timestamp", Long.class);
        for (RocketGraph rg: this.allGraphs) {
            if (rg instanceof RocketDataAngleLineChart) {
                RocketDataAngleLineChart rgC = (RocketDataAngleLineChart) rg;
                String dataType = rgC.getGraphType().getLabel();
                int index = table.getCsvIndexOf(dataType);
                if (index < 0) {
                    continue;
                }
                double value = table.matchValueToColumn(data.get(index), dataType, Double.class);
                rgC.addValue(timestamp, value);
            } else if (rg instanceof RocketDataLineChart) {
                RocketDataLineChart rgC = (RocketDataLineChart) rg;
                String dataType = rgC.getGraphType().getLabel();
                int index = table.getCsvIndexOf(dataType);
                if (index < 0) {
                    continue;
                }
                double value = table.matchValueToColumn(data.get(index), dataType, Double.class);
                rgC.addValue(timestamp, value);
            } else if (rg instanceof RocketDataLocation) {
                RocketDataLocation rgL = (RocketDataLocation) rg;
                // Runs under the assumption that columns are specifically named this.
                int indexLat = table.getCsvIndexOf("lat");
                int indexLong = table.getCsvIndexOf("long");

                // Skip if values don't exist on the table.
                if (indexLat < 0 || indexLong < 0) {
                    continue;
                }
                double latitude = table.matchValueToColumn(data.get(indexLat), "lat", Double.class);
                double longitude = table.matchValueToColumn(data.get(indexLong), "long", Double.class);

                rgL.updateAngleDistanceInfo(latitude, longitude);
            }
        }
    }

    /**
     * Handle translating the incoming RocketData from the simulation listener and drawing it on the graphs.
     *
     * @param data Incoming data from the simulationListener observer to be drawn on the graphs.
     */
    private void graphSimulationSubscription(RocketData data) {
        if (data instanceof RocketStatus) {
            double timestamp = data.getTime();
            for (RocketGraph rg: this.allGraphs) {
                if (rg instanceof RocketDataAngleLineChart) {
                    RocketDataAngleLineChart rgC = (RocketDataAngleLineChart) rg;
                    String dataType = rgC.getGraphType().getLabel();
                    double value = getDataFromRocketData((RocketStatus) data, dataType);
                    rgC.addValue(timestamp, value);
                } else if (rg instanceof RocketDataLineChart) {
                    RocketDataLineChart rgC = (RocketDataLineChart) rg;
                    String dataType = rgC.getGraphType().getLabel();
                    double value = getDataFromRocketData((RocketStatus) data, dataType);
                    rgC.addValue(timestamp, value);
                }
            }
        }
    }

    /**
     * Helper function to retrieve the values from the RocketStatus based on the name of the graph.
     * This is performed through reflection to ensure that each graph can access its' data correctly
     * while being dynamically loaded into the application.
     *
     * @param data Incoming RocketStatus data object which contains the values we want to retrieve from.
     * @param dataType The String name of the graph we need the data type from which will be converted.
     * @return Double value stored within the RocketStatus for the dataType.
     */
    private double getDataFromRocketData(RocketStatus data, String dataType) {
        try {
            String functionLabel = "get" + dataType.replace(" ", "");
            Method getMethod = RocketStatus.class.getDeclaredMethod(functionLabel);
            return (double) getMethod.invoke(data);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Start listening to the serial device.
     */
    public void startListeningOnSerial() {
        serialCommunications.startListening();
    }

    /**
     * Set the wind angle graph to the specified wind angle.
     *
     * @param currentWindAngle Double value of the Wind Angle to be set on the RocketGraph.
     */
    public void setWindAngle(double currentWindAngle) {
        // getAngleByGraphType(GraphType.WINDDIRECTION).setAngle(currentWindAngle);
        LOGGER.debug("The wind angle has been updated to: " + currentWindAngle);
    }

    /**
     * Highlight the specified graphType provided. If this graph type is already highlighted it will unhighlight it.
     * This also assumes only one graph can be highlighted at a time.
     *
     * @param graphTypeLabel The specific label for the GraphType which we want highlighted.
     */
    public void highlight(String graphTypeLabel) {
        for (RocketGraph graph : this.graphs) {
            Region chart = (Region) graph;
            if (graph.getGraphType().getLabel().equals(graphTypeLabel)) {
                if (graphTypeLabel.equals(this.highlightedGraphLabel)) {
                    chart.setBorder(null);
                    this.highlightedGraphLabel = null;

                } else {
                    chart.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID,
                            new CornerRadii(5.0), new BorderWidths(2.0))));
                    this.highlightedGraphLabel = graphTypeLabel;
                }

            } else {
                chart.setBorder(null);
            }
        }
    }

    /**
     * Find a graph by its' graph type. This will O(n) search through the graphs list to find a graph which matches
     * the type provided. If this is not found it will return null.
     *
     * @param label GraphType to match against inside RocketGraphs.
     * @return A RocketGraph which matches the expected type.
     */
    public RocketGraph getGraphByGraphType(String label) {
        for (RocketGraph g : allGraphs) {
            if (g.getGraphType().getLabel().equals(label)) {
                return g;
            }
        }

        return null;
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataLineChart.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param label GraphType to match against inside RocketGraphs.
     * @return A RocketDataLineChart which matches the expected type.
     */
    private RocketDataLineChart getLineChartByGraphType(String label) {
        return (RocketDataLineChart) getGraphByGraphType(label);
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataAngle.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param label GraphType to match against inside RocketGraphs.
     * @return A RocketDataAngle which matches the expected type.
     */
    private RocketDataAngle getAngleByGraphType(String label) {
        return (RocketDataAngle) getGraphByGraphType(label);
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataAngleLineChart.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param label GraphType to match against inside RocketGraphs.
     * @return A RocketDataAngleLineChart which matches the expected type.
     */
    private RocketDataAngleLineChart getAngleLineChartByGraphType(String label) {
        return (RocketDataAngleLineChart) getGraphByGraphType(label);
    }

    /**
     * Callback function for run simulation in main view, this function will open a
     * file dialog to select a simulation data file. It will then load it into the
     * data importer and run the simulation as if it was live.
     */
    public void runSim() {
        simulationImporter.stop();
        pastFlightImporter.stop();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a simulation to run.");
        fileChooser.setInitialDirectory(new File("src/main/resources/"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                simulationImporter.importData(selectedFile.getAbsolutePath());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.DECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Failed to import simulation data!");
                alert.setContentText(e.getMessage());

                alert.showAndWait();
                return;
            }
            // Reconstruct graphs if they are not already in simulation mode
            if (this.simulationMode != SubscriptionType.SIMULATION) {
                String tableName = "previousSimulation";
                ((GraphView) view).updateGraphStructureDefinition(tableName, SubscriptionType.SIMULATION);
            }

            allGraphs.forEach(RocketGraph::clear);
            simulationImporter.start();
        }

        LOGGER.debug("Simulation started");
    }

    /**
     * Callback function for past flights in main view, this function will open a
     * file dialog to select a past flight log file. It will then load it into the
     * data importer and run the simulation as if it was live.
     */
    public void runPastFlights() {
        simulationImporter.stop();
        pastFlightImporter.stop();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a simulation to run.");
        fileChooser.setInitialDirectory(new File("src/main/resources/"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                pastFlightImporter.importData(selectedFile.getAbsolutePath());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.DECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Failed to import PastFlight log data!");
                alert.setContentText(e.getMessage());

                alert.showAndWait();
                return;
            }
            allGraphs.forEach(RocketGraph::clear);

            ((GraphView) view).updateGraphStructureDefinition(pastFlightImporter.getTableName(),
                    SubscriptionType.PAST_FLIGHT);
            pastFlightImporter.start();
        }

        LOGGER.debug("Past flight started");
    }

    /**
     * Update the map location to the new value within the LaunchParameters. Deletes the old
     * map from the graphs to update.
     */
    public void updateMapLocation() {
        GraphType gt = new GraphType("Location", "map");
        // Must unregister first in case location is popped out
        GraphMasterList.getInstance().unRegisterGraph(gt);
        GraphMasterList.getInstance().registerGraph(gt);
        // Create a new RocketDataLocation graph - Uses the current lat/long from LaunchParameters
        // Defaulting to image width/height of 400 as we don't specify this elsewhere and is default.
        RocketDataLocation newGraph = new RocketDataLocation(
                LaunchParameters.getInstance().getLatitude().getValue(),
                LaunchParameters.getInstance().getLongitude().getValue(),
                400, 400,
                gt
        );

        int oldPos = 0;
        // Remove the old graph
        for (int i = 0; i < this.graphs.size(); i++) {
            if (this.graphs.get(i).getGraphType().getLabel().equals("Location")) {
                this.graphs.remove(i);
                oldPos = i;
                break;
            }
        }

        this.graphs.add(oldPos, newGraph);
        setGraphs(this.graphs);
    }

    /**
     * Callback for when the cross at top right gets pressed, this function should
     * be used to cleanup any resources and close any ongoing threads.
     */
    public void shutdown() {
        simulationImporter.stop();
        pastFlightImporter.stop();
        LOGGER.debug("GraphController shutdown called");
        simulationImporter.unsubscribeAllObservers();
        serialCommunications.stopListening();
        serialCommunications.unsubscribeAllObservers();
        // Close all extra windows to finish the application.
        resetExtractedGraphs();
    }

    /**
     * Get the Simulation Importer for this application.
     *
     * @return OpenRocketImporter which runs the simulation for the application.
     */
    public OpenRocketImporter getSimulationImporter() {
        return this.simulationImporter;
    }

    /**
     * Get the serial communications listener for this application.
     *
     * @return SerialCommunications instance which is listening for this application.
     */
    public SerialCommunications getSerialCommunications() {
        return this.serialCommunications;
    }

    /**
     * Reset the observers on the incoming data streams to be reset/loaded on new definition.
     */
    public void resetObservers() {
        this.serialCommunications.unsubscribeAllObservers();

        //TODO: Check if we need to unsubscribe simulation listeners here.
    }

    /**
     * Create a pop out graph view with the provided graph.
     *
     * @param graph Graph to display in a pop out view.
     */
    public void popOutGraph(RocketGraph graph) {
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.NONE);
        popupWindow.setTitle(graph.getGraphType().getLabel());
        GraphMasterList.getInstance().unRegisterGraph(graph.getGraphType());
        syncGraphsPopUp();

        // Ensure the graph position is reset to zero for the pop up window to prevent down shifting
        // If this is not done it will match the y position from the dynamic grid pane when clicked.
        Region graphRegion = (Region) graph;
        graphRegion.setLayoutX(0);
        graphRegion.setLayoutY(0);
        Scene scene = new Scene(graphRegion, 300, 300);

        popupWindow.setScene(scene);

        extractedGraphs.add(popupWindow);
        LOGGER.info("Extracting pop out window for <" + graph.getGraphType().getLabel() + ">");
        LOGGER.info("Total pop out windows is now <" + extractedGraphs.size() + ">");

        // Ensure on pop up close that the graph is added back into the main application.
        popupWindow.setOnCloseRequest((event) -> {
            GraphMasterList.getInstance().registerGraph(graph.getGraphType());
            this.graphs.add(graph);
            syncGraphsPopUp();
        });

        // Must be show not showAndWait to keep the application running
        popupWindow.show();
    }

    /**
     * Sync the graphs for a pop up. Based on the function popOutGraph with specification to sync the master list
     * with a new value within the graphs.
     */
    private void syncGraphsPopUp() {
        this.syncGraphOrder();
        this.setGraphs(graphs);
        ButtonController.getInstance().updateButtons();
    }

    /**
     * Reset the extracted graphs, clearing the list and closing all opened windows.
     */
    private void resetExtractedGraphs() {
        LOGGER.info("Closing extracted graphs - Unregistering external graphs");
        this.extractedGraphs.forEach(Stage::close);
        this.extractedGraphs.clear();
    }
}

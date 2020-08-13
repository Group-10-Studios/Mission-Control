package nz.ac.vuw.engr300.gui.controllers;

import javafx.scene.control.Alert;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataAngleLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.GraphView;
import nz.ac.vuw.engr300.gui.views.View;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * Represents the GraphController for interacting with the graph components for their required actions.
 * All functionality the user can have will be created in here to distinguish from the view code.
 *
 * @author Nathan Duckett
 */
public class GraphController {

    private static final Logger LOGGER = Logger.getLogger(GraphController.class);
    private final OpenRocketImporter simulationImporter = new OpenRocketImporter();
    private static final GraphController instance = new GraphController();

    private List<RocketGraph> graphs;
    private View view;
    private GraphType highlightedGraph;

    /**
     * Private constructor to prevent weather controller being created outside of in here.
     */
    private GraphController() {

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
        RocketGraph graph = getGraphByGraphType(GraphType.fromLabel(label));
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
     */
    public void subscribeGraphs() {
        simulationImporter.subscribeObserver((data) -> {
            if (data instanceof RocketStatus) {
                getLineChartByGraphType(GraphType.ALTITUDE)
                        .addValue(data.getTime(), ((RocketStatus) data).getAltitude());
                getLineChartByGraphType(GraphType.TOTAL_ACCELERATION)
                        .addValue(data.getTime(), ((RocketStatus) data).getTotalAcceleration());
                getLineChartByGraphType(GraphType.TOTAL_VELOCITY)
                        .addValue(data.getTime(), ((RocketStatus) data).getTotalVelocity());

                getLineChartByGraphType(GraphType.Y_ACCELERATION)
                        .addValue(data.getTime(), ((RocketStatus) data).getAccelerationY());
                getLineChartByGraphType(GraphType.Y_VELOCITY)
                        .addValue(data.getTime(), ((RocketStatus) data).getVelocityY());

                getLineChartByGraphType(GraphType.Z_ACCELERATION)
                        .addValue(data.getTime(), ((RocketStatus) data).getAccelerationZ());
                getLineChartByGraphType(GraphType.Z_VELOCITY)
                        .addValue(data.getTime(), ((RocketStatus) data).getVelocityZ());

                getAngleLineChartByGraphType(GraphType.YAW_RATE)
                        .addValue(data.getTime(), ((RocketStatus) data).getYawRate());
                getAngleLineChartByGraphType(GraphType.PITCH_RATE)
                        .addValue(data.getTime(), ((RocketStatus) data).getPitchRate());
                getAngleLineChartByGraphType(GraphType.ROLL_RATE)
                        .addValue(data.getTime(), ((RocketStatus) data).getRollRate());
            }
        });
        LOGGER.debug("All graphs have been subscribed");
    }

    /**
     * Set the wind angle graph to the specified wind angle.
     *
     * @param currentWindAngle Double value of the Wind Angle to be set on the RocketGraph.
     */
    public void setWindAngle(double currentWindAngle) {
        getAngleByGraphType(GraphType.WINDDIRECTION).setAngle(currentWindAngle);
        LOGGER.debug("The wind angle has been updated to: " + currentWindAngle);
    }

    /**
     * Get a specific graph by its' label.
     *
     * @param label String label of the graph we want to retrieve.
     * @return The RocketGraph instance of the graph if it exists inside graphs otherwise it will return null.
     */
    public RocketGraph getGraph(String label) {
        return getGraphByGraphType(GraphType.fromLabel(label));
    }
    
    /**
     * Get all the graphs within the controller.
     * 
     * @return List of RocketGraphs within the controller.
     */
    public List<RocketGraph> getAllGraphs() {
        return this.graphs;
    }

    /**
     * Highlight the specified graphType provided. If this graph type is already highlighted it will unhighlight it.
     * This also assumes only one graph can be highlighted at a time.
     *
     * @param graphType The specific GraphType which we want highlighted.
     */
    public void highlight(GraphType graphType) {
        for (RocketGraph graph : this.graphs) {
            Region chart = (Region) graph;
            if (graph.getGraphType() == graphType) {
                if (graphType == this.highlightedGraph) {
                    chart.setBorder(null);
                    this.highlightedGraph = null;

                } else {
                    chart.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID,
                            new CornerRadii(5.0), new BorderWidths(2.0))));
                    this.highlightedGraph = graphType;
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
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketGraph which matches the expected type.
     */
    private RocketGraph getGraphByGraphType(GraphType type) {
        for (RocketGraph g : graphs) {
            if (g.getGraphType() == type) {
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
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketDataLineChart which matches the expected type.
     */
    private RocketDataLineChart getLineChartByGraphType(GraphType type) {
        return (RocketDataLineChart) getGraphByGraphType(type);
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataAngle.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketDataAngle which matches the expected type.
     */
    private RocketDataAngle getAngleByGraphType(GraphType type) {
        return (RocketDataAngle) getGraphByGraphType(type);
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataAngleLineChart.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketDataAngleLineChart which matches the expected type.
     */
    private RocketDataAngleLineChart getAngleLineChartByGraphType(GraphType type) {
        return (RocketDataAngleLineChart) getGraphByGraphType(type);
    }

    /**
     * Callback function for run simulation in main view, this function will open a
     * file dialog to select a simulation data file. It will then load it into the
     * data importer and run the simulation as if it was live.
     */
    public void runSim() {
        simulationImporter.stop();

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
            graphs.forEach(RocketGraph::clear);
            simulationImporter.start();
        }

        LOGGER.debug("Simulation started");
    }

    /**
     * Callback for when the cross at top right gets pressed, this function should
     * be used to cleanup any resources and close any ongoing threads.
     */
    public void shutdown() {
        simulationImporter.stop();
        LOGGER.debug("GraphController shutdown called");
        simulationImporter.unsubscribeAllObservers();
    }

    /**
     * Get the Simulation Importer for this application.
     *
     * @return OpenRocketImporter which runs the simulation for the application.
     */
    public OpenRocketImporter getSimulationImporter() {
        return this.simulationImporter;
    }
}

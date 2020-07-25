package nz.ac.vuw.engr300.gui.controllers;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataAngleLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;
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

    /**
     * Subscribe all of the graphs to their appropriate data sources from the Simulation Listener.
     *
     * @param graphs List of RocketGraph's present on the screen to subscribe to data.
     */
    public void subscribeGraphs(List<RocketGraph> graphs) {
        simulationImporter.subscribeObserver((data) -> {
            if (data instanceof RocketStatus) {
                getLineChartByGraphType(graphs, GraphType.ALTITUDE)
                        .addValue(data.getTime(), ((RocketStatus) data).getAltitude());
                getLineChartByGraphType(graphs, GraphType.TOTAL_ACCELERATION)
                        .addValue(data.getTime(), ((RocketStatus) data).getTotalAcceleration());
                getLineChartByGraphType(graphs, GraphType.TOTAL_VELOCITY)
                        .addValue(data.getTime(), ((RocketStatus) data).getTotalVelocity());

                getLineChartByGraphType(graphs, GraphType.Y_ACCELERATION)
                        .addValue(data.getTime(), ((RocketStatus) data).getAccelerationY());
                getLineChartByGraphType(graphs, GraphType.Y_VELOCITY)
                        .addValue(data.getTime(), ((RocketStatus) data).getVelocityY());

                getLineChartByGraphType(graphs, GraphType.Z_ACCELERATION)
                        .addValue(data.getTime(), ((RocketStatus) data).getAccelerationZ());
                getLineChartByGraphType(graphs, GraphType.Z_VELOCITY)
                        .addValue(data.getTime(), ((RocketStatus) data).getVelocityZ());

                getAngleLineChartByGraphType(graphs, GraphType.YAW_RATE)
                        .addValue(data.getTime(), ((RocketStatus) data).getYawRate());
                getAngleLineChartByGraphType(graphs, GraphType.PITCH_RATE)
                        .addValue(data.getTime(), ((RocketStatus) data).getPitchRate());
                getAngleLineChartByGraphType(graphs, GraphType.ROLL_RATE)
                        .addValue(data.getTime(), ((RocketStatus) data).getRollRate());
            }
        });
        LOGGER.debug("All graphs have been subscribed");
    }

    /**
     * Set the wind angle graph to the specified wind angle.
     *
     * @param graphs List of RocketGraph's to search for the wind angle to update.
     * @param currentWindAngle Double value of the Wind Angle to be set on the RocketGraph.
     */
    public void setWindAngle(List<RocketGraph> graphs, double currentWindAngle) {
        getAngleByGraphType(graphs, GraphType.WINDDIRECTION).setAngle(currentWindAngle);
        LOGGER.debug("The wind angle has been updated to: " + currentWindAngle);
    }

    /**
     * Find a graph by its' graph type. This will O(n) search through the graphs list to find a graph which matches
     * the type provided. If this is not found it will return null.
     *
     * @param graphs List of RocketGraph's to search for the specified type of graph.
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketGraph which matches the expected type.
     */
    private RocketGraph getGraphByGraphType(List<RocketGraph> graphs, GraphType type) {
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
     * @param graphs List of RocketGraph's to search for the specified type of graph.
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketDataLineChart which matches the expected type.
     */
    private RocketDataLineChart getLineChartByGraphType(List<RocketGraph> graphs, GraphType type) {
        return (RocketDataLineChart) getGraphByGraphType(graphs, type);
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataAngle.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param graphs List of RocketGraph's to search for the specified type of graph.
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketDataAngle which matches the expected type.
     */
    private RocketDataAngle getAngleByGraphType(List<RocketGraph> graphs, GraphType type) {
        return (RocketDataAngle) getGraphByGraphType(graphs, type);
    }

    /**
     * Function interface to getGraphByGraphType which returns an UNCHECKED conversion to a RocketDataAngleLineChart.
     * This is not checked and therefore could result in exceptions if used on a RocketGraph which doesn't match
     * the expected type.
     *
     * @param graphs List of RocketGraph's to search for the specified type of graph.
     * @param type GraphType to match against inside RocketGraphs.
     * @return A RocketDataAngleLineChart which matches the expected type.
     */
    private RocketDataAngleLineChart getAngleLineChartByGraphType(List<RocketGraph> graphs, GraphType type) {
        return (RocketDataAngleLineChart) getGraphByGraphType(graphs, type);
    }

    /**
     * Callback function for run simulation in main view, this function will open a
     * file dialog to select a simulation data file. It will then load it into the
     * data importer and run the simulation as if it was live.
     */
    public void runSim(List<RocketGraph> graphs) {
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
    }
}

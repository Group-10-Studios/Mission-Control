package nz.ac.vuw.engr300.gui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataLocation;
import nz.ac.vuw.engr300.gui.components.RocketDataAngleLineChart;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.layouts.DynamicGridPane;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.UiUtil;

/**
 * Represents the center panel, which displays graphs.
 *
 * @author Tim Salisbury
 * @author Nathan Duckett
 */
public class GraphView implements View {
    private final GridPane root;
    private final DynamicGridPane contentPane;
    private List<RocketGraph> graphs;
    private final GraphController controller;

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
        this.controller.subscribeGraphs();
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
     * Manually binds the graph type to the graphs. This could maybe be automated
     * later but for now can set the values.
     */
    private void createGraphs() {
        this.graphs = new ArrayList<>();
        
        this.graphs.add(new RocketDataLineChart("Time (s)", "Velocity (m/s)",
                        GraphType.TOTAL_VELOCITY));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Velocity (m/s)",
                        GraphType.X_VELOCITY));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Velocity (m/s)",
                        GraphType.Y_VELOCITY));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Velocity (m/s)",
                        GraphType.Z_VELOCITY));

        this.graphs.add(new RocketDataLineChart("Time (s)",
                        "Acceleration ( m/s² )", GraphType.TOTAL_ACCELERATION));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Acceleration (m/s²)",
                        GraphType.X_ACCELERATION));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Acceleration (m/s²)",
                        GraphType.Y_ACCELERATION));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Acceleration (m/s²)",
                        GraphType.Z_ACCELERATION));

        this.graphs.add(new RocketDataLineChart("Time (s)", "Altitude (m)",
                        GraphType.ALTITUDE));
        this.graphs.add(new RocketDataAngleLineChart("Time (s)", "Yaw Rate (°/s)",
                false, GraphType.YAW_RATE));
        this.graphs.add(new RocketDataAngleLineChart("Time (s)", "Pitch Rate (°/s)",
                false, GraphType.PITCH_RATE));
        this.graphs.add(new RocketDataAngleLineChart("Time (s)", "Roll Rate (°/s)",
                false, GraphType.ROLL_RATE));

        this.graphs.add(new RocketDataAngle(true, GraphType.WINDDIRECTION));
        
        RocketDataLocation rdl = new RocketDataLocation(-41.227938, 174.798772, 400, 400,
                        GraphType.ROCKET_LOCATION);
        rdl.updateAngleDistanceInfo(-41.227776, 174.799334);
        this.graphs.add(rdl);
    }
    
    /**
     * Get all the graphs within the GraphView as a Region array format to allow manipulation.
     *
     * @return Region array of graphs.
     */
    private Region[] allGraphs() {
        return this.graphs.stream().filter(RocketGraph::isGraphVisible).map(g -> (Region) g).toArray(Region[]::new);
    }
}

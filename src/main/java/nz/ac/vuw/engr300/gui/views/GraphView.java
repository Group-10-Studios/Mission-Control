package nz.ac.vuw.engr300.gui.views;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.gui.components.RocketBattery;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataLocation;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.UiUtil;

/**
 * Represents the center panel, which displays graphs.
 *
 * @author Tim Salisbury
 * @author Nathan Duckett
 */
public class GraphView implements View {
    private GridPane root;
    private List<RocketGraph> graphs;

    private final OpenRocketImporter simulationImporter = new OpenRocketImporter();

    /**
     * Create new GraphView.
     * 
     * @param root Root GridPane to add content inside.
     */
    public GraphView(GridPane root) {
        this.root = root;
        
        createGraphs();
        buildGraphTilePane();
    }
    
    /**
     * Build the Graph tile pane from the graphs stored inside the view.
     */
    public void buildGraphTilePane() {
        TilePane tile = new TilePane();
        tile.setMaxHeight(2180);
        tile.setMaxWidth(3840);
        tile.setPrefColumns(4);
        tile.setPrefRows(3);
        
        tile.setPrefTileWidth(250);
        tile.setPrefTileHeight(250); 
        for (Region g : allGraphs()) {
            g.setMinWidth(100);
            g.setMinHeight(100);
            g.setMaxHeight(2180);
            g.setMaxWidth(3840);
            tile.getChildren().add(g);
        }
        tile.setBackground(new Background(new BackgroundFill(Color.RED,
                        CornerRadii.EMPTY, Insets.EMPTY)));
        
        UiUtil.addNodeToGrid(tile, root, 0, 0);
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
                        "Acceleration ( M/S² )", GraphType.TOTAL_ACCELERATION));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Acceleration (m/s²)",
                        GraphType.X_ACCELERATION));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Acceleration (m/s²)",
                        GraphType.Y_ACCELERATION));
        this.graphs.add(new RocketDataLineChart("Time (s)", "Acceleration (m/s²)",
                        GraphType.Z_ACCELERATION));

        this.graphs.add(new RocketDataLineChart("Time (s)", "Altitude (m)",
                        GraphType.ALTITUDE));
        this.graphs.add(new RocketDataAngle(false, GraphType.YAW_RATE));
        this.graphs.add(new RocketDataAngle(false, GraphType.PITCH_RATE));
        this.graphs.add(new RocketDataAngle(false, GraphType.ROLL_RATE));

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
        return this.graphs.stream().map(g -> (Region) g).toArray(Region[]::new);
    }
}

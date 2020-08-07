package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;
import static nz.ac.vuw.engr300.gui.util.UiUtil.addPercentColumns;
import static nz.ac.vuw.engr300.gui.util.UiUtil.addPercentRows;
import static nz.ac.vuw.engr300.gui.util.UiUtil.addPixelHeightRows;
import static nz.ac.vuw.engr300.gui.util.UiUtil.createGridPane;

/**
 * Represents a Home Screen view which the application will default to.
 *
 * @author Nathan Duckett
 * @author Tim Salisbury
 */
public class HomeView implements View {

    private final Scene applicationScene;

    private final GridPane root;

    private NavigationView navigationView;
    private InformationView informationView;
    private GraphView graphView;

    /**
     * Create a new HomeView and attach it to the stage.
     *
     * @param stage Root application stage to have panels attached to.
     */
    public HomeView(Stage stage) {

        root = new GridPane();
        this.applicationScene = new Scene(root);
        stage.setScene(this.applicationScene);

        initializeGrid();
        stage.setWidth(1280.0);
        stage.setHeight(720.0);

        stage.show();

        stage.setMinHeight(720);
        stage.setMinWidth(1280);

        //HomeController controller = loader.getController();
        //stage.setOnCloseRequest(e -> controller.shutdown());
        // stage.setMaximized(true);

        stage.show();
        // Set minimum dimensions to 720p - Doesn't support below this
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
    }

    /**
     * Initializes the main root grid.
     */
    private void initializeGrid() {
        // 50 pixel first row for banner
        addPercentRows(root, 5);
        // second row takes up 100% of the remaining space
        addPercentRows(root, 95);
        // One column at 100% width
        addPercentColumns(root, 100);

        Label title = new Label("Mission Control");
        title.setFont(new Font("Arial", 22));
        title.setTextFill(Color.WHITE);

        GridPane contentGrid = new GridPane();
        // Only need one row as content will fill the entire vertical space
        addPercentRows(contentGrid, 100);

        // Need three columns, 20% column for nav, 60% for main content, 20% for warnings
        addPercentColumns(contentGrid, 20, 60, 20);


        addNodeToGrid(title, root, 0, 0, Pos.CENTER, Colours.PRIMARY_COLOUR, Insets.EMPTY);

        addNodeToGrid(contentGrid, root, 1, 0);

        setupCenterPanel(contentGrid);
        setupRightHandSidePanel(contentGrid);
        setupLeftHandSidePanel(contentGrid);

        // Assert they are created - mainly to bypass Spot bugs issues.
        assert this.navigationView != null;
        assert this.graphView != null;
        assert this.informationView != null;
    }

    /**
     * Initializes the center panel by creating a GridPane that holds the content and then constructs a graph view.
     *
     * @param contentGrid The GridPae that will contain the center panel.
     */
    private void setupCenterPanel(GridPane contentGrid) {
        GridPane centerPanel = createGridPane(0, 0, Insets.EMPTY);
        addNodeToGrid(centerPanel, contentGrid, 0, 1, Insets.EMPTY);
        UiUtil.addPercentRows(centerPanel, 100);
        UiUtil.addPercentColumns(centerPanel, 100);

        this.graphView = new GraphView(centerPanel);
    }

    /**
     * Initializes the right hand side panel (Information Panel), this is done by creating a GridPane that is
     * passed to a InformationView object in the constructor.
     *
     * @param contentGrid The content GridPane that will contain the right handside panel.
     */
    private void setupRightHandSidePanel(GridPane contentGrid) {
        GridPane rightHandSidePanel = createGridPane(0, 10, new Insets(10));
        addNodeToGrid(rightHandSidePanel, contentGrid, 0, 2, Insets.EMPTY);

        this.informationView = new InformationView(rightHandSidePanel);
    }

    /**
     * Initializes the left hand side panel (Navigation Panel), this is done by creating a GridPane that is passed to a
     * NavigationView object in the constructor.
     *
     * @param contentGrid The content GridPane that will contain the left handside panel.
     */
    private void setupLeftHandSidePanel(GridPane contentGrid) {
        GridPane leftHandSidePanel = createGridPane(10, 10, new Insets(10));
        addNodeToGrid(leftHandSidePanel, contentGrid, 0, 0, Insets.EMPTY);

        this.navigationView = new NavigationView(leftHandSidePanel, graphView);
    }

    /**
     * Get the internal application scene height.
     *
     * @return Double representation of the scene height.
     */
    public double getSceneHeight() {
        return this.applicationScene.getHeight();
    }

    /**
     * Get the internal application scene width.
     *
     * @return Double representation of the scene width.
     */
    public double getSceneWidth() {
        return this.applicationScene.getWidth();
    }
}

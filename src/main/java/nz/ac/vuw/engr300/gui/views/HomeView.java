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

        // Need three columns, 1/6 column for nav, 2/3 for main content, 1/6 for warnings
        double sidePanelPercent = (1d / 6d) * 100d;
        double centerPanelPercent = (2d / 3d) * 100d;
        addPercentColumns(contentGrid, sidePanelPercent, centerPanelPercent, sidePanelPercent);


        addNodeToGrid(title, root, 0, 0, Pos.CENTER, Colours.PRIMARY_COLOUR, Insets.EMPTY);
        // NOTE:: This assumes that it is the only child added to root by this point to set ID.
        // If this is changed DynamicGuiTests will break.
        root.getChildren().get(0).setId("pnBanner");

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
        centerPanel.setId("centerPanel");
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
        rightHandSidePanel.setId("informationView");
        addNodeToGrid(rightHandSidePanel, contentGrid, 0, 2, Insets.EMPTY);

        this.informationView = new InformationView(rightHandSidePanel);

        //********** CODE LEFT FOR REFERENCE - DELETE WHEN DONE **********

        //// One column, 100 percent of width
        //addPercentColumns(rightHandSidePanel, 100);
        //// 20 for batteries, 60 for warnings, 20 for go/no go
        //addPercentRows(rightHandSidePanel, 20, 60, 20);
        //
        //// Add Batteries and warning sections of right hand side panel
        //addNodeToGrid(new Label("BATTERIES"), rightHandSidePanel, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
        //addNodeToGrid(new Label("WARNINGS"), rightHandSidePanel, 1, 0, Pos.CENTER, Color.MAGENTA, Insets.EMPTY);
        //
        //// Create and populate go no go at bottom of right hand side
        //VBox goNoGoVBox = createMinimumVerticalSizeVBox(5, new Insets(10), new Button("Btn0"), new Button("Btn1"));
        //// Literally just for setting background colour
        //goNoGoVBox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
        // CornerRadii.EMPTY, Insets.EMPTY)));
        //
        //// Set it to hug the warnings above it
        //GridPane.setValignment(goNoGoVBox, VPos.TOP);
        //addNodeToGrid(goNoGoVBox, rightHandSidePanel, 2, 0, Insets.EMPTY);
    }

    /**
     * Initializes the left hand side panel (Navigation Panel), this is done by creating a GridPane that is passed to a
     * NavigationView object in the constructor.
     *
     * @param contentGrid The content GridPane that will contain the left handside panel.
     */
    private void setupLeftHandSidePanel(GridPane contentGrid) {
        GridPane leftHandSidePanel = createGridPane(10, 10, new Insets(10));
        leftHandSidePanel.setId("navigationView");
        addNodeToGrid(leftHandSidePanel, contentGrid, 0, 0, Insets.EMPTY);

        this.navigationView = new NavigationView(leftHandSidePanel, graphView);

        //********** CODE LEFT FOR REFERENCE - DELETE WHEN DONE **********
        //// One column, 100 percent of width
        //addPercentColumns(leftHandSidePanel, 100);
        //// 20 for weather, 60 for nav buttons, 20 for run sim
        //addPercentRows(leftHandSidePanel, 20, 60, 20);
        //
        //// Add weather to lefthand side GridPane
        //addNodeToGrid(new Label("WEATHER"), leftHandSidePanel, 0, 0, Pos.CENTER, Color.ORANGE, Insets.EMPTY);
        //
        //// Create and populate left hand side nav buttons
        //VBox buttonVbox = UIUtil.createStandardVBox(5, new Insets(10, 10, 5, 10),
        //        IntStream.range(0, 10).mapToObj(i -> new Button("Btn" + i)).toArray(Region[]::new));
        //// Literally just for setting background colour
        //buttonVbox.setBackground(new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)));
        //addNodeToGrid(buttonVbox, leftHandSidePanel, 1, 0, Insets.EMPTY);
        //
        //// Create and populate runSim buttons at bottom of left hand side panel
        //VBox runSimVbos = createMinimumVerticalSizeVBox(5, new Insets(10), new Button("Btn0"), new Button("Btn1"));
        //
        //// Literally just for setting background colour
        //runSimVbos.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW,
        // CornerRadii.EMPTY, Insets.EMPTY)));
        //
        //// Set it to hug the nav buttons above it
        //GridPane.setValignment(runSimVbos, VPos.TOP);
        //addNodeToGrid(runSimVbos, leftHandSidePanel, 2, 0, Insets.EMPTY);
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

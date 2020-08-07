package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import javafx.scene.control.Button;

import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import nz.ac.vuw.engr300.gui.components.RocketBattery;
import nz.ac.vuw.engr300.gui.controllers.InformationController;
import nz.ac.vuw.engr300.gui.controllers.WarningsController;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;

/**
 * Represents the right panel, which displays general information.
 *
 * @author Tim Salisbury, Nalin Aswani, Jake Mai, Nathan Duckett
 */
public class InformationView implements View {
    private final GridPane root;

    public RocketBattery primaryBattery = new RocketBattery();
    public RocketBattery secondaryBattery = new RocketBattery();

    public WarningsController warnC;
    public InformationController infController;

    public Button goButton = new Button("  Go   ");

    public Button noGoButton = new Button("No Go");

    /**
     * Add Batteries and warning sections of right hand side panel.
     * @param root The root Gridpane where we will be adding nodes to.
     */
    public InformationView(GridPane root) {
        this.goButton.setId("btnGo");
        this.noGoButton.setId("btnNoGo");
        this.root = root;
        setupRoot();
        setupBatteries();
        setupWarnings();
        setupGoNoGo();
    }

    /**
     * Create new Warnings pane on the right hand side root panel.
     */
    private void setupWarnings() {
        Pane pnWarnings = new Pane();
        pnWarnings.setId("pnWarnings");
        addNodeToGrid(pnWarnings, root, 1, 0);
        // For the warnings controller
        infController = new InformationController(pnWarnings);
        infController.subscribeToSimulation();
    }

    /**
     * Create new Batteries Level pane on the top right hand side root panel.
     */
    private void setupBatteries() {
        // Create and populate batteries in a HBox
        HBox batteryHBox = UiUtil.createMinimumHorizontalSizeHBox(5, new Insets(10), primaryBattery, secondaryBattery);
        addNodeToGrid(batteryHBox, root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
    }

    /**
     * Create Go/NoGo button at the bottom of the right hand side root panel using VBox.
     */
    private void setupGoNoGo() {
        goButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
        noGoButton.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
                CornerRadii.EMPTY, Insets.EMPTY)));

        goButton.setOnAction(e -> infController.onGo(e));
        noGoButton.setOnAction(e -> infController.onNoGo(e));

        // Create and populate go no go at bottom of right hand side
        VBox goNoGoVBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10),
                goButton, noGoButton);
        // Literally just for setting background colour
        goNoGoVBox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Set it to hug the warnings above it
        GridPane.setValignment(goNoGoVBox, VPos.TOP);
        addNodeToGrid(goNoGoVBox, root, 2, 0, Insets.EMPTY);
    }

    /**
     * Set up the root panel on the right hand side which wraps around other panes.
     */
    private void setupRoot() {
        assert this.root != null;

        this.root.setBackground(new Background(new BackgroundFill(Colours.SHADE_COLOUR,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // One column, 100 percent of width
        UiUtil.addPercentColumns(this.root, 100);
        // 20 for batteries, 60 for warnings, 20 for go/no go
        UiUtil.addPercentRows(this.root, 20, 60, 20);
    }

}

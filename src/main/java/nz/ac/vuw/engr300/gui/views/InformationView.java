package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
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
import nz.ac.vuw.engr300.gui.model.BatteryMasterList;
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
    private HBox batteryHBox;

    public Label goIndicator = new Label("Not safe to launch"); // (warnings/errors exist)
    public Label armIndicator = new Label("Disarmed");

    public WarningsController warnC;
    public InformationController infController;

    public Button armButton = new Button("  Arm   ");

    public Button launchConfigButton = new Button("Launch Config");

    /**
     * Add Batteries and warning sections of right hand side panel.
     * @param root The root Gridpane where we will be adding nodes to.
     */
    public InformationView(GridPane root) {
        this.armButton.setId("btnArm");
        this.root = root;
        setupRoot();
        setupBatteries();
        setupWarnings();
        setupBottomButtons();
        setupIndicator();
    }

    /**
     * Sets up the indicators in the top right of the screen.
     * Shows if we are in Go or No go state (safe or unsafe to launch).
     */
    private void setupIndicator() {
        VBox indicatorBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10), goIndicator, armIndicator);
        addNodeToGrid(indicatorBox, root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
        if (infController.hasWarningsOrErrors() == false) {
            goIndicator.setText("No errors exist! (Safe to launch)");
        }
    }

    /**
     * Create new Warnings pane on the right hand side root panel.
     */
    private void setupWarnings() {
        Pane pnWarnings = new Pane();
        pnWarnings.setId("pnWarnings");
        addNodeToGrid(pnWarnings, root, 2, 0);
        // For the warnings controller
        infController = new InformationController(pnWarnings);
        infController.subscribeToSimulation();
    }

    /**
     * Create new Batteries Level pane on the top right hand side root panel.
     */
    public void setupBatteries() {
        root.getChildren().remove(batteryHBox);

        // Create and populate batteries in a HBox
        batteryHBox = UiUtil.createMinimumHorizontalSizeHBox(5, new Insets(10),
                BatteryMasterList.getInstance().allBatteries());
        addNodeToGrid(batteryHBox, root, 1, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
    }

    /**
     * Create Go/NoGo button at the bottom of the right hand side root panel using VBox.
     */
    private void setupBottomButtons() {
        armButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));

        launchConfigButton.setBackground(new Background(new BackgroundFill(Color.YELLOW,
                CornerRadii.EMPTY, Insets.EMPTY)));

        armButton.setOnAction(e -> infController.onArmDisarm(e, armIndicator, armButton));

        launchConfigButton.setOnAction(e -> LaunchParameterView.display((parameters -> {
            infController.saveLaunchParameters(parameters);
        })));

        launchConfigButton.setId("launchConfig");

        // Create and populate go no go at bottom of right hand side
        VBox goNoGoVBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10),
                armButton, launchConfigButton);
        // Literally just for setting background colour
        goNoGoVBox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Set it to hug the warnings above it
        GridPane.setValignment(goNoGoVBox, VPos.TOP);
        addNodeToGrid(goNoGoVBox, root, 3, 0, Insets.EMPTY);
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
        UiUtil.addPercentRows(this.root, 10, 20, 50, 20);
    }

}

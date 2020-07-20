package nz.ac.vuw.engr300.gui.views;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.gui.components.RocketBattery;
import nz.ac.vuw.engr300.gui.controllers.InformationController;
import nz.ac.vuw.engr300.gui.controllers.WarningsController;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import java.util.Optional;

import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;

/**
 * Represents the right panel, which displays general information.
 *
 * @author Tim Salisbury, Nalin Aswani, Jake Mai
 */
public class InformationView implements View {
    private GridPane root;

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
        setupRoot(root);
//        runBatteryThread();
        setupBatteries();
        setupWarnings();
        setupGoNoGo();
    }

    private void setupWarnings() {
        Pane pnWarnings = new Pane();
//        addNodeToGrid(new Label("WARNINGS"), root, 1, 0, Pos.CENTER, Color.MAGENTA, Insets.EMPTY);
        addNodeToGrid(pnWarnings, root, 1, 0, Pos.CENTER, Color.MAGENTA, Insets.EMPTY);
        // For the warnings controller
        infController = new InformationController(pnWarnings);

//        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Something");
//        warnC.checkAllData(weatherToGive);
    }

    private void setupBatteries() {
        // Create and populate batteries in a HBox
        HBox batteryHBox = UiUtil.createMinimumHorizontalSizeHBox(5, new Insets(10), primaryBattery, secondaryBattery);
        addNodeToGrid(batteryHBox, root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
    }

    private void setupGoNoGo() {
        goButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
        noGoButton.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
                CornerRadii.EMPTY, Insets.EMPTY)));

//        goButton.setOnAction(this::onGo);
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

    private void setupRoot(GridPane root) {
        this.root = root;

        // One column, 100 percent of width
        UiUtil.addPercentColumns(this.root, 100);
        // 20 for batteries, 60 for warnings, 20 for go/no go
        UiUtil.addPercentRows(this.root, 20, 60, 20);
    }

}

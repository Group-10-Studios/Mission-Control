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

    /**
     * Separate thread to run the battery timers on.
     */
    private Thread batteryThread;

    public RocketBattery primaryBattery = new RocketBattery();
    public RocketBattery secondaryBattery = new RocketBattery();

    public WarningsController warnC;

    /**
     * Add Batteries and warning sections of right hand side panel.
     * @param root The root Gridpane where we will be adding nodes to.
     */
    public InformationView(GridPane root) {
        setupRoot(root);
        runBatteryThread();
        setupBatteries();
        setupWarnings();
        setupGoNoGo();
    }

    private void runBatteryThread() {
        this.batteryThread = new Thread(() -> {
            double b1Level = 100.0;
            double b2Level = 100.0;
            secondaryBattery.setBatteryLevel(b2Level);
            while (b1Level >= 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Error while updating primaryBattery percentage", e);
                }
                primaryBattery.setBatteryLevel(b1Level);
                b1Level -= 1.0;
            }
            while (b2Level >= 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Error while updating primaryBattery percentage", e);
                }
                secondaryBattery.setBatteryLevel(b2Level);
                b2Level -= 1.0;
            }
        });
        this.batteryThread.start();
    }

    private void setupWarnings() {
        Pane pnWarnings = new Pane();
//        addNodeToGrid(new Label("WARNINGS"), root, 1, 0, Pos.CENTER, Color.MAGENTA, Insets.EMPTY);
        addNodeToGrid(pnWarnings, root, 1, 0, Pos.CENTER, Color.MAGENTA, Insets.EMPTY);
        // For the warnings controller
        warnC = new WarningsController(pnWarnings);
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Can't go, errors exist!");

//        warnC.checkAllData(weatherToGive);
    }

    private void setupBatteries() {
        // Create and populate batteries in a HBox
        HBox batteryHBox = UiUtil.createMinimumHorizontalSizeHBox(5, new Insets(10), primaryBattery, secondaryBattery);
        addNodeToGrid(batteryHBox, root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
    }

    private void setupGoNoGo() {

        // Create and populate go no go at bottom of right hand side
        VBox goNoGoVBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10), new Button("Btn0"), new Button("Btn1"));
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

    /**
     * Basic callback for when clicking on the Go button.
     *
     * @param actionEvent   The action event representing the event.
     */
    private void onGo(ActionEvent actionEvent) {
        if (warnC.hasErrors()) { // If errors, do not go
            warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Can't go, errors exist!");
            return;
        } else if (warnC.hasWarnings()) { // If warnings, give a prompt, ask them to click go again.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Warnings exist");
            alert.setContentText("Are you ok with running a simulation?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return;
            }
        }
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Go Button Pressed",
                "Waiting for rocket to be armed", "(Pretending its armed)");
//        lbState.setText("Go State");
//        runSim();
    }

    /**
     * Basic callback function for when clicking the No Go button.
     *
     * @param actionEvent   The action event representing the event.
     */
    private void onNoGo(ActionEvent actionEvent) {
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "No Go Button Pressed");
//        lbState.setText("No Go State");
    }

}

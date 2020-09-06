package nz.ac.vuw.engr300.gui.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nz.ac.vuw.engr300.communications.model.RocketEvent;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.gui.components.RocketBattery;
import nz.ac.vuw.engr300.importers.JsonExporter;
import nz.ac.vuw.engr300.model.LaunchParameters;

import java.util.Optional;

public class InformationController {
    /**
     * Separate thread to run the battery timers on.
     */
    private Thread batteryThread;
    private WarningsController warnC;

    /**
     * Constructor for the Information controller. Creates a warnings controller, gets the weather for the warnings
     * controller, then checks the weather if it is safe to launch.
     * @param pnWarnings The pane needed for the warnings controller.
     */
    public InformationController(Pane pnWarnings) {
        warnC = new WarningsController(pnWarnings);
        warnC.setDataForWarnings();
        warnC.checkAllData();
    }

    private void runBatteryThread(RocketBattery primaryBattery, RocketBattery secondaryBattery) {
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

    /**
     * Callback for when the cross at top right gets pressed, this function should
     * be used to cleanup any resources and close any ongoing threads.
     */
    public void shutdown() {
        // simulationImporter.stop();
        this.batteryThread.interrupt();
    }

    /**
     * Basic callback function for when clicking the No Go button.
     *
     * @param actionEvent   The action event representing the event.
     * @param armIndicator  The label we want to change.
     */
    public void onDisarm(ActionEvent actionEvent, Label armIndicator) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Disarm button pressed");
        alert.setContentText("Are you sure you want to disarm the rocket?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        armIndicator.setText("Disarmed");
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Disarm Button Pressed",
                "The rocket should be disarmed");
        // lbState.setText("No Go State");simula
    }

    /**
     * Basic callback for when clicking on the Arm button.
     *
     * @param actionEvent   The action event representing the event.
     * @param armIndicator  The label we want to change.
     */
    public void onArm(ActionEvent actionEvent, Label armIndicator) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (warnC.hasWarnings() || warnC.hasErrors()) { // If warnings, give a prompt, ask them to click go again.
            alert.setTitle("Confirmation Dialog");
            if (warnC.hasWarnings()) { // If warnings exist
                alert.setHeaderText("Warnings exist (No Go recommended)");
            } else { // If errors exist
                alert.setHeaderText("Errors exist (No Go recommended)");
            }
            //run simulation here
        } else {
            alert.setHeaderText("Ready to arm (Go recommended)");
        }
        alert.setContentText("Are you ok with arming the rocket?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        armIndicator.setText("Armed");
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Go Button Pressed",
                "Arming the rocket now!");
    }

    /**
     * Updates the indicator in the top right of the UI.
     * If weather is bad, then there should be a No Go indicated.
     */
    public boolean hasWarningsOrErrors() {
        if (warnC.hasWarnings() || warnC.hasErrors()) { // If warnings, then we should be at a no go.
            return true;
        }
        return false;
    }

    /**
     * Save LaunchParameters to json file.
     * @param lp LaunchParameters object.
     */
    public void saveLaunchParameters(LaunchParameters lp) {
        JsonExporter.save("src/main/resources/config/launch-parameters.json", lp);
    }

    /**
     * Subscribe the warnings panel to RocketEvent's in the simulation importer.
     */
    public void subscribeToSimulation() {
        GraphController.getInstance().getSimulationImporter().subscribeObserver((data) -> {
            if (data instanceof RocketEvent) {
                warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT,
                        String.format("Rocket Event @ t+%.2fs: ", data.getTime()),
                        ((RocketEvent) data).getEventType().toString());
            }
        });
    }
}

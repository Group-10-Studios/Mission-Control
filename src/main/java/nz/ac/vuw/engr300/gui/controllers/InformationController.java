package nz.ac.vuw.engr300.gui.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
     */
    public void onNoGo(ActionEvent actionEvent) {
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "No Go Button Pressed");
        // lbState.setText("No Go State");simula
    }

    /**
     * Basic callback for when clicking on the Go button.
     *
     * @param actionEvent   The action event representing the event.
     */
    public void onGo(ActionEvent actionEvent) {
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

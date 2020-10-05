package nz.ac.vuw.engr300.gui.controllers;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.communications.model.RocketEvent;
import nz.ac.vuw.engr300.gui.components.RocketAlert;
import nz.ac.vuw.engr300.importers.JsonExporter;
import nz.ac.vuw.engr300.model.LaunchParameters;

import java.util.Optional;

public class InformationController {

    private WarningsController warnC;

    /**
     * Constructor for the Information controller. Creates a warnings controller, gets the weather for the warnings
     * controller, then checks the weather if it is safe to launch.
     */
    public InformationController() {
        warnC = WarningsController.getInstance();
    }

    /**
     * Checks which button callback to use, arm or disarm.
     *
     * @param actionEvent   The action event representing the event.
     * @param armIndicator  The label we want to change.
     */
    public void onArmDisarm(ActionEvent actionEvent, Label armIndicator, Button armDisarm) {
        if (armIndicator.getText().equals("Disarmed")) {
            onArm(actionEvent, armIndicator, armDisarm);
        } else if (armIndicator.getText().equals("Armed")) {
            onDisarm(actionEvent, armIndicator, armDisarm);
        } else {
            // LOGGER.error("Arm/Disarm indicator not found");
            throw new Error("Arm/Disarm indicator not found - was " + armIndicator.getText());
        }
    }

    /**
     * Basic callback function for when clicking the disarm button.
     *
     * @param actionEvent   The action event representing the event.
     * @param armIndicator  The label we want to change.
     */
    public void onDisarm(ActionEvent actionEvent, Label armIndicator, Button armDisarm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Disarm button pressed");
        alert.setContentText("Are you sure you want to disarm the rocket?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        armDisarm.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
        armDisarm.setText("Arm");

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
    public void onArm(ActionEvent actionEvent, Label armIndicator, Button armDisarm) {
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
        armDisarm.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
                CornerRadii.EMPTY, Insets.EMPTY)));
        armDisarm.setText("Disarm");
        armIndicator.setText("Armed");
        warnC.addRocketAlert(RocketAlert.AlertLevel.ALERT, "Arm Button Pressed",
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

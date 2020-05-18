/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.importers.RocketDataImporter;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;

import javax.swing.*;

/**
 * Represents the controller for the Home application view.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 */
public class HomeController implements Initializable {

    private final OpenRocketImporter simulationImporter = new OpenRocketImporter();
    
    @FXML Label weatherLabel;
    @FXML public RocketDataLineChart lineChartAltitude;
    @FXML public RocketDataLineChart lineChartVel;
    @FXML public RocketDataLineChart lineChartAcceleration;

    public HomeController() {
        simulationImporter.subscribeObserver((data) -> {
            if (data instanceof RocketStatus) {
                lineChartAltitude.addValue(data.getTime(), ((RocketStatus) data).getAltitude());
                lineChartAcceleration.addValue(data.getTime(), ((RocketStatus) data).getTotalAcceleration());
                lineChartVel.addValue(data.getTime(), ((RocketStatus) data).getTotalVelocity());
            }
        });
    }

    /**
    * This is the initialize method that is called to build the root before starting the javafx project.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WeatherController wc = new WeatherController(weatherLabel);
        wc.updateWindSpeed();
    }

    /**
     * TODO This method will update the weather data label with the weather received from the API.
     */
    private void updateDataRealTime() {
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            i.set(i.get() + 1);
                            weatherLabel.setText("Elapsed time: " + i.get() + " seconds");
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Callback function for run simulation in main view, this function will
     * open a file dialog to select a simulation data file. It will then load
     * it into the data importer and run the simulation as if it was live.
     */
    public void runSim(){
        JFileChooser fileChooser = new JFileChooser("src/main/resources");
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try{
                simulationImporter.importData(file.getAbsolutePath());
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Failed to import simulation data!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            simulationImporter.start();
        }
    }

    /**
     * Callback for when the cross at top right gets pressed, this function
     * should be used to cleanup any resources and close any ongoing threads.
     */
    public void shutdown(){
        simulationImporter.stop();
    }
}

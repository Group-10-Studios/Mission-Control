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
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.importers.RocketDataImporter;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;

import javax.swing.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;
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

    @FXML Label lbWeather;
    @FXML Label lblHeader;
    @FXML AnchorPane apApp;
    @FXML Region pnBanner;
    @FXML Pane pnContent;
    @FXML Region pnAngleOfAttack;
    @FXML Region lbRealTimeFlightInfo;
    @FXML Region apNav;
    @FXML Region pnExtras;
    @FXML Region btnPastFlights;
    @FXML Region btnRunSim;
    @FXML Region pnDetails;
    @FXML Region pnNav;
    @FXML Region apWarnings;

    /**
    * This is the initialize method that is called to build the root before starting the javafx project.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WeatherController wc = new WeatherController(lbWeather);
        wc.updateWindSpeed();
        scaleItemHeight(apApp, lbWeather, 2);
        scaleItemWidth(apApp, lbWeather, 2);
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
                            lbWeather.setText("Elapsed time: " + i.get() + " seconds");
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

        /**
         *
         * @param root The root pane the UI is all under.
         * @param node A specific node we may want to change.
         * @param i What ratio of the root height we want to scale things by.
         */
    private void scaleItemHeight(Region root, Region node, int i) {
        root.heightProperty().addListener(new ChangeListener<Number>() {
            /**
             *
             * @param observableValue
             * @param number Current height of the window
             * @param t1 New value of the height, what it will be changed to.
             */
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double height = (double) t1;
//                node.setPrefHeight(height/10);
//                apApp.setPrefHeight(height);
            }
        });
    }

    /**
     *
     * @param root The root pane the UI is all under.
     * @param node A specific node we may want to change.
     * @param i What ratio of the root width we want to scale things by.
     */
    private void scaleItemWidth(Region root, Region node, int i) {
        root.widthProperty().addListener(new ChangeListener<Number>() {
            /**
             *
             * @param observableValue
             * @param number Current width of the window
             * @param t1 New value of the width, what it will be changed to.
             */
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double width = (double) t1;
//                apApp.setPrefWidth(width);
                node.setPrefWidth(width/2);
                pnBanner.setPrefWidth(width);
                lblHeader.setPrefWidth(width);
                apNav.setPrefWidth(width/6); //left panel
                System.out.println(apNav.getWidth());

                //set middle panel to be slightly to the right of left panel
                pnContent.setLayoutX(apNav.getWidth() + 10.0);
                pnContent.setPrefWidth((width*2)/3); //middle panel width should be 2/3 of the screen width

                //set right panel to be slightly to the right of middle panel
                apWarnings.setLayoutX(apNav.getWidth() + 10.0 + pnContent.getWidth() + 10.0);
                apWarnings.setPrefWidth(width/6); //right panel should be a 1/6th of sceen width
            }
        });
    }
}



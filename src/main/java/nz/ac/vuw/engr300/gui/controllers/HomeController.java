/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui.controllers;

import java.io.FileNotFoundException;
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
    @FXML Pane pnRangeDistance;
    @FXML Pane pnAngleOfAttack;
    @FXML Pane pnLocation;

    @FXML Label lbWindSpeed;
    @FXML Pane pnWindSpeed;

    @FXML Label lbVelocity;
    @FXML Pane pnVelocity;

    @FXML Label lbAltitude;
    @FXML Pane pnAltitude;

    @FXML Label lblHeader;
    @FXML AnchorPane apApp;
    @FXML Region pnBanner;
    @FXML Pane pnContent;
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
        WeatherController wc = new WeatherController(lbWindSpeed);
        wc.updateWindSpeed();
        scaleItemHeight(apApp, lbWindSpeed, 2);
        scaleItemWidth(apApp, lbWindSpeed, 2);
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
                            lbWindSpeed.setText("Elapsed time: " + i.get() + " seconds");
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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
                apNav.setMinWidth(width/6); //left panel
                System.out.println(apNav.getWidth());

                //set middle panel to be slightly to the right of left panel
                pnContent.setLayoutX(apNav.getWidth() + 10.0);
                pnContent.setMinWidth((width*2)/3); //middle panel width should be 2/3 of the screen width

                //Small weather pane is 10 to the left of the start of the content pane
                pnWindSpeed.setLayoutX(15.0);
                pnWindSpeed.setPrefWidth((pnContent.getWidth()/3) - 5);
                pnRangeDistance.setLayoutX(15.0);
                pnRangeDistance.setPrefWidth((pnContent.getWidth()/3) - 5);

                //Small Velocity pane is 10 to the left of the start of the weather pane
                pnVelocity.setLayoutX(pnWindSpeed.getLayoutX() + pnWindSpeed.getWidth() + 10.0);
                pnVelocity.setPrefWidth((pnContent.getWidth()/3) - 5);
                pnAngleOfAttack.setLayoutX(pnWindSpeed.getLayoutX() + pnWindSpeed.getWidth() + 10.0);
                pnAngleOfAttack.setPrefWidth((pnContent.getWidth()/3) - 5);


                //Small Altitude pane is 10 to the left of the start of the velocity pane
                pnAltitude.setLayoutX(pnVelocity.getLayoutX() + pnVelocity.getWidth() + 10.0);
//                pnAltitude.setPrefWidth((pnContent.getWidth()/3) - 5); TODO fix this?? this one cuts off
                pnLocation.setLayoutX(pnVelocity.getLayoutX() + pnVelocity.getWidth() + 10.0);
//                pnLocation.setPrefWidth((pnContent.getWidth()/3) - 5); TODO fix this?? this one cuts off

                //set right panel to be slightly to the right of middle panel
                apWarnings.setLayoutX(apNav.getWidth() + 10.0 + pnContent.getWidth() + 10.0);
                apWarnings.setMinWidth(width/6); //right panel should be a 1/6th of sceen width
            }
        });
    }
}



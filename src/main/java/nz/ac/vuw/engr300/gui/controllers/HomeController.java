/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

import javax.swing.*;

/**
 * Represents the controller for the Home application view.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 */
public class HomeController implements Initializable {
    
    @FXML Label weatherLabel;
    @FXML RocketDataLineChart lineChartAltitude;
    @FXML RocketDataLineChart lineChartVel;
    @FXML RocketDataLineChart lineChartRangeDist;

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

    public void runSim(){

//        //TODO: implement with data rather than a variable
//        AtomicInteger var = new AtomicInteger();
//        new Thread(() -> {
//            while(true){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//                lineChartAltitude.addValue(var.getAndIncrement(), var.getAndIncrement());
//                lineChartRangeDist.addValue(var.getAndIncrement(), var.getAndIncrement());
//                lineChartVel.addValue(var.getAndIncrement(), var.getAndIncrement());
//            }
//        }).start();

        OpenRocketImporter sim = new OpenRocketImporter();

        JFileChooser fc = new JFileChooser("src/main/resources");
        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            sim.importData(f.getAbsolutePath());
            sim.subscribeObserver((data) -> {

                if (data instanceof RocketStatus) {
                    lineChartAltitude.addValue(data.getTime(), ((RocketStatus) data).getAltitude());
                    lineChartRangeDist.addValue(data.getTime(), 1);
                    lineChartVel.addValue(data.getTime(), ((RocketStatus) data).getTotalVelocity());
                }

            });

            sim.start();
        }


    }
}

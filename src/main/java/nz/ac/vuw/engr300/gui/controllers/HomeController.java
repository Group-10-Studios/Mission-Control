/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui.controllers;

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
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;

/**
 * Represents the controller for the Home application view.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 */
public class HomeController implements Initializable {
    
    @FXML Label weatherLabel;
    @FXML RocketDataLineChart lineChardAltitude;

    /**
    * This is the initialize method that is called to build the root before starting the javafx project.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WeatherController wc = new WeatherController(weatherLabel);
        wc.updateWindSpeed();


//        //TODO: implement with data rather than a variable
//        AtomicInteger var = new AtomicInteger();
//        new Thread(() -> {
//           while(true){
//               try {
//                   Thread.sleep(1000);
//               } catch (InterruptedException e){
//                   e.printStackTrace();
//               }
//               lineChardAltitude.addValue(var.getAndIncrement(), var.getAndIncrement());
//           }
//        }).run();
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
}

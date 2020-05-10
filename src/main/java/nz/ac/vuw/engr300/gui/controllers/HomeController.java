/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Represents the controller for the Home application view.
 *
 * @author Nalin
 */
public class HomeController implements Initializable {
    
    @FXML
    private Label label;
    @FXML private Label weatherLabel;
    private String weather;

    /**
     * This method will update the weather data label with the weather received from the API.
     * @param weatherRecieved
     */
    public void initWeatherData(String weatherRecieved) {
        weather = weatherRecieved;
        weatherLabel.setText(weather);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initWeatherData("Weather Stats");
    }
    
}

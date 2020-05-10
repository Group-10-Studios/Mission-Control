/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.views.HomeView;

/**
 * Runs the main GUI application for Mission Control. This defaults to a HomeView start.
 *
 * @author Nalin
 */
public class GUI extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("ENGR301-Group10-Mission Control Software System");
        new HomeView(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

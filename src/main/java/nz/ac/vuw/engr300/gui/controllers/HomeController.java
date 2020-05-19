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
 * @author Nathan Duckett
 */
public class HomeController implements Initializable {
  @FXML
  Pane pnRangeDistance;
  @FXML
  Pane pnAngleOfAttack;
  @FXML
  Pane pnLocation;

  @FXML
  Label lbWindSpeed;
  @FXML
  Pane pnWindSpeed;

  @FXML
  Label lbVelocity;
  @FXML
  Pane pnVelocity;

  @FXML
  Label lbAltitude;
  @FXML
  Pane pnAltitude;

  @FXML
  Label lblHeader;
  @FXML
  AnchorPane apApp;
  @FXML
  Region pnBanner;
  @FXML
  Pane pnContent;
  @FXML
  Region lbRealTimeFlightInfo;
  @FXML
  Region apNav;
  @FXML
  Region pnExtras;
  @FXML
  Region btnPastFlights;
  @FXML
  Region btnRunSim;
  @FXML
  Region pnDetails;
  @FXML
  Region pnNav;
  @FXML
  Region apWarnings;
  @FXML
  Region pnWarnings;

  /**
   * This is the initialize method that is called to build the root before
   * starting the javafx project.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    WeatherController wc = new WeatherController(lbWindSpeed);
    wc.updateWindSpeed();
    scaleItemHeight(apApp, lbWindSpeed, 2);
    scaleItemWidth(apApp, lbWindSpeed, 2);

    refreshOnStart();
  }

  /**
   * Refresh on start is designed to wait on a separate thread then manually
   * update the positions of panels to match our dynamic design.
   */
  private void refreshOnStart() {
    new Thread(() -> {
      try {
        // Sleep for 350ms to wait for UI to load
        Thread.sleep(350);
      } catch (InterruptedException e) {
        throw new RuntimeException("Error while sleeping to auto-refresh display position", e);
      }

      // Pass bound width to begin application
      updatePanelPositions(apApp, apApp.getBoundsInParent().getWidth());
      updatePanelPositionsVertical(apApp, apApp.getBoundsInParent().getHeight());
    }).start();
  }

  /**
   * TODO This method will update the weather data label with the weather received
   * from the API.
   */
  private void updateDataRealTime() {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      i.set(i.get() + 1);
      lbWindSpeed.setText("Elapsed time: " + i.get() + " seconds");
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   *
   * @param root The root pane the UI is all under.
   * @param node A specific node we may want to change.
   * @param i    What ratio of the root height we want to scale things by.
   */
  private void scaleItemHeight(Region root, Region node, int i) {
    root.heightProperty().addListener(new ChangeListener<Number>() {
      /**
       *
       * @param observableValue
       * @param number          Current height of the window
       * @param t1              New value of the height, what it will be changed to.
       */
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number number,
          Number t1) {
        updatePanelPositionsVertical(node, t1);
      }
    });
  }
  
  /**
   * Update the panel's positions to dynamically match the new application height.
   * @param rootPanel
   * @param newHeight
   */
  private void updatePanelPositionsVertical(Region rootPanel, Number newHeight) {
    double height = (double) newHeight;
    updatePanelsToHeight(height, apNav, pnContent, apWarnings);
    
    // pnWarnings can have 5/6 of height space
    updatePanelsToHeight((height * 5) /6, pnWarnings);
    
    // pnNav can have 2/3 of height space
    updatePanelsToHeight((height * 2) / 3, pnNav);
    
    // Update the y position of pnExtras
    updatePanelPositionOffsetVertical(pnExtras, pnNav, 10.0);
  }

  /**
   *
   * @param root The root pane the UI is all under.
   * @param node A specific node we may want to change.
   * @param i    What ratio of the root width we want to scale things by.
   */
  private void scaleItemWidth(Region root, Region node, int i) {
    root.widthProperty().addListener(new ChangeListener<Number>() {
      /**
       *
       * @param observableValue
       * @param number          Current width of the window
       * @param t1              New value of the width, what it will be changed to.
       */
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number number,
          Number t1) {
        updatePanelPositions(node, t1);
      }
    });
  }

  /**
   * Update the panel positions to dynamically match the new application width.
   * 
   * @param rootPanel
   * @param newWidth
   */
  private void updatePanelPositions(Region rootPanel, Number newWidth) {
    double standardOffset = 10.0;
    double rows = 3;
    double width = (double) newWidth;
    apApp.setPrefWidth(width / 2);
    updatePanelsToWidth(width, pnBanner, lblHeader);
    apNav.setMinWidth(width / 6); // left panel

    // set middle panel to be slightly to the right of left panel
    updatePanelPositionOffset(pnContent, apNav, standardOffset);
    pnContent.setMinWidth((width * 2) / 3); // middle panel width should be 2/3 of the screen width
    pnContent.setMaxWidth((width * 2) / 3); // middle panel shouldn't be larger than 2/3
    
    // Only the length internally excluding the offset
    double graphWidth = (pnContent.getWidth() / rows) - standardOffset;
    // Set all positions based on graph width
    updatePanelsToWidth(graphWidth, pnWindSpeed, pnRangeDistance, pnVelocity, pnAngleOfAttack,
        pnAltitude, pnLocation);

    // Set left most graph x positions - not relative to anything
    updatePanelPositionOffset(pnWindSpeed, null, standardOffset / 2);
    updatePanelPositionOffset(pnRangeDistance, null, standardOffset / 2);

    // Set centre graph x positions - relative to wind speed graph
    updatePanelPositionOffset(pnVelocity, pnWindSpeed, standardOffset);
    updatePanelPositionOffset(pnAngleOfAttack, pnWindSpeed, standardOffset);

    // Set the right graph x positions - relative to velocity graph
    updatePanelPositionOffset(pnAltitude, pnVelocity, standardOffset);
    updatePanelPositionOffset(pnLocation, pnVelocity, standardOffset);

    // set right panel to be slightly to the right of middle panel
    apWarnings
        .setLayoutX(apNav.getWidth() + standardOffset + pnContent.getWidth() + standardOffset);
    apWarnings.setMinWidth(width / 6); // right panel should be a 1/6th of screen width
  }

  /**
   * Update all of the provided panels preferred width to the value provided.
   * 
   * @param width  Preferred width to set all panels to.
   * @param panels Array of panels to set the preferred width on.
   */
  private void updatePanelsToWidth(double width, Region... panels) {
    for (Region panel : panels) {
      panel.setPrefWidth(width);
      panel.setMaxWidth(width);
    }
  }
  
  /**
   * Update all of the provided panels preferred height to the value provided.
   * 
   * @param height  Preferred height to set all panels to.
   * @param panels Array of panels to set the preferred height on.
   */
  private void updatePanelsToHeight(double height, Region... panels) {
    for (Region panel : panels) {
      panel.setPrefHeight(height);
      panel.setMaxHeight(height);
    }
  }

  /**
   * Update the panel position based on the relative position of the other panel.
   * This can offset thisPanel by the correct amount to not overlap relativePanel.
   * This works on the x axis.
   * 
   * @param thisPanel     The panel to update the x position of based on the
   *                      relativePanel.
   * @param relativePanel Relative panel to position thisPanel against based on
   *                      its' x position and width.
   * @param offset        Offset to add between the relativePanel right side and
   *                      thisPanel left side.
   */
  private void updatePanelPositionOffset(Region thisPanel, Region relativePanel, double offset) {
    if (relativePanel == null) {
      thisPanel.setLayoutX(offset);
      return;
    }

    // Calculate x position based off relativePanel position/size
    thisPanel.setLayoutX(relativePanel.getLayoutX() + relativePanel.getWidth() + offset);
  }
  
  /**
   * Update the panel position based on the relative position of the other panel.
   * This can offset thisPanel by the correct amount to not overlap relativePanel.
   * This works on the y axis.
   * 
   * @param thisPanel     The panel to update the y position of based on the
   *                      relativePanel.
   * @param relativePanel Relative panel to position thisPanel against based on
   *                      its' y position and height.
   * @param offset        Offset to add between the relativePanel bottom side and
   *                      thisPanel top side.
   */
  private void updatePanelPositionOffsetVertical(Region thisPanel, Region relativePanel, double offset) {
    if (relativePanel == null) {
      thisPanel.setLayoutY(offset);
      return;
    }

    // Calculate x position based off relativePanel position/size
    thisPanel.setLayoutY(relativePanel.getLayoutY() + relativePanel.getHeight() + offset);
  }

}

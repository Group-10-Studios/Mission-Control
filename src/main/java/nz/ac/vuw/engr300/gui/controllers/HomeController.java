/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.vuw.engr300.gui.controllers;

import java.util.List;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.model.GraphType;

import javax.swing.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Represents the controller for the Home application view.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 * @author Nathan Duckett
 */
public class HomeController implements Initializable {
  private static final double STANDARD_OFFSET = 10.0;
  private static final double HALF_OFFSET = STANDARD_OFFSET / 2;
  private static final double ROWS = 2;

  @FXML
  public Pane pnWindDirection;
  @FXML
  public Label lbWindDirection;

  @FXML
  public RocketDataAngle windCompass;

  @FXML
  Pane pnAcceleration;
  @FXML
  Label lbAcceleration;

  @FXML
  Label lbVelocity;
  @FXML
  Pane pnVelocity;

  @FXML
  Label lbAltitude;
  @FXML
  Pane pnAltitude;

  private final OpenRocketImporter simulationImporter = new OpenRocketImporter();

  @FXML
  Label weatherLabel;
  @FXML
  public RocketDataLineChart lineChartAltitude;
  @FXML
  public RocketDataLineChart lineChartVel;
  @FXML
  public RocketDataLineChart lineChartAcceleration;

  @FXML
  Label lbWeather;
  @FXML
  Label lbWeatherHead;
  @FXML
  Label lbRocketID;
  @FXML
  Label lbRocketHead;
  @FXML
  Label lbState;
  @FXML
  Label lbStateHead;

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

  public HomeController() {
    simulationImporter.subscribeObserver((data) -> {
      if (data instanceof RocketStatus) {
        lineChartAltitude.addValue(data.getTime(), ((RocketStatus) data).getAltitude());
        lineChartAcceleration.addValue(data.getTime(),
            ((RocketStatus) data).getTotalAcceleration());
        lineChartVel.addValue(data.getTime(), ((RocketStatus) data).getTotalVelocity());
      }
    });
  }

  /**
   * This is the initialize method that is called to build the root before
   * starting the javafx project.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    WeatherController wc = new WeatherController(lbWeather, windCompass);
    wc.updateWindSpeed();
    scaleItemHeight(apApp);
    scaleItemWidth(apApp);

    refreshOnStart();
    bindGraphsToType();
    listGraphs();
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
   * Manually binds the graph type to the graphs. This could maybe be automated later but for now can
   * set the values.
   */
  private void bindGraphsToType() {
      lineChartAcceleration.setGraphType(GraphType.ACCELERATION);
      lineChartAltitude.setGraphType(GraphType.ALTITUDE);
      lineChartVel.setGraphType(GraphType.VELOCITY);
  }
  
  /**
   * List out all of the graphs in the side panel.
   */
  private void listGraphs() {
      List<String> labels = Stream.of(GraphType.values()).map(g -> g.getLabel()).collect(Collectors.toList());
      Pane nav = (Pane) pnNav;
      int y = 5;
      for (String label : labels) {
	  Button b = new Button(label);
	  b.setLayoutY(y);
	  b.setOnAction(e -> {});
	  nav.getChildren().add(b);
	  y += 30;
      }
  }

  /**
   * TODO This method will update the weather data label with the weather received
   * from the API.
   */
  private void updateDataRealTime() {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      i.set(i.get() + 1);
//      lbWindSpeed.setText("Elapsed time: " + i.get() + " seconds");
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Callback function for run simulation in main view, this function will open a
   * file dialog to select a simulation data file. It will then load it into the
   * data importer and run the simulation as if it was live.
   */
  public void runSim() throws InvocationTargetException, InterruptedException {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFileChooser fileChooser = new JFileChooser("src/main/resources");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          try {
            simulationImporter.importData(file.getAbsolutePath());
          } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed to import simulation data!",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }

          simulationImporter.start();
        }
      }
    });
  }

  /**
   * Callback for when the cross at top right gets pressed, this function should
   * be used to cleanup any resources and close any ongoing threads.
   */
  public void shutdown() {
    simulationImporter.stop();
  }

  /**
   *
   * @param root The root pane the UI is all under.
   * @param node A specific node we may want to change.
   * @param i    What ratio of the root height we want to scale things by.
   */
  private void scaleItemHeight(Region root) {
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
        updatePanelPositionsVertical(root, t1);
      }
    });
  }

  /**
   * Update the panel's positions to dynamically match the new application height.
   * 
   * @param rootPanel Region provided from the root panel within the listener.
   * @param newWidth  New height value of the root panel.
   */
  private void updatePanelPositionsVertical(Region rootPanel, Number newHeight) {
    double height = (double) newHeight;
    updatePanelsToHeight(height - pnBanner.getHeight(), apNav, pnContent, apWarnings);

    // pnWarnings can have 5/6 of height space
    updatePanelsToHeight((height * 5) / 6, pnWarnings);

    // pnNav can have 1/2 of height space
    updatePanelsToHeight(height / 2, pnNav);

    // Update the y position of pnExtras
    updatePanelPositionOffsetVertical(pnExtras, pnNav, 10.0);
    
    // Update each graph relative to each other
    updateGraphsVertical();
    
    // Update text height relative to each other in pnDetails
    updatePanelPositionOffsetVertical(lbRocketHead, null, 0);
    updatePanelPositionOffsetVertical(lbRocketID, lbRocketHead, 0);
    updatePanelPositionOffsetVertical(lbStateHead, lbRocketID, 0);
    updatePanelPositionOffsetVertical(lbState, lbStateHead, 0);
    updatePanelPositionOffsetVertical(lbWeatherHead, lbState, 0);
    updatePanelPositionOffsetVertical(lbWeather, lbWeatherHead, 0);
  }

  private void updateGraphsVertical() {
    // Update heights of the panels
    double graphHeight = (pnContent.getHeight() / ROWS) - STANDARD_OFFSET;
    updatePanelsToHeight(graphHeight, pnAcceleration, pnAltitude, pnVelocity, pnWindDirection);
    
    // Set the graph sizes relative to the box
    double internalChartWidth = graphHeight * 5/6;
    lineChartAcceleration.setMaxHeight(internalChartWidth);
    lineChartAltitude.setMaxHeight(internalChartWidth);
    lineChartVel.setMaxHeight(internalChartWidth);
    lineChartAcceleration.setPrefHeight(internalChartWidth);
    lineChartAltitude.setPrefHeight(internalChartWidth);
    lineChartVel.setPrefHeight(internalChartWidth);
    
    // Set position relative to above row
    updatePanelPositionOffsetVertical(pnAcceleration, pnVelocity, 10.0);
    updatePanelPositionOffsetVertical(pnWindDirection, pnAltitude, 10.0);
  }
  
  /**
   *
   * @param root The root pane the UI is all under.
   * @param node A specific node we may want to change.
   * @param i    What ratio of the root width we want to scale things by.
   */
  private void scaleItemWidth(Region root) {
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
        updatePanelPositions(root, t1);
      }
    });
  }

  /**
   * Update the panel positions to dynamically match the new application width.
   * 
   * @param rootPanel Region provided from the root panel within the listener.
   * @param newWidth  New width value of the root panel.
   */
  private void updatePanelPositions(Region rootPanel, Number newWidth) {
    double width = (double) newWidth;
    // side panels should be a 1/6th of screen width
    double sidePanelWidth = (width - STANDARD_OFFSET) / 6;
    apApp.setPrefWidth(width / 2);
    updatePanelsToWidth(width, pnBanner, lblHeader);

    // left panel - set widths of internal panels within the width
    updateLeftPanel(sidePanelWidth);
    updateMiddlePanel(width);
    updateRightPanel(sidePanelWidth);
  }

  /**
   * Update the components within the left panel to dynamically fit within the
   * screen.
   * 
   * @param sidePanelWidth The expected width of the side panel.
   */
  private void updateLeftPanel(double sidePanelWidth) {
    apNav.setMinWidth(sidePanelWidth);
    updatePanelPositionOffset(pnNav, null, HALF_OFFSET);
    updatePanelPositionOffset(pnDetails, null, HALF_OFFSET);
    updatePanelPositionOffset(pnExtras, null, HALF_OFFSET);
    updatePanelsToWidth(sidePanelWidth - STANDARD_OFFSET, pnNav, pnDetails, pnExtras);

    // internal left panel extras buttons
    updatePanelsToWidth(pnExtras.getWidth() - (STANDARD_OFFSET * 2), btnRunSim, btnPastFlights);
    updatePanelPositionOffset(btnRunSim, null, STANDARD_OFFSET);
    updatePanelPositionOffset(btnPastFlights, null, STANDARD_OFFSET);

    // internal left panel details text
    updatePanelsToWidth(pnDetails.getWidth(), lbRocketHead, lbRocketID,
        lbState, lbStateHead, lbWeather, lbWeatherHead);
    updatePanelPositionOffset(lbState, null, 0);
    updatePanelPositionOffset(lbStateHead, null, 0);
    updatePanelPositionOffset(lbRocketID, null, 0);
    updatePanelPositionOffset(lbRocketHead, null, 0);
    updatePanelPositionOffset(lbWeather, null, 0);
    updatePanelPositionOffset(lbWeatherHead, null, 0);
  }

  /**
   * Update the components within the middle panel to dynamically fit within the
   * screen.
   * 
   * @param width Total width of the application which will determine the internal
   *              widths.
   */
  private void updateMiddlePanel(double width) {
    // set middle panel to be slightly to the right of left panel
    updatePanelPositionOffset(pnContent, apNav, STANDARD_OFFSET / 2);
    pnContent.setMinWidth((width * 2) / 3); // middle panel width should be 2/3 of the screen width
    pnContent.setMaxWidth((width * 2) / 3); // middle panel shouldn't be larger than 2/3

    // Only the length internally excluding the offset
    double graphWidth = ((pnContent.getWidth() - STANDARD_OFFSET) / ROWS) - STANDARD_OFFSET;
    // Set all positions based on graph width
//    updatePanelsToWidth(graphWidth, pnWindSpeed, pnRangeDistance, pnVelocity, pnAngleOfAttack,
//        pnAltitude, pnLocation);

    updatePanelsToWidth(graphWidth, pnAltitude, pnVelocity, pnAcceleration, pnWindDirection);
    double internalChartWidth = graphWidth - STANDARD_OFFSET;
    lineChartAcceleration.setMaxWidth(internalChartWidth);
    lineChartAltitude.setMaxWidth(internalChartWidth);
    lineChartVel.setMaxWidth(internalChartWidth);
    windCompass.setMaxWidth(internalChartWidth);
    // Set left most graph x positions - not relative to anything
    updatePanelPositionOffset(pnAcceleration, null, STANDARD_OFFSET);
    updatePanelPositionOffset(pnVelocity, null, STANDARD_OFFSET);

    // Set the right graph x positions - relative to velocity graph
    updatePanelPositionOffset(pnAltitude, pnVelocity, STANDARD_OFFSET);
    updatePanelPositionOffset(pnWindDirection, pnAcceleration, STANDARD_OFFSET);
  }

  /**
   * Update the components within the right side panel to dynamically fit within
   * the screen.
   * 
   * @param sidePanelWidth The expected width of the side panel.
   */
  private void updateRightPanel(double sidePanelWidth) {
    // set right panel to be right of middle panel
    updatePanelPositionOffset(apWarnings, pnContent, HALF_OFFSET);
    apWarnings.setMinWidth(sidePanelWidth);
    updatePanelsToWidth(apWarnings.getWidth() - (2 * STANDARD_OFFSET), pnWarnings);
    // Must be relative to null to make internal to apWarnings
    updatePanelPositionOffset(pnWarnings, null, HALF_OFFSET);
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
   * @param height Preferred height to set all panels to.
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
  private void updatePanelPositionOffsetVertical(Region thisPanel, Region relativePanel,
      double offset) {
    if (relativePanel == null) {
      thisPanel.setLayoutY(offset);
      return;
    }

    // Calculate x position based off relativePanel position/size
    thisPanel.setLayoutY(relativePanel.getLayoutY() + relativePanel.getHeight() + offset);
  }

}

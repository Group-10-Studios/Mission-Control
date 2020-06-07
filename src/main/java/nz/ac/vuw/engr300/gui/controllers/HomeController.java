package nz.ac.vuw.engr300.gui.controllers;

import java.awt.EventQueue;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.model.GraphType;

/**
 * Represents the controller for the Home application view.
 *
 * @author Nalin Aswani
 * @author Jake Mai
 * @author Nathan Duckett
 * @author Ahad Rahman
 */
public class HomeController implements Initializable {
    private static final double STANDARD_OFFSET = 10.0;
    private static final double HALF_OFFSET = STANDARD_OFFSET / 2;
    private static final double ROWS = 3;
    private static final double COLS = 4;

    private static final double BUTTON_HEIGHT = 30;

    @FXML
    public RocketDataAngle windCompass = new RocketDataAngle(true);

    private final OpenRocketImporter simulationImporter = new OpenRocketImporter();

    @FXML
    Label weatherLabel;
    @FXML
    public RocketDataLineChart lineChartAltitude = new RocketDataLineChart("Time ( S )", "Altitude ( M )");
    @FXML
    public RocketDataLineChart lineChartTotalVelocity = new RocketDataLineChart("Time ( S )", "Altitude ( M/S )");
    @FXML
    public RocketDataLineChart lineChartTotalAcceleration = new RocketDataLineChart("Time ( S )", "Altitude ( M/S^2 )");

    @FXML
    public RocketDataLineChart lineChartVelocityX = new RocketDataLineChart("Time ( S )", "Altitude ( M/S )");
    @FXML
    public RocketDataLineChart lineChartVelocityY = new RocketDataLineChart("Time ( S )", "Altitude ( M/S )");
    @FXML
    public RocketDataLineChart lineChartVelocityZ = new RocketDataLineChart("Time ( S )", "Altitude ( M/S )");
    @FXML
    public RocketDataLineChart lineChartAccelerationX = new RocketDataLineChart("Time ( S )", "Altitude ( M/S^2 )");
    @FXML
    public RocketDataLineChart lineChartAccelerationY = new RocketDataLineChart("Time ( S )", "Altitude ( M/S^2 )");
    @FXML
    public RocketDataLineChart lineChartAccelerationZ = new RocketDataLineChart("Time ( S )", "Altitude ( M/S^2 )");

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

    /**
     * Note must be Region to be a parent of all graph components.
     */
    private List<RocketGraph> graphs;
    private List<Button> pnNavButtons;
    /**
     * Stores the currently highlighted graph information for de-selection.
     */
    private GraphType highlightedGraph;

    /**
     * Create a new HomeController subscribing the graphs to the data sources.
     */
    public HomeController() {
        simulationImporter.subscribeObserver((data) -> {
            if (data instanceof RocketStatus) {
                lineChartAltitude.addValue(data.getTime(), ((RocketStatus) data).getAltitude());
                lineChartTotalAcceleration.addValue(data.getTime(), ((RocketStatus) data).getTotalAcceleration());
                lineChartTotalVelocity.addValue(data.getTime(), ((RocketStatus) data).getTotalVelocity());
            }
        });
    }

    /**
     * This is the initialize method that is called to build the root before
     * starting the javafx project.
     *
     * @param url The location used to resolve relative paths for the root object,
     *            or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the
     *            root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WeatherController wc = new WeatherController(lbWeather, windCompass);
        wc.updateWindSpeed();
        scaleItemHeight(apApp);
        scaleItemWidth(apApp);

        this.pnNavButtons = new ArrayList<>();

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
            updatePanelPositions(apApp, apApp.getWidth());
            updatePanelPositionsVertical(apApp, apApp.getHeight());
        }).start();
    }

    /**
     * Manually binds the graph type to the graphs. This could maybe be automated
     * later but for now can set the values.
     */
    private void bindGraphsToType() {
        lineChartAltitude.setGraphType(GraphType.ALTITUDE);

        lineChartTotalAcceleration.setGraphType(GraphType.TOTAL_ACCELERATION);
        lineChartAccelerationX.setGraphType(GraphType.X_ACCELERATION);
        lineChartAccelerationY.setGraphType(GraphType.Y_ACCELERATION);
        lineChartAccelerationZ.setGraphType(GraphType.Z_ACCELERATION);


        lineChartTotalVelocity.setGraphType(GraphType.TOTAL_VELOCITY);
        lineChartVelocityX.setGraphType(GraphType.X_VELOCITY);
        lineChartVelocityY.setGraphType(GraphType.Y_VELOCITY);
        lineChartVelocityZ.setGraphType(GraphType.Z_VELOCITY);

        windCompass.setGraphType(GraphType.WINDDIRECTION);


        this.graphs = new ArrayList<>();
        this.graphs.add(lineChartTotalVelocity);
        this.graphs.add(lineChartVelocityX);
        this.graphs.add(lineChartVelocityY);
        this.graphs.add(lineChartVelocityZ);

        this.graphs.add(lineChartTotalAcceleration);
        this.graphs.add(lineChartAccelerationX);
        this.graphs.add(lineChartAccelerationY);
        this.graphs.add(lineChartAccelerationZ);

        this.graphs.add(lineChartAltitude);
        this.graphs.add(windCompass);
        // Initialize the graph table.
        buildTable();
    }


    /**
     * Build a dynamic VBox/HBox table to hold our graphs in the centre of the
     * screen.
     */
    private void buildTable() {
        int graphNo = 0;
        VBox rowBox = new VBox(ROWS);
        rowBox.setSpacing(0);
        for (int i = 0; i < ROWS; i++) {
            HBox colBox = new HBox(COLS);
            colBox.setSpacing(0);
            for (int j = 0; j < COLS; j++) {
                if (graphNo >= this.graphs.size()) {
                    break;
                }
                colBox.getChildren().add((Region) this.graphs.get(graphNo++));
            }
            rowBox.getChildren().add(colBox);
        }

        pnContent.getChildren().clear();
        pnContent.getChildren().add(rowBox);
    }

    /**
     * List out all of the graphs in the side panel.
     */
    private void listGraphs() {
        List<String> labels = Stream.of(GraphType.values()).map(g -> g.getLabel()).collect(Collectors.toList());
        Pane nav = (Pane) pnNav;
        int y = 5;
        reorderGraphs(labels);
        ButtonSelected buttonSelected = new ButtonSelected();
        for (String label : labels) {
            Button b = new Button(label);
            b.setLayoutY(y);
            b.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    b.toFront();
                    buttonSelected.originalY = b.getLayoutY();
                    buttonSelected.y = b.getLayoutY() - mouseEvent.getSceneY();
                }
            });
            b.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    b.setLayoutY(mouseEvent.getSceneY() + buttonSelected.y);
                }
            });
            b.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double distanceMoved = Math.abs(buttonSelected.originalY - b.getLayoutY());
                    if (distanceMoved > BUTTON_HEIGHT/2) { //If the user has dragged the button past the halfway point of a button boundary
                        String buttonBeingMovedLabel = b.getText();
                        int indexOfButtonBeingMoved = labels.indexOf(buttonBeingMovedLabel);
                        int indexFurther = (int) Math.floor(distanceMoved/(BUTTON_HEIGHT-1));
                        int indexToReplace;
                        if (buttonSelected.originalY - b.getLayoutY() < 0) { //Figuring out which direction the user is dragging. If this is true, the user is dragging downwards
                            indexToReplace = indexOfButtonBeingMoved + indexFurther;
                            if (indexToReplace > labels.size() - 1) {
                                indexToReplace = labels.size() - 1; //If the user drags beyond the list, replace the last button
                            }
                        } else {
                            indexToReplace = indexOfButtonBeingMoved - indexFurther;
                            if (indexToReplace < 0) {
                                indexToReplace = 0; //If the user drags above the list, replace the first button
                            }
                        }
                        String btnBeingReplaced = labels.get(indexToReplace);
                        for (Button bt : pnNavButtons) {
                            if (bt.getText().equals(btnBeingReplaced)) {
                                b.setLayoutY(bt.getLayoutY());
                                bt.setLayoutY(buttonSelected.originalY);
                            }
                        }
                        Collections.swap(labels, indexOfButtonBeingMoved, indexToReplace); //Swap the two butons
                        reorderGraphs(labels);
                    } else {
                        b.setLayoutY(buttonSelected.originalY); //If the user barely drags the button (by mistake), then put it back
                    }
                }
            });
            b.setOnAction(e -> {
                GraphType thisGraph = GraphType.fromLabel(label);
                for (RocketGraph chart : this.graphs) {
                    // Get the chart as a region to set the Border
                    Region chartRegion = (Region) chart;
                    if (chart.getGraphType() == thisGraph && thisGraph != this.highlightedGraph) {
                        chartRegion.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID,
                                new CornerRadii(5.0), new BorderWidths(2.0))));
                        this.highlightedGraph = thisGraph;
                    } else if (chart.getGraphType() == thisGraph && thisGraph == this.highlightedGraph) {
                        // Ensure the clicked type is thisGraph and check if it is already clicked.
                        chartRegion.setBorder(null);

                        // Set the highlighted graph to null if already highlighted before.
                        // This is for turning off highlighting to re-enable.
                        this.highlightedGraph = null;
                    } else {
                        chartRegion.setBorder(null);
                    }
                }
            });
            nav.getChildren().add(b);
            // Add to button list for dynamics
            pnNavButtons.add(b);
            y += BUTTON_HEIGHT;
        }
    }

    private void reorderGraphs(List<String> labels) {
        for (int i = 0; i < labels.size(); i++) {
            for (int j = 0; j < graphs.size(); j++) {
                if (graphs.get(j).getGraphType().getLabel().equals(labels.get(i))) {
                    RocketGraph temp = graphs.get(j);
                    graphs.remove(j);
                    graphs.add(i, temp);
                }
            }
        }
        buildTable();
    }

    /**
     * TODO This method will update the weather data label with the weather received
     * from the API.
     */
    private void updateDataRealTime() {
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            i.set(i.get() + 1);
            // lbWindSpeed.setText("Elapsed time: " + i.get() + " seconds");
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
                simulationImporter.stop();

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
                    graphs.forEach(g -> g.clear());
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
     * Scaling all the panel heights according to the current window size.
     *
     * @param root The root pane the UI is all under.
     */
    private void scaleItemHeight(Region root) {
        root.heightProperty()
                .addListener((ObservableValue<? extends Number> observableValue, Number number, Number t1) -> {
                    updatePanelPositionsVertical(root, t1);
                });

    }

    /**
     * Update the panel's positions to dynamically match the new application height.
     *
     * @param rootPanel Region provided from the root panel within the listener.
     * @param newHeight New height value of the root panel.
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

    /**
     * Updates heights of the panels when the window is adjusted. Set the graph
     * sizes relative to the box. Set position relative to above row.
     */
    private void updateGraphsVertical() {
        // Update heights of the panels
        double graphHeight = (pnContent.getHeight() / ROWS);

        // Set the graph sizes relative to the box
        updatePanelsToHeight(graphHeight, allGraphs());
    }

    /**
     * Scaling all the panel widths according to the current window size.
     *
     * @param root The root pane the UI is all under.
     */
    private void scaleItemWidth(Region root) {
        root.widthProperty()
                .addListener((ObservableValue<? extends Number> observableValue, Number number, Number t1) -> {
                    updatePanelPositions(root, t1);
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

        // Internal pnNav Buttons
        updatePanelsToWidth(pnExtras.getWidth() - (STANDARD_OFFSET * 2),
                this.pnNavButtons.toArray(new Button[this.pnNavButtons.size()]));
        for (Button b : this.pnNavButtons) {
            updatePanelPositionOffset(b, null, STANDARD_OFFSET);
        }

        // internal left panel details text
        updatePanelsToWidth(pnDetails.getWidth(), lbRocketHead, lbRocketID, lbState, lbStateHead, lbWeather,
                lbWeatherHead);
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
        double graphWidth = (pnContent.getWidth()) / COLS;

        updatePanelsToWidth(graphWidth, allGraphs());
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
    private void updatePanelPositionOffsetVertical(Region thisPanel, Region relativePanel, double offset) {
        if (relativePanel == null) {
            thisPanel.setLayoutY(offset);
            return;
        }

        // Calculate x position based off relativePanel position/size
        thisPanel.setLayoutY(relativePanel.getLayoutY() + relativePanel.getHeight() + offset);
    }

    /**
     * Get all the graphs from pnContent in Region format.
     *
     * @return Region array of graphs.
     */
    private Region[] allGraphs() {
        return this.graphs.stream().map(g -> (Region) g).toArray(Region[]::new);
    }
}

// records relative y co-ordinates.
class ButtonSelected {
    double originalY;
    double y;
}
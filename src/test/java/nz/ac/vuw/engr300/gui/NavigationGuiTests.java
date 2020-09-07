package nz.ac.vuw.engr300.gui;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.communications.importers.CsvConfiguration;
import nz.ac.vuw.engr300.gui.components.RocketDataAngleLineChart;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.components.RocketGraph;
import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.HomeView;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify Navigation button components are working as expected.
 *
 * @author Nathan Duckett
 */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NavigationGuiTests extends ApplicationTest {
    private Stage stage;
    private HomeView homeView;

    /**
     * Get the number of graphs which are currently visible.
     * @return long representing the number of visible graphs on the GUI.
     */
    private long getNumberOfVisibleGraphs() {
        return homeView.getGraphView().getGraphs().stream().filter(RocketGraph::isGraphVisible).count();
    }

    /**
     * Build the NavigationButton ID from the graph type with the prefix for the robot to click on.
     *
     * @param graphType GraphType of the graph to be clicked.
     * @param prefix Button Prefix to determine it's type for this NavigationButton.
     * @return String containing an ID for the button to click.
     */
    private String buildButtonId(GraphType graphType, String prefix) {
        return "#btn" + prefix + graphType.getLabel().replace(" ", "");
    }

    /**
     * Goes through all graphs, hides all of them using the navigation buttons ensuring that they were hidden after
     * each click.
     *
     * @param robot   The robot injected to run tests.
     */
    private void hideAllGraphs(FxRobot robot) {
        long visibleCount = getNumberOfVisibleGraphs();
        for (GraphType g : GraphMasterList.getInstance().getGraphs()) {
            String btnId = buildButtonId(g, "Vis");
            robot.clickOn(btnId);
            long currentSize = getNumberOfVisibleGraphs();
            assertTrue(currentSize < visibleCount, g.getLabel() + " was not hidden correctly.");
            visibleCount = currentSize;
        }
    }

    /**
     * Goes through all graphs, shows all of them using the navigation buttons ensuring that they were shown after
     * each click. This works on the assumption that all graphs were hidden first.
     *
     * @param robot   The robot injected to run tests.
     */
    private void showAllGraphs(FxRobot robot) {
        long visibleCount = getNumberOfVisibleGraphs();
        for (GraphType g : GraphMasterList.getInstance().getGraphs()) {
            String btnId = buildButtonId(g, "Vis");
            robot.clickOn(btnId);
            long currentSize = getNumberOfVisibleGraphs();
            assertTrue(currentSize > visibleCount, g.getLabel() + " was not shown correctly.");
            visibleCount = currentSize;
        }
    }

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
        // Define graph structure from Test file.
        CsvConfiguration.getInstance().loadNewConfig("src/test/resources/TestCommunications.json");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.requestFocus();

        stage = primaryStage;
        stage.setMaximized(true);
        this.homeView = new HomeView(primaryStage);
        stage.show();

        // Overriding graph configuration to support Serial communications with Simulation
        homeView.getGraphView().updateGraphStructureDefinition("graphSim", true);
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.cleanupStages();
        stage.close();

        // Do not remove this - we need to shutdown the controller first after each test
        // This ensures no duplicate observers which can break graph values.
        GraphController.getInstance().shutdown();
    }

    /**
     * Checks the visibility of the graphs in both the JavaFX sense "isVisible" and in our hidden/visible state
     * "isGraphVisible". This will ensure that all graphs start as visible to the user.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    @Order(1)
    public void check_visibility_of_graphs(FxRobot robot) {
        // Loop through all graphs to make sure they're visible.
        for (GraphType graphType: GraphMasterList.getInstance().getGraphs()) {
            NodeQuery graph = robot.lookup("#" + graphType.getGraphID());
            // Check against region as all graphs should extend class
            Region regionComponent = graph.queryAs(Region.class);
            Assertions.assertThat(regionComponent.isVisible());

            RocketGraph graphComponent;
            if (graphType.getChart().equals("line")) {
                graphComponent = graph.queryAs(RocketDataLineChart.class);
            } else if (graphType.getChart().equals("angle")) {
                graphComponent = graph.queryAs(RocketDataAngleLineChart.class);
            } else {
                // Skip graphs which are not of the default type.
                continue;
            }
            Assertions.assertThat(graphComponent.isGraphVisible());
        }
    }

    /**
     * Test that as you click on each hidden button the number of graphs on the main screen decreases.
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_mark_graph_invisible(FxRobot robot) {
        hideAllGraphs(robot);
    }

    /**
     * Test that the graphs can be hidden and re-shown to count to the same amount as before.
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_toggle_graph_visibility(FxRobot robot) {
        hideAllGraphs(robot);
        showAllGraphs(robot);
    }

    /**
     * Test that a graph can be shifted down the list of graphs within the MasterList using NavigationButton.
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_shift_graph_down(FxRobot robot) {
        int graphCount = GraphMasterList.getInstance().getGraphs().size();
        GraphType graphToMove = GraphMasterList.getInstance().getGraphs().get(0);
        String buttonId = buildButtonId(graphToMove, "Down");

        // Repeat for the number of graphs to reach the bottom - wraps around at size.
        for (int i = 0; i < graphCount; i++) {
            int finalI = i;
            // Must be run in Platform.runLater to run on JavaFX thread.
            // Without this the TestFX workaround will not work.
            Platform.runLater(() -> {
                System.out.println(GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));
                assertEquals(finalI, GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));

                // Manually fire the button operation due to limitation outlined in #184 issue comments
                // TestFX does not allow clickOn in for loop, where button position changes each iteration
                robot.lookup(buttonId).queryButton().fire();


                // Check if we are at the end of the list.
                if (finalI == graphCount - 1) {
                    // Ensure it wraps if at the end.
                    assertEquals(0, GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));
                } else {
                    // Ensure that it moves to the next position.
                    assertEquals(finalI + 1, GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));
                }
            });
        }
    }

    /**
     * Test that a graph can be shifted up the list of graphs within the MasterList using NavigationButton.
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_shift_graph_up(FxRobot robot) {
        int graphCount = GraphMasterList.getInstance().getGraphs().size();
        GraphType graphToMove = GraphMasterList.getInstance().getGraphs().get(graphCount - 1);
        String buttonId = buildButtonId(graphToMove, "Up");

        // Repeat for the number of graphs to reach the top
        for (int i = graphCount - 1; i >= 0; i--) {
            int finalI = i;
            // Must be run in Platform.runLater to run on JavaFX thread.
            // Without this the TestFX workaround will not work.
            Platform.runLater(() -> {
                assertEquals(finalI, GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));

                // Manually fire the button operation due to limitation outlined in #184 issue comments
                // TestFX does not allow clickOn in for loop, where button position changes each iteration
                robot.lookup(buttonId).queryButton().fire();

                // Check if we are at the top of the list.
                if (finalI == 0) {
                    // Ensure it loops back around to the back of the list
                    assertEquals(graphCount - 1, GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));
                } else {
                    // Ensure that it moves to the next position
                    assertEquals(finalI - 1, GraphMasterList.getInstance().getGraphs().indexOf(graphToMove));
                }
            });
        }
    }
}

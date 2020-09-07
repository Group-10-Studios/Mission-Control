package nz.ac.vuw.engr300.gui;

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
    private String buildVisibleButtonId(GraphType graphType, String prefix) {
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
            String btnId = buildVisibleButtonId(g, "Vis");
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
            String btnId = buildVisibleButtonId(g, "Vis");
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
}

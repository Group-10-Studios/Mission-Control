package nz.ac.vuw.engr300.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.communications.importers.CsvConfiguration;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.importers.SerialCommunications;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
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
import org.testfx.service.query.EmptyNodeQueryException;
import org.testfx.util.WaitForAsyncUtils;

/**
 * General tests for the UI.
 *
 * @author Tim Salisbury
 * @author Nathan Duckett
 */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeneralGuiTests extends ApplicationTest {
    private static final List<RocketStatus> TEST_DATA;

    private static final String fullyCorrectRocketData =
            new File("src/test/resources/FullyCorrectRocketData.csv").getAbsolutePath();
    private static final String fullyCorrectTestData =
            new File("src/test/resources/FullyCorrectTestData.csv").getAbsolutePath();

    private static final String invalidJSONFile = new File("src/test/resources/InvalidJsonFile.json").getAbsolutePath();

    static {
        //Load in test data
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData("src/test/resources/FullyCorrectTestData.csv");
        TEST_DATA = importer.getData().stream().filter(data -> data instanceof RocketStatus)
                .map(data -> (RocketStatus) data).collect(Collectors.toList());
    }

    private Stage stage;

    /**
     * Checks if an alert pop is shown while importing simulation data. Note, this will take 8 seconds currently. If
     * a popup is on the screen, this function will press the OK button on it to dismiss it.
     *
     * @param robot The robot injected to run tests.
     * @return Whether or not an alert popup was visible.
     */
    public static boolean checkForAlertPopup(FxRobot robot) {
        try {
            WaitForAsyncUtils.waitFor(8, TimeUnit.SECONDS, () -> {
                try {
                    Node title = robot.lookup("Failed to import simulation data!").queryAs(Node.class);
                    Node ok = robot.lookup("OK").queryAs(Node.class);
                    if (title.isVisible() && ok.isVisible()) {
                        robot.clickOn(ok);
                        return true;
                    }
                    return false;
                } catch (EmptyNodeQueryException | NoSuchElementException ignored) {
                    return false;
                }
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Checks that a node is visible on screen, and when it is, this function will click on it. After 8 seconds,
     * if the node is not visible then the function will return false.
     *
     * @param nodeId The ID of the node to check for on the UI.
     * @param robot  The robot injected to run tests.
     */
    public static boolean checkAndClickOnNode(FxRobot robot, String nodeId) {
        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
                try {
                    Node node = robot.lookup(nodeId).queryAs(Node.class);
                    if (node.isVisible()) {
                        robot.clickOn(node);
                        return true;
                    }
                    return false;
                } catch (RuntimeException ignored) {
                    return false;
                }
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Checks that the graphs displayed by the UI have been populated with the {@code expected}
     * Rocket data.
     *
     * @param robot    The robot injected to run tests.
     * @param expected The expected data that the graphs should be populated with.
     */
    private static void checkGraphValues(FxRobot robot, List<RocketStatus> expected) {
        int expectedDataSize = expected.size();
        for (GraphType graphType: GraphMasterList.getInstance().getGraphs()) {
            List<XYChart.Data<Number, Number>> chartData =
                    robot.lookup("#" + graphType.getGraphID()).queryAs(RocketDataLineChart.class)
                            .getData().get(0).getData();

            assertEquals(expectedDataSize, chartData.size());
            for (int i = 0; i < expectedDataSize; i++) {
                assertEquals(expected.get(i).getTime(), chartData.get(i).getXValue());
                double value = getValueForGraph(expected, i, graphType);
                assertEquals(value, chartData.get(i).getYValue());
            }
        }
    }

    /**
     * Get an expected value for the provided graph type at the provided position. This lets us compare the actual
     * values to what we expected.
     *
     * @param expected List of expected rocket values we want to get the value from.
     * @param position The position of the data within the expected dataset.
     * @param graphType The type of graph this data is from, corresponding to the value to retrieve from the dataset.
     * @return A double retrieved from the expected dataset for the graph type.
     */
    private static double getValueForGraph(List<RocketStatus> expected, int position, GraphType graphType) {
        if (graphType.getLabel().equals("Total Velocity")) {
            return expected.get(position).getTotalVelocity();
        } else if (graphType.getLabel().equals("Total Acceleration")) {
            return expected.get(position).getTotalAcceleration();
        } else {
            return expected.get(position).getAltitude();
        }
    }

    /**
     * Runs the simulation provided at {@code simulationFile} for {@code simulationRunTime}ms.
     *
     * @param robot             The robot injected to run tests.
     * @param simulationFile    The simulation we wish to run.
     */
    private static boolean runSimulation(FxRobot robot, String simulationFile) {
        if (!checkAndClickOnNode(robot, "#btnRunSim")) {
            fail("Run simulation button not found!");
        }

        WaitForAsyncUtils.waitForFxEvents(5);

        copyPasteString(robot, simulationFile);
        WaitForAsyncUtils.waitForFxEvents();

        return !checkForAlertPopup(robot);
    }

    /**
     * Clicks the button and checks to see if the graph is highlighted.
     *
     * @param btnId   The ID of the button to be clicked.
     * @param graphId The ID of the graph that should be highlighted.
     * @param robot   The robot injected to run tests.
     */
    private static void checkHighlight(String btnId, String graphId, FxRobot robot) {

        assertNull(robot.lookup(graphId).queryAs(Region.class).getBorder());
        robot.clickOn(btnId);
        assertNotNull(robot.lookup(graphId).queryAs(Region.class).getBorder());
        robot.clickOn(btnId);
        assertNull(robot.lookup(graphId).queryAs(Region.class).getBorder());

    }

    /**
     * Copies {@code string} to the ClipBoard and pastes it in the focused InputBox,
     * whatever that may be.
     *
     * @param robot  The robot injected to run tests.
     * @param string The string to copy and paste.
     */
    public static void copyPasteString(FxRobot robot, String string) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, stringSelection);

        robot.press(KeyCode.CONTROL, KeyCode.V).release(KeyCode.CONTROL, KeyCode.V).type(KeyCode.ENTER);
    }

    /**
     * Converts a string into JavaFX KeyCode objects, used to make the robot type.
     *
     * @param input The string to convert.
     * @return The JavaFX KeyCodes representing the string.
     */
    private static KeyCode[] getKeyCodes(String input) {
        List<KeyCode> keyCodes = new ArrayList<>();
        // Because apparently providing a function that converts string to an array of
        // KeyCode's is too hard.
        for (String s : input.split("")) {
            if (s.equals(".")) {
                keyCodes.add(KeyCode.PERIOD);
            } else if (s.equals("/")) {
                keyCodes.add(KeyCode.SLASH);
            } else if (Character.isUpperCase(s.charAt(0))) {
                keyCodes.add(KeyCode.CAPS);
                keyCodes.add(KeyCode.getKeyCode(s.toUpperCase()));
                keyCodes.add(KeyCode.CAPS);
            } else if (s.equals("-")) {
                keyCodes.add(KeyCode.SUBTRACT);
            } else if (s.equals("_")) {
                keyCodes.add(KeyCode.UNDERSCORE);
            } else {
                keyCodes.add(KeyCode.getKeyCode(s.toUpperCase()));
            }
        }
        keyCodes.add(KeyCode.ENTER);
        return keyCodes.toArray(KeyCode[]::new);
    }

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
        // Define graph structure from Test file.
        CsvConfiguration.getInstance().loadNewConfig("src/test/resources/TestCommunications.json");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.requestFocus();

        stage = primaryStage;
        stage.setMaximized(true);
        HomeView v = new HomeView(primaryStage);
        stage.show();

        // Overriding graph configuration to support Serial communications with Simulation
        v.getGraphView().updateGraphStructureDefinition("graphSim", true);
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
     * Checks the visibility of the graphs, much like Nathan's tests.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    @Order(1)
    public void check_visibility_of_graphs(FxRobot robot) {
        // Loop through all graphs to make sure they're visible.
        for (GraphType graphType: GraphMasterList.getInstance().getGraphs()) {
            RocketDataLineChart chart =
                    robot.lookup("#" + graphType.getGraphID()).queryAs(RocketDataLineChart.class);
            Assertions.assertThat(chart.isVisible());
        }
    }

    /**
     * Tests that the run simulations button actually opens up a JFileChooser,
     * lets you select a file, and then runs the simulation.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void test_run_simulation(FxRobot robot) {
        if (!runSimulation(robot, fullyCorrectTestData)) {
            fail("Failed to run simulation - Alert popup found.");
        }

        checkGraphValues(robot, TEST_DATA);
    }

    /**
     * Tests that when you run a simulation while one is already running, it will
     * stop the previous simulation and run the new one.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void test_running_simulation_while_simulation_running(FxRobot robot) {
        // NOTE: The 8 seconds waited while checking for an alert pop is not enough time to run this simulation.
        if (!runSimulation(robot, fullyCorrectRocketData)) {
            fail("Failed to run simulation - Alert popup found.");
        }

        // Also note that these two files are actually different.

        // Run another simulation while one is already going
        if (!runSimulation(robot, fullyCorrectTestData)) {
            fail("Failed to run simulation - Alert popup found.");
        }

        checkGraphValues(robot, TEST_DATA);
    }

    /**
     * Tests that an alert popup is shown when attempting to run simulations with invalid simulation
     * files.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void test_running_simulation_with_invalid_file(FxRobot robot) {
        if (runSimulation(robot, invalidJSONFile)) {
            fail("Alert popup not shown when it should have!");
        }
    }

    /**
     * Test to see if the buttons will highlight the graphs.
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_highlight_graphs(FxRobot robot) {
        for (GraphType g : GraphMasterList.getInstance().getGraphs()) {

            String btnId = "#btn" + g.getLabel().replace(" ", "");
            String graphId = "#" + g.getGraphID();
            checkHighlight(btnId, graphId, robot);
        }
    }
}

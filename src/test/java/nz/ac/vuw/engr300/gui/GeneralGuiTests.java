package nz.ac.vuw.engr300.gui;

import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.model.RocketData;
import nz.ac.vuw.engr300.communications.model.RocketEvent;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.views.HomeView;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Tim Salisbury
 */
@ExtendWith(ApplicationExtension.class)
public class GeneralGuiTests {
    private static final List<RocketStatus> TEST_DATA;
    static {
        //Load in test data
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData("src/test/resources/FullyCorrectTestData.csv");
        TEST_DATA = importer.getData().stream().filter(data -> data instanceof RocketStatus)
                .map(data -> (RocketStatus)data).collect(Collectors.toList());
    }

    /**
     * Start the application UI. This is run before each test.
     *
     * @param stage Injected stage parameter to load the GUI inside.
     */
    @Start
    private static void start(Stage stage) {
        new HomeView(stage);
    }


    /**
     * Checks the visibility of the graphs, much like Nathan's tests.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void check_visibility_of_graphs(FxRobot robot){
        RocketDataLineChart velocityChart = robot.lookup("#lineChartVel").queryAs(RocketDataLineChart.class);
        RocketDataLineChart altitudeChart = robot.lookup("#lineChartAltitude").queryAs(RocketDataLineChart.class);
        RocketDataLineChart accelerationChart = robot.lookup("#lineChartAcceleration").queryAs(RocketDataLineChart.class);
        Assertions.assertThat(velocityChart.isVisible());
        Assertions.assertThat(altitudeChart.isVisible());
        Assertions.assertThat(accelerationChart.isVisible());

    }

    /**
     * Tests that the run simulations button actually opens up a JFileChooser,
     * lets you select a file, and then runs the simulation.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void test_run_simulation(FxRobot robot) {
        runSimulation(robot, "../../test/resources/FullyCorrectTestData.csv", 500);

        checkGraphValues(robot, TEST_DATA);
    }

    /**
     * Tests that when you run a simulation while one is already running, it will
     * stop the previous simulation and run the new one.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void test_running_simulation_while_simulation_running(FxRobot robot){
        // NOTE: 250ms is not enough time for this simulation to run
        runSimulation(robot, "../../test/resources/FullyCorrectRocketData.csv", 250);

        // Also note that these two files are actually different.

        // Run another simulation while one is already going
        runSimulation(robot, "../../test/resources/FullyCorrectTestData.csv", 500);

        checkGraphValues(robot, TEST_DATA);
    }

    @Test
    public void test_running_simulation_with_invalid_file(FxRobot robot){
        runSimulation(robot, "../../test/resources/InvalidJsonFile.json", 200);

        FxAssert.verifyThat("Failed to import simulation data!", Node::isVisible);
        FxAssert.verifyThat("File provided does not contain a header line!", Node::isVisible);

        robot.clickOn("OK");
    }

    /**
     * Checks that the graphs displayed by the UI have been populated with the {@code expected}
     * Rocket data.
     *
     * @param robot     The robot injected to run tests.
     * @param expected  The expected data that the graphs should be populated with.
     */
    private static void checkGraphValues(FxRobot robot, List<RocketStatus> expected){
        List<XYChart.Data<Number, Number>> velocityChartData = robot.lookup("#lineChartVel").queryAs(RocketDataLineChart.class).getData().get(0).getData();
        List<XYChart.Data<Number, Number>>  altitudeChartData = robot.lookup("#lineChartAltitude").queryAs(RocketDataLineChart.class).getData().get(0).getData();
        List<XYChart.Data<Number, Number>>  accelerationChartData = robot.lookup("#lineChartAcceleration").queryAs(RocketDataLineChart.class).getData().get(0).getData();

        int expectedDataSize = expected.size();
        assertEquals(expectedDataSize, velocityChartData.size());
        assertEquals(expectedDataSize, altitudeChartData.size());
        assertEquals(expectedDataSize, accelerationChartData.size());

        //Now check all the values displayed by the graph are those in the simulation!
        for(int i = 0; i < expectedDataSize; i++){
            assertEquals(expected.get(i).getTime(), velocityChartData.get(i).getXValue());
            assertEquals(expected.get(i).getTime(), altitudeChartData.get(i).getXValue());
            assertEquals(expected.get(i).getTime(), accelerationChartData.get(i).getXValue());

            assertEquals(expected.get(i).getTotalVelocity(), velocityChartData.get(i).getYValue());
            assertEquals(expected.get(i).getAltitude(), altitudeChartData.get(i).getYValue());
            assertEquals(expected.get(i).getTotalAcceleration(), accelerationChartData.get(i).getYValue());
        }
    }

    /**
     * Runs the simulation provided at {@code simulationFile} for {@code simulationRunTime}ms.
     *
     * @param robot                 The robot injected to run tests.
     * @param simulationFile        The simulation we wish to run.
     * @param simulationRunTime     How long we should let the simulation run for.
     */
    private static void runSimulation(FxRobot robot, String simulationFile, long simulationRunTime){
        try {
            // Wait for UI to resize, otherwise it seems to miss the button?
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }
        robot.clickOn("#btnRunSim");
        try {
            //Let the JFileChooser open, this isn't done on the main thread so we need to sleep.
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }
        //As the JFileChooser can not be tested by TestFX, we must make the robot type for us.
        copyPasteString(robot, simulationFile);

        try {
            //Let the simulation run
            Thread.sleep(simulationRunTime);
        } catch (InterruptedException ignored) { }
    }

    /**
     * Copies {@code string} to the ClipBoard and pastes it in the focused InputBox,
     * whatever that may be.
     *
     * @param robot     The robot injected to run tests.
     * @param string    The string to copy and paste.
     */
    private static void copyPasteString(FxRobot robot, String string){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, stringSelection);

        robot.press(KeyCode.CONTROL, KeyCode.V).release(KeyCode.CONTROL, KeyCode.V).type(KeyCode.ENTER);
    }

    /**
     * Converts a string into JavaFX KeyCode objects, used to make the robot type.
     *
     * @param input The string to convert.
     * @return      The JavaFX KeyCodes representing the string.
     */
    private static KeyCode[] getKeyCodes(String input){
        List<KeyCode> keyCodes = new ArrayList<>();
        // Because apparently providing a function that converts string to an array of
        // KeyCode's is too hard.
        for(String s : input.split("")){
            if(s.equals(".")){
                keyCodes.add(KeyCode.PERIOD);
            }else if(s.equals("/")) {
                keyCodes.add(KeyCode.SLASH);
            }else if(Character.isUpperCase(s.charAt(0))){
                keyCodes.add(KeyCode.CAPS);
                keyCodes.add(KeyCode.getKeyCode(s.toUpperCase()));
                keyCodes.add(KeyCode.CAPS);
            }else{
                keyCodes.add(KeyCode.getKeyCode(s.toUpperCase()));
            }
        }
        keyCodes.add(KeyCode.ENTER);
        return keyCodes.toArray(KeyCode[]::new);
    }
}

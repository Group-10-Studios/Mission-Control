package nz.ac.vuw.engr300.gui;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
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
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

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
        robot.clickOn("#btnRunSim");
        try {
            //Let the JFileChooser open, this isn't done on the main thread so we need to sleep.
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }
        //As the JFileChooser can not be tested by TestFX, we must make the robot type for us.
        robot.type(getKeyCodes("../../test/resources/FullyCorrectTestData.csv"));
        try {
            //Let the simulation run
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }

        RocketDataLineChart velocityChart = robot.lookup("#lineChartVel").queryAs(RocketDataLineChart.class);
        RocketDataLineChart altitudeChart = robot.lookup("#lineChartAltitude").queryAs(RocketDataLineChart.class);
        RocketDataLineChart accelerationChart = robot.lookup("#lineChartAcceleration").queryAs(RocketDataLineChart.class);

        //Now check all the values displayed by the graph are those in the simulation!
        for(int i = 0; i < TEST_DATA.size(); i++){
            assertEquals(TEST_DATA.get(i).getTime(), velocityChart.getData().get(0).getData().get(i).getXValue());
            assertEquals(TEST_DATA.get(i).getTime(), altitudeChart.getData().get(0).getData().get(i).getXValue());
            assertEquals(TEST_DATA.get(i).getTime(), accelerationChart.getData().get(0).getData().get(i).getXValue());

            assertEquals(TEST_DATA.get(i).getTotalVelocity(), velocityChart.getData().get(0).getData().get(i).getYValue());
            assertEquals(TEST_DATA.get(i).getAltitude(), altitudeChart.getData().get(0).getData().get(i).getYValue());
            assertEquals(TEST_DATA.get(i).getTotalAcceleration(), accelerationChart.getData().get(0).getData().get(i).getYValue());
        }
    }

    /**
     * Converts a string into JavaFX KeyCode objects, used to make the robot type.
     *
     * @param input The string to convert.
     * @return      The JavaFX KeyCodes representing the string.
     */
    private KeyCode[] getKeyCodes(String input){
        List<KeyCode> keyCodes = new ArrayList<>();
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

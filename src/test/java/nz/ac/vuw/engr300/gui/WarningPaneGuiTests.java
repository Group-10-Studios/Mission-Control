package nz.ac.vuw.engr300.gui;

import javafx.scene.control.ListView;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.views.HomeView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ApplicationExtension.class)
public class WarningPaneGuiTests {

    private static final String fullyCorrectTestData =
            new File("src/test/resources/FullyCorrectTestData.csv").getAbsolutePath();

    private Stage stage;


    /**
     * Start the application UI. This is run before each test.
     *
     * @param stage Injected stage parameter to load the GUI inside.
     */
    @Start
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        HomeView v = new HomeView(stage);
    }


    /**
     * Presses the arm/disarm button.
     *
     * @param robot          The robot injected to run tests.
     * @param simulationFile The simulation we wish to run.
     */
    private static boolean pressArm(FxRobot robot, String simulationFile) {
        if (!GeneralGuiTests.checkAndClickOnNodeWithPopup(robot, "#btnArm")) {
            fail("Arm button not found!");
        }
        //        WaitForAsyncUtils.waitForFxEvents(5);

        //        GeneralGuiTests.copyPasteString(robot, simulationFile);
        //        WaitForAsyncUtils.waitForFxEvents();

        //        return !GeneralGuiTests.checkForAlertPopup(robot);
        return true;
    }

    /**
     * Tests that we are able to run the Go button and then run a simulation, and a rocket alert comes up..
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_press_go(FxRobot robot) {
        ListView rocketList = robot.lookup("#rocketEventList").queryAs(ListView.class);
        int initialSize = rocketList.getItems().size();
        if (!pressArm(robot, fullyCorrectTestData)) {
            fail("Failed to press go");
        }
        assertTrue(rocketList.getItems().size() > initialSize);
    }

    /**
     * Tests that we are able to press the No Go button and then a rocket alert comes up.
     *
     * @param robot The robot injected to run tests.
     */
    @Test
    public void test_press_no_go(FxRobot robot) {
        ListView rocketList = robot.lookup("#rocketEventList").queryAs(ListView.class);
        int initialSize = rocketList.getItems().size();
        if (!pressDisarm(robot, fullyCorrectTestData)) {
            fail("Failed to press no go");
        }
        assertTrue(rocketList.getItems().size() > initialSize);
    }
}

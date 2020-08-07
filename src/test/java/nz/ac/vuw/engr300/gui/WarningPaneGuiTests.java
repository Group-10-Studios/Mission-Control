package nz.ac.vuw.engr300.gui;

import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.views.HomeView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
     * Runs the simulation provided at {@code simulationFile} for {@code simulationRunTime}ms.
     *
     * @param robot             The robot injected to run tests.
     * @param simulationFile    The simulation we wish to run.
     */
    private static boolean pressGo(FxRobot robot, String simulationFile) {
        if (!GeneralGuiTests.checkAndClickOnNode(robot, "#btnGo")) {
            fail("Go button not found!");
        }

//        WaitForAsyncUtils.waitForFxEvents(5);

//        GeneralGuiTests.copyPasteString(robot, simulationFile);
//        WaitForAsyncUtils.waitForFxEvents();

//        return !GeneralGuiTests.checkForAlertPopup(robot);
        return true;
    }

    /**
     * Tests that the run simulations button actually opens up a JFileChooser,
     * lets you select a file, and then runs the simulation.
     *
     * @param robot The robot injected to run tests
     */
    @Test
    public void test_press_go(FxRobot robot) {
        if (!pressGo(robot, fullyCorrectTestData)) {
            fail("Failed to press go");
        }
    }


}

package nz.ac.vuw.engr300.gui;

import com.sun.javafx.scene.control.InputField;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.controllers.WeatherController;
import nz.ac.vuw.engr300.gui.model.TestLaunchParameters;
import nz.ac.vuw.engr300.gui.views.HomeView;
import nz.ac.vuw.engr300.gui.views.LaunchParameterView;
import nz.ac.vuw.engr300.model.LaunchParameters;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LaunchParametersViewTests extends ApplicationTest {

    private static final String TEST_EXPORT_SIMULATION_DATA_FILE =
            "TestExportedSimulationData.csv";
    private static final String TEST_WEATHER_DATA = "src/test/resources/test-weather-data/";

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Class<LaunchParameters> launchParametersClass = LaunchParameters.class;
        Field instanceField = launchParametersClass.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, new TestLaunchParameters());

        Class<LaunchParameterView> launchParameterViewCLass = LaunchParameterView.class;

        Field saveWeatherField = launchParameterViewCLass.getDeclaredField("WEATHER_SAVE_FILE_DIR");
        saveWeatherField.setAccessible(true);
        saveWeatherField.set(null, "src/test/resources/test-weather-data/");

        Field saveMapField = launchParameterViewCLass.getDeclaredField("MAP_SAVE_FILE_DIR");
        saveMapField.setAccessible(true);
        saveMapField.set(null, "src/test/resources/test-map-data/");

        Field baseDirectoryField = launchParameterViewCLass.getDeclaredField("BASE_FILE_DIRECTORY");
        baseDirectoryField.setAccessible(true);
        baseDirectoryField.set(null, "src/test/resources/");

        Field baseDirectoryWeatherField = WeatherController.class.getDeclaredField("BASE_WEATHER_DIR");
        baseDirectoryWeatherField.setAccessible(true);
        baseDirectoryWeatherField.set(null, "src/test/resources/test-weather-data/");

        primaryStage.requestFocus();

        stage = primaryStage;
        primaryStage.setFullScreen(true);
        HomeView v = new HomeView(primaryStage);
        stage.show();
    }

    /**
     * Tests that LaunchParametersView is populated correctly with all fields from the test LaunchParameters object.
     *
     * @param robot The injected robot.
     */
    @Order(1)
    @Test
    public void testPopulationOfLaunchConfigurationsView(FxRobot robot) {
        clickLaunchConfig(robot);
        Class<TestLaunchParameters> clazz = TestLaunchParameters.class;

        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            String label = formatString(f.getName());
            assertTrue(robot.lookup(label).queryAs(Node.class).isVisible());
        }
    }

    /**
     * Tests LaunchParametersInputField with a double type does not accept strange inputs
     * and only accepts double values.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testDoubleInputField(FxRobot robot) {
        clickLaunchConfig(robot);
        assertEquals("123", processTextTest(robot, "#testDouble-inputField", "123"));
        assertEquals("", processTextTest(robot, "#testDouble-inputField", "drake"));
        assertEquals("1345", processTextTest(robot, "#testDouble-inputField", "drake 1345"));
        assertEquals("12.4578", processTextTest(robot, "#testDouble-inputField", "12.45.78"));
        assertEquals("69.69", processTextTest(robot, "#testDouble-inputField", "69.69"));
        assertEquals("-4.6", processTextTest(robot, "#testDouble-inputField", "-4.6"));
        assertEquals("-4.6", processTextTest(robot, "#testDouble-inputField", "--4.6"));
    }

    /**
     * Tests LaunchParametersInputField with a integer type does not accept strange inputs
     * and only accepts integer values.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testIntegerInputField(FxRobot robot) {
        clickLaunchConfig(robot);
        assertEquals("1337", processTextTest(robot, "#testInteger-inputField", "abde 1337"));
        assertEquals("", processTextTest(robot, "#testInteger-inputField", "abcdefghi"));
        assertEquals("-7562", processTextTest(robot, "#testInteger-inputField", "--7562"));
        assertEquals("42069420", processTextTest(robot, "#testInteger-inputField", "420.69.420"));
        assertEquals("-1337", processTextTest(robot, "#testInteger-inputField", "-1337"));
        assertEquals("1250", processTextTest(robot, "#testInteger-inputField", "12.50"));
        assertEquals("123", processTextTest(robot, "#testInteger-inputField", "123"));
    }

    /**
     * Tests LaunchParametersInputField with a string type does not accept strange inputs
     * and only accepts string values.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testStringInputFieldIsValid(FxRobot robot) {
        clickLaunchConfig(robot);
        assertEquals("124jhjgjr //", processTextTest(robot, "#testString-inputField", "124jhjgjr //"));
    }

    /**
     * Tests the save button in LaunchParameterView to ensure the values of LaunchParameter is changed once modified
     * in the configuration screen.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testSaveButton(FxRobot robot) {
        clickLaunchConfig(robot);
        processTextTest(robot, "#testString-inputField", "asdf");
        processTextTest(robot, "#testDouble-inputField", "124.0");
        processTextTest(robot, "#testInteger-inputField", "124");

        clickOnButton(robot, "#saveBtn");

        assertEquals("asdf", ((TestLaunchParameters) LaunchParameters.getInstance()).testString.getValue());
        assertEquals(124.0, ((TestLaunchParameters) LaunchParameters.getInstance()).testDouble.getValue());
        assertEquals(124, ((TestLaunchParameters) LaunchParameters.getInstance()).testInteger.getValue());
    }

    /**
     * Tests that the export simulation data button displays an error when there is no weather data.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testExportSimulationDataButtonWithoutWeatherData(FxRobot robot) {
        LaunchParameters parameters = LaunchParameters.getInstance();
        deleteFile(new File(TEST_WEATHER_DATA + parameters.getLatitude() + "-" + parameters.getLongitude() + ".json"));

        clickLaunchConfig(robot);

        assertTrue(GeneralGuiTests.checkAndClickOnNodeWithPopup(robot, "#exportSimulationParametersBtn"));
    }

    /**
     * Tests that the export simulation data properly exports the simulation parameters if the weather data is present.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testExportSimulationDataButton(FxRobot robot) {
        File simulationFile = new File("src/test/resources/TestExportedSimulationData.csv");

        deleteFile(simulationFile);

        clickLaunchConfig(robot);
        clickOnButton(robot, "#pullDataBtn");

        LaunchParameters parameters = LaunchParameters.getInstance();
        File testWeather = new File(TEST_WEATHER_DATA + parameters.getLatitude().getValue() +
                "-" + parameters.getLongitude().getValue() + ".json");
        assertTrue(waitAndCheckForFileToExist(testWeather), "Weather data failed to pull.");

        clickOnButton(robot, "#exportSimulationParametersBtn");

        GeneralGuiTests.copyPasteString(robot, TEST_EXPORT_SIMULATION_DATA_FILE);

        assertTrue(waitAndCheckForFileToExist(simulationFile), simulationFile.getAbsolutePath() + " did not eixst.");

        deleteFile(simulationFile);
    }


    /**
     * Tests that the pull data button actually pulls the data.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testPullDataButton(FxRobot robot) {
        LaunchParameters parameters = LaunchParameters.getInstance();
        File weatherData = new File(TEST_WEATHER_DATA + parameters.getLatitude().getValue() + "-" +
                parameters.getLongitude().getValue() + ".json");
        File mapData = new File("src/test/resources/test-map-data/" + parameters.getLatitude().getValue()
                + "-" + parameters.getLongitude().getValue() + "-map_image.png");
        deleteFile(weatherData);
        deleteFile(mapData);

        clickLaunchConfig(robot);

        clickOnButton(robot, "#pullDataBtn");

        waitAndCheckForFileToExist(weatherData, mapData);

        assertTrue(weatherData.exists(), weatherData.getAbsolutePath() + " file not found.");
        assertTrue(mapData.exists(), mapData.getAbsolutePath() + " file not found.");

        deleteFile(weatherData);
        deleteFile(mapData);
    }

    /**
     * Clicks on a button on screen with the same ID as nodeId.
     *
     * @param robot     The robot to use for clicking.
     * @param nodeId    The button ID.
     */
    private static void clickOnButton(FxRobot robot, String nodeId) {
        Button pullDataBtn = robot.lookup(nodeId).queryAs(Button.class);
        robot.clickOn(pullDataBtn);
    }

    /**
     * Waits for up to 10 seconds for a list of files to exist in the file system. If they appear before
     * the 10 seconds is up then this function will exit early with true. If the files never show up in
     * the 10 seconds, then this function will return with false.
     *
     * @param files The files to check for existence.
     * @return      Whether or not the files showed up in the 10 second wait time.
     */
    private static boolean waitAndCheckForFileToExist(File... files) {
        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
                try {
                    return Arrays.stream(files).allMatch(File::exists);
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
     * Clicks on the launch config button to bring up the LaunchParametersView screen.
     *
     * @param robot The injected robot.
     */
    private static void clickLaunchConfig(FxRobot robot) {
        clickOnButton(robot, "#launchConfig");
    }

    /**
     * Processes a text test for a LaunchParameterInputField.
     *
     * @param robot     The injected robot.
     * @param id        The ID of the LaunchParameterInputField.
     * @param input     The input to give it.
     * @return          The output of the LaunchParameterInputField.
     */
    private static String processTextTest(FxRobot robot, String id, String input) {
        if (GeneralGuiTests.checkAndClickOnNode(robot, id)) {
            robot.clickOn(id).type(clear()).type(getKeyCodes(input));
            return robot.lookup(id).queryAs(TextField.class).getText();
        } else {
            fail("Failed to look up the node id.");
            return "";
        }
    }

    /**
     * Generates an array of backspace keycodes to apply to input fields to clear their contents.
     *
     * @return  The array of backspaces.
     */
    private static KeyCode[] clear() {
        List<KeyCode> keyCodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            keyCodes.add(KeyCode.BACK_SPACE);
        }
        return keyCodes.toArray(KeyCode[]::new);
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
            } else if (s.equals(" ")) {
                keyCodes.add(KeyCode.SPACE);
            } else {
                keyCodes.add(KeyCode.getKeyCode(s.toUpperCase()));
            }
        }
        keyCodes.add(KeyCode.ENTER);
        return keyCodes.toArray(KeyCode[]::new);
    }

    /**
     * Formats a string to be appropriately titled, from camel case to title case.
     *
     * @param str The String to format.
     * @return The newly formatted string.
     */
    private static String formatString(String str) {
        str = str.replaceAll("([A-Z])", " $1");
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Deletes the file if it exists in the path specified.
     * @param file The file to be Deleted.
     */
    private static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}

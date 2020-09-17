package nz.ac.vuw.engr300.gui;

import com.sun.javafx.scene.control.InputField;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LaunchParametersViewTests extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Class<LaunchParameters> launchParametersClass = LaunchParameters.class;
        Field instanceField = launchParametersClass.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, new TestLaunchParameters());

        Class<LaunchParameterView> launchParameterViewCLass = LaunchParameterView.class;
        Field saveFileField = launchParameterViewCLass.getDeclaredField("SAVE_FILE_PATH");
        saveFileField.setAccessible(true);

        saveFileField.set(null, "src/test/resources");

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
     * Tests LaunchParametersInputField with a string type does not accept strange inputs
     * and only accepts string values.
     *
     * @param robot The injected robot.
     */
    @Test
    public void testSaveButton(FxRobot robot) {
        clickLaunchConfig(robot);
        processTextTest(robot, "#testString-inputField", "asdf");
        processTextTest(robot, "#testDouble-inputField", "124.0");
        processTextTest(robot, "#testInteger-inputField", "124");
//        processTextTest(robot, "#testBoolean-inputField", "124");

        Button saveBtn = robot.lookup("#saveBtn").queryAs(Button.class);
        robot.clickOn(saveBtn);

        assertEquals("asdf", ((TestLaunchParameters) LaunchParameters.getInstance()).testString.getValue());
        assertEquals(124.0, ((TestLaunchParameters) LaunchParameters.getInstance()).testDouble.getValue());
        assertEquals(124, ((TestLaunchParameters) LaunchParameters.getInstance()).testInteger.getValue());
    }

    /**
     * Clicks on the launch config button to bring up the LaunchParametersView screen.
     *
     * @param robot The injected robot.
     */
    private static void clickLaunchConfig(FxRobot robot) {
        Button launchConfigBtn = robot.lookup("#launchConfig").queryAs(Button.class);
        robot.clickOn(launchConfigBtn);
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
}

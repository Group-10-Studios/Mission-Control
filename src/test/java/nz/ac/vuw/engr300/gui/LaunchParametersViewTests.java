package nz.ac.vuw.engr300.gui;

import com.sun.javafx.scene.control.InputField;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.model.TestLaunchParameters;
import nz.ac.vuw.engr300.gui.views.HomeView;
import nz.ac.vuw.engr300.model.LaunchParameters;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LaunchParametersViewTests extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Class<LaunchParameters> clazz = LaunchParameters.class;
        Field instanceField = clazz.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, new TestLaunchParameters());

        primaryStage.requestFocus();

        stage = primaryStage;
        stage.setMaximized(true);
        HomeView v = new HomeView(primaryStage);
        stage.show();
    }

    @Test
    public void testDoubleInputField_int(FxRobot robot) {
        clickLaunchConfig(robot);
        String input = "123";
        assertEquals(input, processTextTest(robot, "#testDouble-inputField", input));
    }

    @Test
    public void testDoubleInputField_string(FxRobot robot) {
        clickLaunchConfig(robot);
        String input = "drake";
        assertEquals("", processTextTest(robot, "#testDouble-inputField", input));
    }

    private static void clickLaunchConfig(FxRobot robot) {
        Button launchConfigBtn = robot.lookup("#launchConfig").queryAs(Button.class);
        robot.clickOn(launchConfigBtn);
    }

    public static String processTextTest(FxRobot robot, String id, String input) {
        if (GeneralGuiTests.checkAndClickOnNode(robot, id)) {
            robot.clickOn(id).type(clear(10)).type(getKeyCodes(input));
            return robot.lookup(id).queryAs(TextField.class).getText();
        } else {
            fail("Failed to look up the node id.");
            return "";
        }
    }

    private static KeyCode[] clear(int x){
        List<KeyCode> keyCodes = new ArrayList<>();
        for(int i = 0; i < x; i++){
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
            } else {
                keyCodes.add(KeyCode.getKeyCode(s.toUpperCase()));
            }
        }
        keyCodes.add(KeyCode.ENTER);
        return keyCodes.toArray(KeyCode[]::new);
    }
}

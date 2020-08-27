package nz.ac.vuw.engr300.gui;

import javafx.scene.control.Button;
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
    public void test(FxRobot robot) {
        clickLaunchConfig(robot);
    }

    private static void clickLaunchConfig(FxRobot robot) {
        Button launchConfigBtn = robot.lookup("#launchConfig").queryAs(Button.class);
        robot.clickOn(launchConfigBtn);
    }
}

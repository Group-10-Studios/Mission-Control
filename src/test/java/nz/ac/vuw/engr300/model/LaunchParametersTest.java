package nz.ac.vuw.engr300.model;

import com.google.gson.Gson;
import nz.ac.vuw.engr300.gui.components.LaunchParameterInputField;
import nz.ac.vuw.engr300.gui.views.LaunchParameterView;
import nz.ac.vuw.engr300.importers.JsonExporter;
import nz.ac.vuw.engr300.importers.JsonImporter;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LaunchParametersTest {

    @Test
    public void test_CorrectJSONFileWithDefaultValues() {
        LaunchParameters actual = getLaunchParametersWithDefaultValues();
        LaunchParameters.getInstance().saveToJSONFile("src/test/resources/test-launch-parameters.json");
        Gson gson = new Gson();
        try {
            LaunchParameters testInstance = gson.fromJson(JsonImporter.load("src/test/resources/test-launch-parameters.json"),
                    LaunchParameters.class);
            assertEquals(actual, testInstance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Launch Parameters Test File not found.");
        }
    }

    @Test
    public void test_CorrectJSONFileWithNewValues() {
        LaunchParameters actual = getLaunchParametersWithDefaultValues();
        //Modified values
        actual.getMaximumAngleOfAttack().setValue(10.0);
        actual.getMaximumGroundHitSpeed().setValue(69.0);
        actual.getMaximumLaunchAngle().setValue(96.0);
        actual.getLatitude().setValue(13.0);
        actual.getLongitude().setValue(16.0);
        actual.getMaximumWindSpeed().setValue(12.0);
        actual.getMaximumParachuteDeploySpeed().setValue(3.0);

        actual.saveToJSONFile("src/test/resources/test-launch-parameters.json");
        Gson gson = new Gson();
        try {
            LaunchParameters testInstance = gson.fromJson(JsonImporter.load("src/test/resources/test-launch-parameters.json"),
                    LaunchParameters.class);
            assertEquals(actual, testInstance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Launch Parameters Test File not found.");
        }
    }
    public LaunchParameters getLaunchParametersWithDefaultValues() {
        return LaunchParameters.getInstance("some/invalid/filepath"); //Used to generate LaunchParameters object with default values
    }
}

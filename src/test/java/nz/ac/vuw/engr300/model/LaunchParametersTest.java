package nz.ac.vuw.engr300.model;

import com.google.gson.Gson;
import nz.ac.vuw.engr300.importers.JsonImporter;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LaunchParametersTest {

    /**
     * Creates a LaunchParameters object and writes to a file.
     * Loads that file and checks if the values are as expected.
     */
    @Test
    public void test_CorrectJsonFileWithDefaultValues() {
        LaunchParameters actual = LaunchParameters.getNewInstanceDefaultValues();
        actual.saveToJsonFile();
        Gson gson = new Gson();
        try {
            LaunchParameters testInstance =
                    gson.fromJson(JsonImporter.load("src/test/resources/test-launch-parameters.json"),
                    LaunchParameters.class);
            assertEquals(actual, testInstance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Launch Parameters Test File not found.");
        }
    }

    /**
     * Creates a LaunchParameters object and edit the values and writes to a file.
     * Loads that file and checks if the values are as expected.
     */
    @Test
    public void test_CorrectJsonFileWithNewValues() {
        LaunchParameters actual = LaunchParameters.getNewInstanceDefaultValues();
        //Modified values
        actual.getMaximumAngleOfAttack().setValue(10.0);
        actual.getMaximumGroundHitSpeed().setValue(69.0);
        actual.getMaximumLaunchAngle().setValue(96.0);
        actual.getLatitude().setValue(13.0);
        actual.getLongitude().setValue(16.0);
        actual.getMaximumWindSpeed().setValue(12.0);
        actual.getMaximumParachuteDeploySpeed().setValue(3.0);

        actual.saveToJsonFile();
        Gson gson = new Gson();
        try {
            LaunchParameters testInstance =
                    gson.fromJson(JsonImporter.load("src/test/resources/test-launch-parameters.json"),
                    LaunchParameters.class);
            assertEquals(actual, testInstance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Launch Parameters Test File not found.");
        }
    }
}

package nz.ac.vuw.engr300.gui;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.controllers.WeatherController;
import nz.ac.vuw.engr300.gui.views.HomeView;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class WeatherGuiTests {
    private Stage stage;

    /**
     * Start the application UI. This is run before each test.
     *
     * @param stage Injected stage parameter to load the GUI inside.
     */
    @Start
    public void start(Stage stage) {
        this.stage = stage;
        HomeView v = new HomeView(stage);
    }

    /**
     * Test retrieving a good set of weather data. Ensure that the weather information is updated with the
     * values and they are accurate.
     *
     * @param robot FxRobot injected to run GUI tests.
     */
    @Test
    public void test_UpdatingGoodWeather(FxRobot robot) {
        updateWeatherData("GoodWeather.json");

        WeatherData expectedData = WeatherController.getInstance().getWeatherData();

        checkHasExpectedData(robot, expectedData.getHumidity(), "humidity");
        checkHasExpectedData(robot, expectedData.getPressure(), "airpressure");
        checkHasExpectedData(robot, expectedData.getTemp(), "weathertemp");
        checkHasExpectedData(robot, expectedData.getWindSpeed(), "windspeed");
    }

    /**
     * Set the application to use the specified testWeatherFile.
     *
     * @param testWeatherFileName JSON file name to be used within this test.
     */
    private void updateWeatherData(String testWeatherFileName) {
        try {
            WeatherController.getInstance().setWeatherData("src/test/resources/test-weather-data/"
                    + testWeatherFileName);
        } catch (FileNotFoundException e) {
            fail("Test data file <" + testWeatherFileName
                    + "> was not found within src/test/resources/test-weather-data");
        }

        // Force a sleep for a period to wait for the GUI thread to update.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail("Sleep waiting for GUI update was interrupted");
        }
    }

    /**
     * Check that the expected value for the type of weather is present on the GUI in the weather information panel.
     * This will check for a GUI Label, construct what is expected in the label text and verify it matches.
     *
     * @param robot FxRobot to find the label on the GUI.
     * @param expectedValue Expected Value to be seen in the GUI for this type of weather.
     * @param typeOfWeather String type of weather to determine how to build the label, and for finding the label by ID.
     */
    private void checkHasExpectedData(FxRobot robot, double expectedValue, String typeOfWeather) {
        String expectedString = "";
        switch (typeOfWeather) {
            case "airpressure": {
                expectedString = "Air Pressure: " + roundDp(expectedValue, 1) + " mb";
                break;
            }
            case "humidity": {
                expectedString = "Humidity: " + roundDp(expectedValue, 1) + " %";
                break;
            }
            case "weathertemp": {
                double temp = roundDp((expectedValue - 273.15), 1);
                expectedString = "Temperature: " + temp + " degrees";
                break;
            }
            case "windspeed": {
                double windSpeed = roundDp((expectedValue * 3.6), 2);
                expectedString = "Windspeed: " + windSpeed + " km/h";
                break;
            }
            default: {
                fail("Invalid typeOfWeather provided <" + typeOfWeather + "> with no support to handle");
            }
        }

        String valueOnGui = robot.lookup("#WeatherInfo-" + typeOfWeather).queryAs(Label.class).getText();

        assertEquals(expectedString, valueOnGui, "GUI value did not match <" + expectedString + ">");
    }

    /**
     * Round the value to the specified number of decimal places.
     *
     * @param value Value to be rounded.
     * @param dp Number of decimal places to round to.
     * @return Rounded double to the specified number of DP.
     */
    private double roundDp(double value, int dp) {
        double roundingFactor = Math.pow(10, dp);
        return Math.round(value * roundingFactor) / roundingFactor;
    }
}
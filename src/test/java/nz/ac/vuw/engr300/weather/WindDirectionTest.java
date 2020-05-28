package nz.ac.vuw.engr300.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WindDirection;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify the Weather Direction is working as expected with imported
 * data and generated data.
 * 
 * @author Nathan Duckett
 *
 */
class WindDirectionTest {
    /**
     * Test providing a static value of north west and verifying it is correct.
     */
    @Test
    void test_northWest() {
        WindDirection dir = WindDirection.getFromWindAngle(315);

        assertEquals(WindDirection.NORTHWEST, dir);
        assertEquals("NW", dir.toString());
    }

    /**
     * Test providing a static value of north and verifying it is correct.
     */
    @Test
    void test_north() {
        WindDirection dir = WindDirection.getFromWindAngle(0);

        assertEquals(WindDirection.NORTH, dir);
        assertEquals("N", dir.toString());
    }

    /**
     * Test providing a static value of north east and verifying it is correct.
     */
    @Test
    void test_northEast() {
        WindDirection dir = WindDirection.getFromWindAngle(45);

        assertEquals(WindDirection.NORTHEAST, dir);
        assertEquals("NE", dir.toString());
    }

    /**
     * Test providing a static value of east and verifying it is correct.
     */
    @Test
    void test_east() {
        WindDirection dir = WindDirection.getFromWindAngle(90);

        assertEquals(WindDirection.EAST, dir);
        assertEquals("E", dir.toString());
    }

    /**
     * Test providing a static value of south east and verifying it is correct.
     */
    @Test
    void test_southEast() {
        WindDirection dir = WindDirection.getFromWindAngle(135);

        assertEquals(WindDirection.SOUTHEAST, dir);
        assertEquals("SE", dir.toString());
    }

    /**
     * Test providing a static value of south and verifying it is correct.
     */
    @Test
    void test_south() {
        WindDirection dir = WindDirection.getFromWindAngle(180);

        assertEquals(WindDirection.SOUTH, dir);
        assertEquals("S", dir.toString());
    }

    /**
     * Test providing a static value of south west and verifying it is correct.
     */
    @Test
    void test_southWest() {
        WindDirection dir = WindDirection.getFromWindAngle(225);

        assertEquals(WindDirection.SOUTHWEST, dir);
        assertEquals("SW", dir.toString());
    }

    /**
     * Test providing a static value of west and verifying it is correct.
     */
    @Test
    void test_west() {
        WindDirection dir = WindDirection.getFromWindAngle(270);

        assertEquals(WindDirection.WEST, dir);
        assertEquals("W", dir.toString());
    }

    /**
     * Test the value of the imported data is matching the correct direction.
     */
    @Test
    void test_imported_data_01() {
        try {
            WeatherImporter weatherData = new WeatherImporter("src/test/resources/WeatherTestFile.json");
            // Get the Direction based on the first entry in the weather data file.
            WindDirection dir = WindDirection.getFromWindAngle(weatherData.getWeather(0).getWindAngle());

            // Check matching
            assertEquals(WindDirection.NORTHWEST, dir);
            assertEquals("NW", dir.toString());
        } catch (FileNotFoundException e) {
            fail("Unable to find the WeatherTestFile.json test file");
        }
    }
}

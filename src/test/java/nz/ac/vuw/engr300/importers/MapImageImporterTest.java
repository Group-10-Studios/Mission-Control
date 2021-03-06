package nz.ac.vuw.engr300.importers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import nz.ac.vuw.engr300.exceptions.TomTomRequestFailedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests to verify that the imported image from TomTom is correct and valid.
 * 
 * @author Ahad Rahman
 */
public class MapImageImporterTest {

    static String API_KEY;

    /**
     * Initialize the API_KEY so that the test cases can use it during execution.
     */
    @BeforeAll
    public static void initTestKeys() {
        try {
            API_KEY = KeyImporter.getKey("maps");
        } catch (KeyNotFoundException e) {
            System.err.println("Can't run tests as the 'keys.json' file is missing");
            System.exit(1);
        }
    }

    /**
     * Test whether the imported image has the correct dimensions.
     * 
     * @throws IOException IOException thrown when can't find the map.
     */
    @Test
    public void test_ImageCorrectDimensions() throws IOException {
        double latitude = -41.300442;
        double longitude = 174.780319;
        int zoomLevel = 17; // Number between 0-22
        int imageWidth = 512; // Width of the output file
        int imageHeight = 512; // Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            String filename = latitude + "-" + longitude + "-zoom_level" + zoomLevel + "-map_image.png";
            BufferedImage img = ImageIO.read(new File("src/main/resources/map-data/" + filename));
            assertEquals(img.getWidth() == imageWidth, img.getHeight() == imageHeight);
        } catch (TomTomRequestFailedException e) {
            fail("API request to TomTom failed");
        }
    }

    /**
     * Test whether the imported image has the correct filename.
     */
    @Test
    public void test_ImageCorrectFilename() {
        double latitude = -40.303442;
        double longitude = 175.764319;
        int zoomLevel = 16; // Number between 0-22
        int imageWidth = 300; // Width of the output file
        int imageHeight = 300; // Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            String expectedFilepath = "src/main/resources/map-data/" + latitude + "-" + longitude
                    + "-zoom_level" + zoomLevel + "-map_image.png";
            File f = new File(expectedFilepath);
            assertEquals(true, f.exists());
        } catch (TomTomRequestFailedException e) {
            fail("API request to TomTom failed");
        }
    }

    /**
     * Expects an illegal argument exception as the latitude is outside the valid range.
     */
    @Test
    public void test_InvalidLatitude() {
        double latitude = -104.378432; // Latitude should be between -85 and 85
        double longitude = 175.764319;
        int zoomLevel = 16; // Number between 0-22
        int imageWidth = 300; // Width of the output file
        int imageHeight = 300; // Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            fail("Invalid latitude");
        } catch (IllegalArgumentException e) {
            assert (true);
        } catch (TomTomRequestFailedException e) {
            fail("API request to TomTom failed");
        }
    }

    /**
     * Expects an illegal argument exception as the longitude is outside the valid range.
     */
    @Test
    public void test_InvalidLongitude() {
        double latitude = -64.378432; // Latitude should be between -85 and 85
        double longitude = 195.764319; // Latitude should be between -180 and 180
        int zoomLevel = 16; // Number between 0-22
        int imageWidth = 300; // Width of the output file
        int imageHeight = 300; // Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            fail("Invalid longitude");
        } catch (IllegalArgumentException e) {
            assert (true);
        } catch (TomTomRequestFailedException e) {
            fail("API request to TomTom failed");
        }
    }

    /**
     * Expects an illegal argument exception as the zoom level is outside the valid range.
     */
    @Test
    public void test_InvalidZoomLevel() {
        double latitude = -40.303442;
        double longitude = 175.764319;
        int zoomLevel = 25; // Number between 0-22
        int imageWidth = 300; // Width of the output file
        int imageHeight = 300; // Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            fail("Invalid zoom level");
        } catch (IllegalArgumentException e) {
            assert (true);
        } catch (TomTomRequestFailedException e) {
            fail("API request to TomTom failed");
        }
    }

    /**
     * URL cannot contain non-ASCII characters, so by using chinese characters as the api key, an Error is thrown.
     */
    @Test
    public void test_InvalidCharacters() {
        double latitude = -40.303442;
        double longitude = 175.764319;
        int zoomLevel = 16; // Number between 0-22
        int imageWidth = 300; // Width of the output file
        int imageHeight = 300; // Height of the output file
        try {
            MapImageImporter.importImage("你好", latitude, longitude, zoomLevel, imageWidth, imageHeight);
            fail("Expected Error to be thrown on importImage due to invalid characters");
        } catch (TomTomRequestFailedException e) {
            assert (true);
        }
    }

}

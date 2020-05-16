package nz.ac.vuw.engr300.importers;

import java.awt.image.BufferedImage;
import java.io.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify that the imported image from TomTom is correct and valid
 * @author Ahad Rahman
 */
public class MapImageImporterTest {

    public static String API_KEY;
    
    /**
     * Initialize the API_KEY so that the test cases can use it during execution.
     */
    @BeforeAll
    public static void initTestKeys() {
    	try {
			API_KEY = KeyImporter.getKey("maps");
		} catch (FileNotFoundException e) {
			System.err.println("Can't run tests as the 'keys.json' file is missing");
			System.exit(1);
		}
    }

    /**
     * Test whether the imported image has the correct dimensions
     * @throws IOException
     */
    @Test
    public void test_ImageCorrectDimensions() throws IOException {
        double latitude = -41.300442;
        double longitude = 174.780319;
        int zoomLevel = 17; //Number between 0-22
        int imageWidth = 512; //Width of the output file
        int imageHeight = 512; //Height of the output file
        MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
        String filename = latitude+"-"+longitude+"-map_image.png";
        BufferedImage img = ImageIO.read(new File("src/main/resources/map-data/"+filename));
        assertEquals(img.getWidth() == imageWidth, img.getHeight() == imageHeight);
    }

    /**
     * Test whether the imported image has the correct filename
     */
    @Test
    public void test_ImageCorrectFilename(){
        double latitude = -40.303442;
        double longitude = 175.764319;
        int zoomLevel = 16; //Number between 0-22
        int imageWidth = 300; //Width of the output file
        int imageHeight = 300; //Height of the output file
        MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
        String expectedFilepath = "src/main/resources/map-data/"+latitude+"-"+longitude+"-map_image.png";
        File f = new File(expectedFilepath);
        assertEquals(true, f.exists());
    }

    @Test
    public void test_InvalidLatitude() {
        double latitude = -104.378432; //Latitude should be between -85 and 85
        double longitude = 175.764319;
        int zoomLevel = 16; //Number between 0-22
        int imageWidth = 300; //Width of the output file
        int imageHeight = 300; //Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            fail();
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    @Test
    public void test_InvalidLongitude() {
        double latitude = -64.378432; //Latitude should be between -85 and 85
        double longitude = 195.764319; //Latitude should be between -180 and 180
        int zoomLevel = 16; //Number between 0-22
        int imageWidth = 300; //Width of the output file
        int imageHeight = 300; //Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
            fail();
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    @Test
    public void test_InvalidZoomLevel() {
        double latitude = -40.303442;
        double longitude = 175.764319;
        int zoomLevel = 25; //Number between 0-22
        int imageWidth = 300; //Width of the output file
        int imageHeight = 300; //Height of the output file
        try {
            MapImageImporter.importImage(API_KEY, latitude, longitude, zoomLevel, imageWidth, imageHeight);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

}

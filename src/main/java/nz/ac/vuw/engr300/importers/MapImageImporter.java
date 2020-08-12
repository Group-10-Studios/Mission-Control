package nz.ac.vuw.engr300.importers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import org.apache.log4j.Logger;

/**
 * Returns and saves an image from TomTom's API. Requires a longitude and
 * latitude, as well the desired exported image's width and height and zoom
 * level.
 * 
 * @author Ahad Rahman
 */
public class MapImageImporter {
    private static final Logger LOGGER = Logger.getLogger(MapImageImporter.class);

    /**
     * Start to download a Map at the specified location.
     * 
     * @param args ARguments for Application.
     */
    public static void main(String[] args) {
        String apiKey = null;
        try {
            apiKey = KeyImporter.getKey("maps");
        } catch (KeyNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            System.out.println("Unable to retrieve the map as your keys.json file is missing");
            System.exit(1);
        }
        double latitude = -41.300442;
        double longitude = 174.780319;
        int zoomLevel = 17; // Number between 0-22
        int imageWidth = 512; // Width of the output file
        int imageHeight = 512; // Height of the output file
        importImage(apiKey, latitude, longitude, zoomLevel, imageWidth, imageHeight);
    }

    //TODO Doco
    public static void importImage(String apiKey, double latitude, double longitude) {
        importImage(apiKey, latitude, longitude, 17, 400, 400);
    }
    /**
     * Download an image with the following information to the application.
     * @param apiKey TomTom API key.
     * @param latitude Latitude for center of image.
     * @param longitude Longitude for center of image.
     * @param zoomLevel Zoom level of this image - default should be 17
     * @param imageWidth Image width required
     * @param imageHeight Image height required.
     */
    public static void importImage(String apiKey, double latitude, double longitude, int zoomLevel, int imageWidth,
            int imageHeight) {
        if (latitude < -85.0 || latitude > 85.0) {
            throw new IllegalArgumentException("Invalid latitude");
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Invalid longitude");
        }
        if (zoomLevel < 0 || zoomLevel > 22) {
            throw new IllegalArgumentException("Invalid zoom level");
        }
        try {
            String apiCall = "https://api.tomtom.com/map/1/staticimage?layer=basic&style=main&format=png&key=" + apiKey
                    + "&zoom=" + zoomLevel + "&center=" + longitude + "," + latitude + "&width=" + imageWidth
                    + "&height=" + imageHeight + "&viewUnified&language=NGT";
            URL imageUrl = new URL(apiCall);
            InputStream in = new BufferedInputStream(imageUrl.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            String filename = latitude + "-" + longitude + "-map_image.png";
            FileOutputStream fos = new FileOutputStream("src/main/resources/map-data/" + filename);
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            throw new Error("API request to TomTom failed");
        }
    }
}

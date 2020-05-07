package nz.ac.vuw.engr300.importers;

import java.io.*;
import java.net.URL;

/**
 * Returns and saves an image from TomTom's API.
 * Requires a longitude and latitude, as well the desired exported image's width and height and zoom level.
 * @author Ahad Rahman
 */
public class MapImageImporter {

    public static void main(String[] args) {
        String apiKey = KeyImporter.getKey("maps");
        double latitude = -141.300442;
        double longitude = 174.780319;
        int zoomLevel = 17; //Number between 0-22
        int imageWidth = 512; //Width of the output file
        int imageHeight = 512; //Height of the output file
        importImage(apiKey, latitude, longitude, zoomLevel, imageWidth, imageHeight);
    }

    public static boolean importImage(String apiKey, double latitude, double longitude, int zoomLevel, int imageWidth, int imageHeight) {
        boolean success = false;
        try {
            String apiCall = "https://api.tomtom.com/map/1/staticimage?layer=basic&style=main&format=png&key="+apiKey+"&zoom="+zoomLevel+"&center="+longitude+","+latitude+"&width="+imageWidth+"&height="+imageHeight+"&viewUnified&language=NGT";
            URL imageURL = new URL(apiCall);
            InputStream in = new BufferedInputStream(imageURL.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            String filename = latitude+"-"+longitude+"-map_image.png";
            FileOutputStream fos = new FileOutputStream("src/main/resources/map-data/"+filename);
            fos.write(response);
            fos.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}

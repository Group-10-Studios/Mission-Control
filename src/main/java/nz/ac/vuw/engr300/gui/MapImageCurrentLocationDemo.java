package nz.ac.vuw.engr300.gui;

import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class MapImageCurrentLocationDemo extends JPanel {

    public static final int MARKER_SIZE = 10;

    public static double centerLatitude = -41.227938; //Update this value
    public static double centerLongitude = 174.798772; //Update this value
    public static int graphicsWidth = 800;
    public static int graphicsHeight = 600;
    public static String filename;
    public static double angle;
    public static double hypotenuse;
    public static String API_KEY;

    static {
        try {
            API_KEY = KeyImporter.getKey("maps");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MapImageImporter.importImage(API_KEY, centerLatitude, centerLongitude, 17, graphicsWidth, graphicsHeight);
        filename = "src/main/resources/map-data/"+centerLatitude+"-"+centerLongitude+"-map_image.png";

        double newLatitude = -41.228890; //Update this value
        double newLongitude = 174.799470; //Update this value
        updateAngleDistanceInfo(newLatitude, newLongitude);

        JFrame frame= new JFrame();
        frame.getContentPane().add(new MapImageCurrentLocationDemo());
        frame.setSize(graphicsWidth, graphicsHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    private static void updateAngleDistanceInfo(double newLatitude, double newLongitude) {
        angle = angleBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
        hypotenuse = distanceBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
    }


    public void paint(Graphics g) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(filename));
            g.drawImage(img, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("HYP: " + hypotenuse);
        System.out.println("ANGLE: " + angle);
        g.setColor(Color.GREEN);
        g.fillOval(graphicsWidth /2 - (MARKER_SIZE /2), graphicsHeight /2 - (MARKER_SIZE /2), MARKER_SIZE, MARKER_SIZE); //Center
        double toMoveVertical = hypotenuse * Math.cos(Math.toRadians(angle));
        double toMoveHorizontal = hypotenuse * Math.sin(Math.toRadians(angle));
        System.out.println("METERS TO MOVE: " + toMoveHorizontal + ", " + toMoveVertical);
        System.out.println("PIXELS TO MOVE: " + pixelsToMove(toMoveHorizontal) + ", " + pixelsToMove(toMoveVertical));
        g.setColor(Color.RED);
        g.fillOval(graphicsWidth /2 - (MARKER_SIZE /2) + (int)pixelsToMove(toMoveHorizontal), graphicsHeight /2 - (MARKER_SIZE /2) - (int)pixelsToMove(toMoveVertical), MARKER_SIZE, MARKER_SIZE); //Center
    }

    /**
     * http://www.movable-type.co.uk/scripts/latlong.html
     * @param lat1
     * @param long1
     * @param lat2
     * @param long2
     * @return
     */
    public static double distanceBetweenTwoLocations(double lat1, double long1, double lat2, double long2) {
        double r = 6371e3;
        double theta1 = lat1 * Math.PI/180.0;
        double theta2 = lat2 * Math.PI/180.0;
        double deltaTheta = (lat2 - lat1) * Math.PI/180.0;
        double deltaLambda = (long2 - long1) * Math.PI/180.0;
        double a = Math.sin(deltaTheta/2.0) * Math.sin(deltaTheta/2.0) + Math.cos(theta1) * Math.cos(theta2) * Math.sin(deltaLambda/2.0) * Math.sin(deltaLambda/2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return r * c;
    }

    /**
     * https://stackoverflow.com/questions/3932502/calculate-angle-between-two-latitude-longitude-points
     * @param lat1
     * @param long1
     * @param lat2
     * @param long2
     * @return
     */
    public static double angleBetweenTwoLocations(double lat1, double long1, double lat2, double long2) {
        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 365 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }

    /**
     * https://developer.tomtom.com/maps-api/maps-api-documentation/zoom-levels-and-tile-grid
     * @param distance, the distance in meters
     * @return - the amount of pixels to move
     */
    public static double pixelsToMove(double distance) {
        //1 m = 0.8333 pixels for zoom level 17.
        //1.8 is a scale factor...Don't ask me why it works, it just does...
        return (1.8*(distance/0.8333));
    }
}

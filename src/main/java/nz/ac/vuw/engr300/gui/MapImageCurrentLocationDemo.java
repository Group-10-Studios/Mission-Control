package nz.ac.vuw.engr300.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


//        double latitude = -41.300442;
//        double longitude = 174.780319;
//        String filename = "src/main/resources/map-data/-41.300442-174.780319-map_image.png";
//        JFrame frame = new JFrame();
//        Canvas canvas = new MapImageCurrentLocationDemo();
//        canvas.setSize(550, 550);
//        frame.add(canvas);
//        frame.pack();
//        BufferedImage img = ImageIO.read(new File(filename));
//        ImageIcon icon = new ImageIcon(img);
//        frame.setLayout(new FlowLayout());
//        JLabel label = new JLabel();
//        label.setIcon(icon);
//        frame.add(label);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

public class MapImageCurrentLocationDemo extends JPanel {

    public static final int WIDTH = 512;
    public static final int HEIGHT = 512;
    public static final int MARKER_HEIGHT = 10;
    public static final int MARKER_WIDTH = 10;

    public void paint(Graphics g) {

        String filename = "src/main/resources/map-data/-41.300442-174.780319-map_image.png";
        BufferedImage img;
        try {
            img = ImageIO.read(new File(filename));
            g.drawImage(img, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double centerLatitude = -41.300442;
        double centerLongitude = 174.780319;
//        double newLatitude = -41.299716;
//        double newLongitude = 174.781139;'

        double newLatitude = -41.300290;
        double newLongitude = 174.781161;


        double angle = angleBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
        double hypotenuse = distanceBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
        System.out.println("HYP: " + hypotenuse);
        System.out.println("ANGLE: " + angle);
        g.setColor(Color.GREEN);
        g.fillOval(WIDTH/2 - (MARKER_WIDTH/2), HEIGHT/2 - (MARKER_HEIGHT/2), MARKER_WIDTH, MARKER_HEIGHT); //Center

//        if (angle > 0 && angle < 90) {
        double toMoveVertical = hypotenuse * Math.cos(Math.toRadians(angle));
        double toMoveHorizontal = hypotenuse * Math.sin(Math.toRadians(angle));
        System.out.println("METERS TO MOVE: " + toMoveHorizontal + ", " + toMoveVertical);
        System.out.println("PIXELS TO MOVE: " + pixelsToMove(toMoveHorizontal) + ", " + pixelsToMove(toMoveVertical));
        g.setColor(Color.RED);
        g.fillOval(WIDTH/2 - (MARKER_WIDTH/2) + (int)pixelsToMove(toMoveHorizontal), HEIGHT/2 - (MARKER_HEIGHT/2) - (int)pixelsToMove(toMoveVertical), MARKER_WIDTH, MARKER_HEIGHT); //Center
//        }


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

    public static double angleBetweenTwoLocations(double lat1, double long1, double lat2, double long2) {
        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }

    public static double pixelsToMove(double distance) {
        //Not sure which one to use
        return (distance*(WIDTH/235.0));
//        return (distance/(WIDTH/307.2));
//        return (distance/1.2);
    }


    public static double pixelsToMoveHorizontally(double distance) {
        return (distance/(WIDTH/307.2));
    }

    public static double pixelsToMoveVerically(double distance) {
        return (distance/(HEIGHT/307.2));
    }



    public static void main(String[] args){
        JFrame frame= new JFrame();
        frame.getContentPane().add(new MapImageCurrentLocationDemo());
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

//        System.out.println(distanceBetweenTwoLocations(-41.300442, 174.780319, -41.299716, 174.781139));
//        System.out.println(angleBetweenTwoLocations(-41.300442, 174.780319, -41.299716, 174.781139));
//        System.out.println(pixelsToMove(distanceBetweenTwoLocations(-41.300442, 174.780319, -41.299716, 174.781139)));
    }
}

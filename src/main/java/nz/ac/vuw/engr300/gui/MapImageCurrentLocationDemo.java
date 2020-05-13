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
    public static final int MARKER_HEIGHT = 20;
    public static final int MARKER_WIDTH = 20;

    public void paint(Graphics g) {

        String filename = "src/main/resources/map-data/-41.300442-174.780319-map_image.png";
        BufferedImage img;
        try {
            img = ImageIO.read(new File(filename));
            g.drawImage(img, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        -41.299652, 174.780667
//        (givenLat*heightOfContainerElement)/180
//        (givenLng*widthOfContainerElement)/360
        double latitude = -41.300442;
        double longitude = 174.780319;
        //-41.297928, 174.784660

        double graphicsX = (latitude * HEIGHT)/180.0;
        double graphicsY = (longitude * WIDTH)/360.0;
//        System.out.println(graphicsX);
//        System.out.println(graphicsY);

        g.setColor(Color.RED);
        g.fillOval((int) graphicsX - (MARKER_WIDTH/2), (int) graphicsY - (MARKER_HEIGHT/2), MARKER_WIDTH, MARKER_HEIGHT);

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

    public static int pixelsToMove(double distance) {
        return (int) (distance/1.2);
    }



    public static void main(String[] args){
        JFrame frame= new JFrame();
        frame.getContentPane().add(new MapImageCurrentLocationDemo());
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        System.out.println(distanceBetweenTwoLocations(-41.300442, 174.780319, -41.297928, 174.780667));
    }
}

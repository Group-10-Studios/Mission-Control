package nz.ac.vuw.engr300.gui.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.importers.KeyImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.CancellationException;

/**
 * A component for the main ui that shows the current location of the rocket
 *
 * @author Ahad Rahman
 */
public class RocketDataLocation extends Pane implements RocketGraph {
    private static final int MARKER_SIZE = 10;

    private Canvas canvas;
    private GraphType type;
    private double centerLatitude;
    private double centerLongitude;
    private String filename;
    private double angle;
    private double hypotenuse;
    public int graphicsWidth;
    public int graphicsHeight;
    private String api_key;

    public RocketDataLocation(double centerLatitude, double centerLongitude, int graphicsWidth, int graphicsHeight) {
        canvas = new Canvas(getWidth(), getHeight());
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.graphicsWidth = graphicsWidth;
        this.graphicsHeight = graphicsHeight;
        filename = "src/main/resources/map-data/" + centerLatitude + "-" + centerLongitude + "-map_image.png";
        try {
            this.api_key = KeyImporter.getKey("maps");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        updateAngleDistanceInfo(-41.228890, 174.799470);
    }

//
//    public void draw(Graphics g) {
//
//    }

    public void updateAngleDistanceInfo(double newLatitude, double newLongitude) {
        angle = angleBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
        hypotenuse = distanceBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
    }

    /**
     * http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param lat1  Latitude from
     * @param long1 Longitude from
     * @param lat2  Latitude to
     * @param long2 Longitude to
     * @return
     */
    public static double distanceBetweenTwoLocations(double lat1, double long1, double lat2, double long2) {
        double r = 6371e3;
        double theta1 = lat1 * Math.PI / 180.0;
        double theta2 = lat2 * Math.PI / 180.0;
        double deltaTheta = (lat2 - lat1) * Math.PI / 180.0;
        double deltaLambda = (long2 - long1) * Math.PI / 180.0;
        double a = Math.sin(deltaTheta / 2.0) * Math.sin(deltaTheta / 2.0)
                + Math.cos(theta1) * Math.cos(theta2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return r * c;
    }

    /**
     * https://stackoverflow.com/questions/3932502/calculate-angle-between-two-latitude-longitude-points
     *
     * @param lat1  Latitude from
     * @param long1 Longitude from
     * @param lat2  Latitude to
     * @param long2 Longitude to
     * @return
     */
    public static double angleBetweenTwoLocations(double lat1, double long1, double lat2, double long2) {
        double diffLong = (long2 - long1);

        double y = Math.sin(diffLong) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(diffLong);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 365 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }

    /**
     * https://developer.tomtom.com/maps-api/maps-api-documentation/zoom-levels-and-tile-grid
     *
     * @param distance the distance in meters
     * @return - the amount of pixels to move
     */
    public static double pixelsToMove(double distance) {
        // 1 m = 0.8333 pixels for zoom level 17.
        // 1.8 is a scale factor...Don't ask me why it works, it just does...
        return (1.8 * (distance / 0.8333));
    }

    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
    }

    @Override
    public GraphType getGraphType() {
        return type;
    }

    @Override
    public void clear() {

    }

    @Override
    protected void layoutChildren() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        Image img = new Image(filename);

//        try {
//            img = ImageIO.read(new File(filename));
//            g.drawImage(img);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        g.drawImage(img, 0, 0);
        g.setFill(Color.BLUE);
        g.fillOval(graphicsWidth / 2 - (MARKER_SIZE / 2), graphicsHeight / 2 - (MARKER_SIZE / 2), MARKER_SIZE,
                MARKER_SIZE); // Center
        double toMoveVertical = hypotenuse * Math.cos(Math.toRadians(angle));
        double toMoveHorizontal = hypotenuse * Math.sin(Math.toRadians(angle));
        g.setFill(Color.RED);
        g.fillOval(graphicsWidth / 2 - (MARKER_SIZE / 2) + (int) pixelsToMove(toMoveHorizontal),
                graphicsHeight / 2 - (MARKER_SIZE / 2) - (int) pixelsToMove(toMoveVertical),
                MARKER_SIZE, MARKER_SIZE); // Center
    }
}

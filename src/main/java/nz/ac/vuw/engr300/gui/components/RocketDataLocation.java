package nz.ac.vuw.engr300.gui.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;

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
    public double graphicsWidth;
    public double graphicsHeight;
    private String api_key;

    public RocketDataLocation(double centerLatitude, double centerLongitude, int imageWidth, int imageHeight) throws FileNotFoundException {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.api_key = KeyImporter.getKey("maps");
        MapImageImporter.importImage(api_key, centerLatitude, centerLongitude, 17, imageWidth, imageHeight);
        filename = "src/main/resources/map-data/" + centerLatitude + "-" + centerLongitude + "-map_image.png";
        canvas = new Canvas(getWidth(), getHeight());
        this.getChildren().add(canvas);
        widthProperty().addListener(e -> canvas.setWidth(getWidth()));
        heightProperty().addListener(e -> canvas.setHeight(getHeight()));
    }

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
        super.layoutChildren();
        GraphicsContext g = canvas.getGraphicsContext2D();
        Image img = new Image("file:" + filename);
        this.graphicsWidth = canvas.getWidth();
        this.graphicsHeight = canvas.getHeight();
        g.drawImage(img, 0, 0, this.graphicsWidth, this.graphicsHeight);

        g.setFill(Color.GREEN);
        g.fillOval(graphicsWidth / 2 - (MARKER_SIZE / 2), graphicsHeight / 2 - (MARKER_SIZE / 2), MARKER_SIZE,
                MARKER_SIZE); // Center
        double toMoveVertical = hypotenuse * Math.cos(Math.toRadians(angle));
        double toMoveHorizontal = hypotenuse * Math.sin(Math.toRadians(angle));
        g.setFill(Color.RED);
        double x = graphicsWidth / 2 - (MARKER_SIZE / 2) + (int) pixelsToMove(toMoveHorizontal);
        double y = graphicsHeight / 2 - (MARKER_SIZE / 2) - (int) pixelsToMove(toMoveVertical);
        int markerOffset = 40;

        g.fillOval(x, y, MARKER_SIZE, MARKER_SIZE); // Center
        g.strokeOval(x - markerOffset/2, y - markerOffset/2, MARKER_SIZE + markerOffset, MARKER_SIZE + markerOffset); // Center
    }
}

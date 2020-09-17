package nz.ac.vuw.engr300.gui.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import nz.ac.vuw.engr300.exceptions.TomTomRequestFailedException;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * A component for the main ui that shows the current location of the rocket.
 *
 * @author Ahad Rahman
 */
public class RocketDataLocation extends Pane implements RocketGraph {
    private static final Logger LOGGER = Logger.getLogger(RocketDataLocation.class);
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
    private String apiKey;
    private boolean apiKeyFound = true;
    private boolean isVisible = true;
    private boolean fileExists = true;

    /**
     * Create a new RocketDataLocation panel which shows the rocket position on a
     * map in the GUI.
     *
     * @param centerLatitude  Center latitude position.
     * @param centerLongitude Center longitude position.
     * @param imageWidth      Image width.
     * @param imageHeight     Image height.
     * @param graphType       The graph type for this graph.
     */
    public RocketDataLocation(double centerLatitude, double centerLongitude, int imageWidth, int imageHeight,
                              GraphType graphType) {
        filename = "src/main/resources/map-data/" + centerLatitude + "-" + centerLongitude + "-map_image.png";
        File mapFile = new File(filename);
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        if (!mapFile.exists() && !mapFile.isDirectory()) {
            try {
                this.apiKey = KeyImporter.getKey("maps");
                MapImageImporter.importImage(apiKey, centerLatitude, centerLongitude, 17, imageWidth, imageHeight, "src/main/resources/map-data/");
            } catch (TomTomRequestFailedException ex) {
                fileExists = false;
            } catch (KeyNotFoundException e) {
                apiKeyFound = false;
                LOGGER.error("Maps key missing", e);
            }
        }
        canvas = new Canvas(getWidth(), getHeight());
        this.getChildren().add(canvas);
        widthProperty().addListener(e -> canvas.setWidth(getWidth()));
        heightProperty().addListener(e -> canvas.setHeight(getHeight()));

        this.setGraphType(graphType);
        this.setId(graphType.getGraphID());

    }

    /**
     * Calculates the angle and euclidean distance between the launching point and current location of the rocket.
     *
     * @param newLatitude  - Rocket's new Latitude
     * @param newLongitude - Rocket's new longitude
     */
    public void updateAngleDistanceInfo(double newLatitude, double newLongitude) {
        if (apiKeyFound || fileExists) {
            angle = angleBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
            hypotenuse = distanceBetweenTwoLocations(centerLatitude, centerLongitude, newLatitude, newLongitude);
            // Must manually run layoutChildren to update the map with data.
            layoutChildren();
        }
    }

    /**
     * http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param lat1  Latitude from
     * @param long1 Longitude from
     * @param lat2  Latitude to
     * @param long2 Longitude to
     * @return Double distance location between two locations.
     */
    public static double distanceBetweenTwoLocations(double lat1, double long1, double lat2, double long2) {
        double r = 6371e3;
        double theta1 = lat1 * Math.PI / 180.0;
        double theta2 = lat2 * Math.PI / 180.0;
        double deltaTheta = (lat2 - lat1) * Math.PI / 180.0;
        double deltaLambda = (long2 - long1) * Math.PI / 180.0;
        double a = Math.sin(deltaTheta / 2.0) * Math.sin(deltaTheta / 2.0) + Math.cos(theta1) * Math.cos(theta2)
                * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
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
     * @return A double angle between location one and two.
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
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.graphicsWidth = canvas.getWidth() * 0.98;
        this.graphicsHeight = canvas.getHeight() * 0.98;
        if (!apiKeyFound) {
            g.strokeText("Map Image API key not found", graphicsWidth / 4, graphicsHeight / 2);
            return;
        }
        if (fileExists) {
            Image img = new Image("file:" + filename);
            g.drawImage(img, canvas.getWidth() * 0.01, canvas.getHeight() * 0.01, this.graphicsWidth,
                    this.graphicsHeight);
            g.setFill(Color.GREEN);
            g.fillOval(graphicsWidth / 2 - (MARKER_SIZE / 2), graphicsHeight / 2 - (MARKER_SIZE / 2), MARKER_SIZE,
                    MARKER_SIZE); // Center
            double toMoveVertical = hypotenuse * Math.cos(Math.toRadians(angle));
            double toMoveHorizontal = hypotenuse * Math.sin(Math.toRadians(angle));
            g.setFill(Colours.PRIMARY_COLOUR);
            double x = graphicsWidth / 2 - (MARKER_SIZE / 2) + (int) pixelsToMove(toMoveHorizontal);
            double y = graphicsHeight / 2 - (MARKER_SIZE / 2) - (int) pixelsToMove(toMoveVertical);
            double markerOffset = 40;

            g.fillOval(x, y, MARKER_SIZE, MARKER_SIZE); // Center
            g.setStroke(Colours.PRIMARY_COLOUR);
            g.strokeOval(x - markerOffset / 2, y - markerOffset / 2, MARKER_SIZE + markerOffset,
                    MARKER_SIZE + markerOffset); // Center
        } else {
            g.strokeText("Map Image Not Found. Pull Map Data.", graphicsWidth / 8, graphicsHeight / 2);
        }

    }

    @Override
    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    @Override
    public boolean isGraphVisible() {
        return this.isVisible;
    }
}

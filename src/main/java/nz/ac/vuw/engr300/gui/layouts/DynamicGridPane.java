package nz.ac.vuw.engr300.gui.layouts;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * DynamicGridPane is an extension of GridPane to allow for dynamic layouts of items inside.
 * This allows a list of regions to be added into the GridPane in left-to-right ordering. This includes
 * allowing for resizing a region within the GridPane. The columns are dynamically adjusted based on
 * the current width of the DynamicGridPane and the minimum specified graph width.
 * If a region can fit inside the row it will be added. However if there is not enough space for the
 * graph the other graphs inside the row will scale horizontally to fill the GridPane's horizontal space.
 *
 * @author Nathan Duckett
 */
public class DynamicGridPane extends GridPane {
    private int columns;
    private int rows;
    private int minRegionWidth;
    private final List<Region> internalGridContents;

    /**
     * Create a new DynamicGridPane. This can be provided as an empty constructor to create a blank
     * DynamicGridPane or can be provided an array of Regions which will be added directly to the GridPane.
     *
     * @param gridContents Array of Region contents to be placed within the GridPane.
     */
    public DynamicGridPane(Region...gridContents) {
        this(300, gridContents);
    }

    /**
     * Create a new DynamicGridPane. This allows you to provide a custom minimum region width and an array of regions
     * to be added into the GridPane.
     *
     * @param minRegionWidth Integer minRegionWidth representing the minimum size a region can be.
     * @param gridContents Array of Region contents to be placed within the GridPane.
     */
    public DynamicGridPane(int minRegionWidth, Region...gridContents) {
        super();
        this.internalGridContents = new ArrayList<>();
        this.minRegionWidth = minRegionWidth;

        // Default to 2 columns based on application starting size can be changed later.
        this.columns = 2;
        this.rows = calculatedRows();

        // Create a listener to calculate columns based on width changes and redraw the screen.
        this.widthProperty().addListener((ov, n, t1) -> {
            updateInternalDimensions(t1.doubleValue());
            updateContents();
        });

        // Add all provided contents if any
        for (Region region: gridContents) {
            addGridContent(region, false);
        }

        updateContents();
    }

    /**
     * Add a new region into the GridPane contents and update the contents once added.
     *
     * @param gridContent Region to be added within this GridPane.
     */
    public void addGridContent(Region gridContent) {
        addGridContent(gridContent, true);
    }

    /**
     * Add a new region into the GridPane contents. This allows you to set whether the contents are instantly updated.
     * This is used during initialization to prevent duplicate operations.
     *
     * @param gridContent Region to be added within this GridPane.
     * @param instantUpdate Boolean indicating whether the contents will be updated with this added region.
     */
    private void addGridContent(Region gridContent, boolean instantUpdate) {
        this.internalGridContents.add(gridContent);
        this.rows = calculatedRows();
        if (instantUpdate) {
            updateContents();
        }
    }

    /**
     * Clear all internal stored regions.
     */
    public void clearGridContents() {
        this.internalGridContents.clear();
        updateContents();
    }

    /**
     * Update the contents of the GridPane to reflect the internal ordering and contents of the regions.
     */
    public void updateContents() {
        // Clear children first otherwise could duplicate add resulting in exception
        this.getChildren().clear();
        int regionNo = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (regionNo < internalGridContents.size()) {
                    // Assumes all regions consume 1 ROW SPAN/COL SPAN
                    this.add(internalGridContents.get(regionNo++), x, y, 1, 1);
                } else {
                    // Exit once all graphs are done
                    return;
                }
            }
        }
    }

    /**
     * Get the current minimum region width which has been set for this DynamicGridPane.
     *
     * @return Integer representing the minimum size allowed for one graph currently.
     */
    public int getMinRegionWidth() {
        return this.minRegionWidth;
    }

    /**
     * Set the minimum region width allowed for each individual graph to the provided value.
     *
     * @param minRegionWidth Integer minRegionWidth representing the smallest size a region can be
     */
    public void setMinRegionWidth(int minRegionWidth) {
        this.minRegionWidth = minRegionWidth;
        // Update columns to match the new specification.
        updateInternalDimensions(getWidth());
        updateContents();
    }

    /**
     * Get the current number of columns within the GridPane.
     *
     * @return Integer representing the current columns in the GridPane.
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Get the current number of rows within the GridPane.
     *
     * @return Integer representing the current rows in the GridPane.
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Get the calculated rows this GridPane contains based on the number of internal regions and the number of
     * columns currently supported by the GridPane's size.
     *
     * @return Integer representing the total number of rows needed to display all regions.
     */
    private int calculatedRows() {
        int fullRows = this.internalGridContents.size() / columns;
        if (this.internalGridContents.size() % columns > 0) {
            fullRows++;
        }
        return fullRows;
    }

    /**
     * Update the internal Rows/Cols dimension within this GridPane based on the width.
     *
     * @param currentWidth Current width of the GridPane to base the values from.
     */
    private void updateInternalDimensions(double currentWidth) {
        this.columns = (int) (currentWidth / minRegionWidth);
        this.rows = calculatedRows();
    }
}

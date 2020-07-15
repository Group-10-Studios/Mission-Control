package nz.ac.vuw.engr300.gui.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Utility class that provides useful functions for dealing with the UI.
 *
 * @author Tim Salisbury
 */
public class UIUtil {

    /**
     * Creates a GridPane with Vgap/Hgap/padding set to the given parameters.
     *
     * @param horizontalGap     The horizontal gap to have inbetween columns. NOTE: This does not work if there is only one column! Use the padding for that.
     * @param verticalGap       The vertical gap to have inbetween rows. NOTE: This does not work if there is only one row! Use the padding for that.
     * @param padding           The padding to set the GridPane to have.
     * @return                  The GridPane.
     */
    public static GridPane createGridPane(int horizontalGap, int verticalGap, Insets padding){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(verticalGap);
        gridPane.setHgap(horizontalGap);
        gridPane.setPadding(padding);
        return gridPane;
    }

    /**
     * Creates a VBox which height is equal to the sum of the children height + their gaps. The childrens width will grow
     * to always be the maximum size they can go (VBox width - padding).
     *
     * @param childrenGap   The gap vertical to put inbetween each child.
     * @param padding       The padding to set the VBox to have around the children.
     * @param children      The children to add to the VBox.
     * @return              The final VBox.
     */
    public static VBox createMinimumVerticalSizeVBox(int childrenGap, Insets padding, Region... children){
        VBox wrapper = new VBox(childrenGap, children);
        wrapper.setPadding(padding);

        // Set their max widths to a large number to ensure they always grow.
        Arrays.stream(children).forEach(child -> child.setMaxWidth(10000));

        // Set the wrappers height to be children height + their gaps
        wrapper.setMaxHeight(VBox.USE_PREF_SIZE);
        return wrapper;
    }

    /**
     * Creates a VBox which height is equal to the sum of the children height. The childrens width will grow
     * to always be the maximum size they can go (VBox width - padding).
     *
     * @param padding       The padding to set the VBox to have around the children.
     * @param children      The children to add to the VBox.
     * @return              The final VBox.
     */
    public static VBox createMinimumVerticalSizeVBox(Insets padding, Region... children){
        return createMinimumVerticalSizeVBox(0, padding, children);
    }

    /**
     * Creates a VBox which height is equal to the sum of the children height. The childrens width will grow
     * to always be the maximum size they can go (VBox width).
     *
     * @param children      The children to add to the VBox.
     * @return              The final VBox.
     */
    public static VBox createMinimumVerticalSizeVBox(Region... children){
        return createMinimumVerticalSizeVBox(0, Insets.EMPTY, children);
    }

    /**
     * Creates a standard VBox whose height is the maximum it can be. The childrens widths of the VBox will always grow
     * to the maximum size they can.
     *
     * @param childrenGap   The vertical gap to have inbetween children.
     * @param padding       The padding to set the VBox to have around the children.
     * @param children      The children to add to the VBox.
     * @return              The final VBox.
     */
    public static VBox createStandardVBox(int childrenGap, Insets padding, Region... children){
        VBox wrapper = new VBox(childrenGap, children);
        Arrays.stream(children).forEach(child -> child.setMaxWidth(10000));
        wrapper.setPadding(padding);
        return wrapper;
    }

    /**
     *  Helper function for proof of concept - not needed otherwise.
     */
    public static void addNodeToGrid(Node child, GridPane parent, int row, int column, Pos alignment, Color fill, Insets insets){
        HBox nodeWrapper = new HBox();

        // Just to set the background
        nodeWrapper.setBackground(new Background(new BackgroundFill(fill,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Alignment in the cell
        nodeWrapper.setAlignment(alignment);

        nodeWrapper.getChildren().add(child);

        addNodeToGrid(nodeWrapper, parent, row, column, insets);
    }

    /**
     * Adds a node (child) to a GridPane (parent) at the specified row and column, with the specified
     * rowSpan and columnSpan, with the specified insets set.
     *
     * @param child         The child to add.
     * @param parent        The parent to add the child to.
     * @param row           The row to add the child at.
     * @param column        The column to add the child at.
     * @param rowSpan       The rowSpan to make the child.
     * @param columnSpan    The columnSpan to make the child.
     * @param insets        The insets to set the child at.
     */
    public static void addNodeToGrid(Region child, GridPane parent, int row, int column, int rowSpan, int columnSpan, Insets insets){
//        // Set position in grid
//        GridPane.setRowIndex(child, row);
//        GridPane.setColumnIndex(child, column);
//
//        // Set margins within the cell
        GridPane.setMargin(child, insets);
//
//        // Set row and column span
//        GridPane.setRowSpan(child, rowSpan);
//        GridPane.setColumnSpan(child, columnSpan);
//
//        // Finally add
//        parent.getChildren().add(child);
        // ALTERNATIVELY
        parent.add(child, column, row, columnSpan, rowSpan);
    }

    /**
     * Adds a node (child) to a GridPane (parent) at the specified row and column, with the specified insets set.
     *
     * @param child         The child to add.
     * @param parent        The parent to add the child to.
     * @param row           The row to add the child at.
     * @param column        The column to add the child at.
     * @param insets        The insets to set the child at.
     */
    public static void addNodeToGrid(Region child, GridPane parent, int row, int column, Insets insets){
        addNodeToGrid(child, parent, row, column, 1, 1, insets);
    }

    /**
     * Adds a node (child) to a GridPane (parent) at the specified row and column.
     *
     * @param child         The child to add.
     * @param parent        The parent to add the child to.
     * @param row           The row to add the child at.
     * @param column        The column to add the child at.
     */
    public static void addNodeToGrid(Region child, GridPane parent, int row, int column){
        addNodeToGrid(child, parent, row, column, 1, 1, Insets.EMPTY);
    }

    /**
     * Adds a node (child) to a GridPane (parent) at the specified row and column, with the specified
     * rowSpan and columnSpan.
     *
     * @param child         The child to add.
     * @param parent        The parent to add the child to.
     * @param row           The row to add the child at.
     * @param column        The column to add the child at.
     * @param rowSpan       The rowSpan to make the child.
     * @param columnSpan    The columnSpan to make the child.
     */
    public static void addNodeToGrid(Region child, GridPane parent, int row, int column, int rowSpan, int columnSpan){
        addNodeToGrid(child, parent, row, column, rowSpan, columnSpan, Insets.EMPTY);
    }

    /**
     * Adds columns with percentage widths to the specified grid.
     *
     * @param grid          The grid to add columns to.
     * @param percents      The percents of the columns to add.
     */
    public static void addPercentColumns(GridPane grid, double... percents){
        // Constructor value v1 in Column/Row Constraints is pixel width/height, so percents need to be a separate function call.
        // I know, it's lame.
        for (double percent : percents){
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(percent);
            grid.getColumnConstraints().add(column);
        }
    }

    /**
     * Adds rows with percentage heights to the specified grid.
     *
     * @param grid          The grid to add rows to.
     * @param percents      The percents of the rows to add.
     */
    public static void addPercentRows(GridPane grid, double... percents){
        // Constructor value v1 in Column/Row Constraints is pixel width/height, so percents need to be a separate function call.
        // I know, it's lame.
        for (double percent : percents){
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(percent);
            grid.getRowConstraints().add(row);
        }
    }

    /**
     * Adds columns with hardcoded pixel widths to the specified grid.
     *
     * @param grid          The grid to add columns to.
     * @param widths        The pixel widths of the columns to add.
     */
    public static void addPixelWidthColumns(GridPane grid, double... widths){
        for (double width : widths){
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        }
    }

    /**
     * Adds rows with hardcoded pixel heights to the specified grid.
     *
     * @param grid          The grid to add rows to.
     * @param heights       The pixel heights of the rows to add.
     */
    public static void addPixelHeightRows(GridPane grid, double... heights){
        for (double height : heights){
            grid.getRowConstraints().add(new RowConstraints(height));
        }
    }


    //*********** Useful, but not needed functions ***********

    public static HBox createMinimumHorizontalSizeHBox(int childrenGap, Insets padding, Region... children){
        HBox wrapper = new HBox(childrenGap, children);
        wrapper.setPadding(padding);
        Arrays.stream(children).forEach(child -> child.setMaxHeight(10000));
        wrapper.setMaxHeight(VBox.USE_PREF_SIZE);
        return wrapper;
    }

    public static HBox createMinimumHorizontalSizeHBox(Insets padding, Region... children){
        return createMinimumHorizontalSizeHBox(0, padding, children);
    }

    public static HBox createMinimumHorizontalSizeHBox(Region... children){
        return createMinimumHorizontalSizeHBox(0, Insets.EMPTY, children);
    }
}

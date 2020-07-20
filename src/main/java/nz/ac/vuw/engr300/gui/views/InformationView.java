package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.components.RocketBattery;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;

/**
 * Represents the right panel, which displays general information.
 *
 * @author Tim Salisbury, Nalin Aswani, Jake Mai
 */
public class InformationView implements View {
    private GridPane root;

    /**
     * Separate thread to run the battery timers on.
     */
    private Thread batteryThread;

    public RocketBattery primaryBattery = new RocketBattery();
    public RocketBattery secondaryBattery = new RocketBattery();

    public InformationView(GridPane root) {
        this.root = root;

        // One column, 100 percent of width
        UiUtil.addPercentColumns(root, 100);
        // 20 for batteries, 60 for warnings, 20 for go/no go
        UiUtil.addPercentRows(root, 20, 60, 20);

        // Add Batteries and warning sections of right hand side panel
//        addNodeToGrid(new Label("BATTERIES"), root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);

        // Create and populate batteries in a HBox
        HBox batteryHBox = UiUtil.createMinimumHorizontalSizeHBox(5, new Insets(10), primaryBattery, secondaryBattery);

        addNodeToGrid(batteryHBox, root, 0, 0, Pos.CENTER, Color.TURQUOISE, Insets.EMPTY);
        addNodeToGrid(new Label("WARNINGS"), root, 1, 0, Pos.CENTER, Color.MAGENTA, Insets.EMPTY);

        // Create and populate go no go at bottom of right hand side
        VBox goNoGoVBox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10), new Button("Btn0"), new Button("Btn1"));
        // Literally just for setting background colour
        goNoGoVBox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
         CornerRadii.EMPTY, Insets.EMPTY)));

        // Set it to hug the warnings above it
        GridPane.setValignment(goNoGoVBox, VPos.TOP);
        addNodeToGrid(goNoGoVBox, root, 2, 0, Insets.EMPTY);
    }


}

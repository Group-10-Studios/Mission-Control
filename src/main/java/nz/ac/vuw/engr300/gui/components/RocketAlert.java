package nz.ac.vuw.engr300.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.util.Colours;

import java.util.ArrayList;

/**
 * Basic component to display events/warnings/alerts on the events pane on the right side of the UI.
 *
 * @author Tim Salisbury
 * @author Nalin Aswani
 */
public class RocketAlert extends VBox {

    private Label title = new Label();
    private AlertLevel alertLevel;

    /**
     * Constructor for the Rocket Alert used to display events/warnings/alerts on the right hand side pane. Note,
     * that descriptions is any number of strings. This is because we want multiline descriptions to have all
     * lines centered in the component.
     *
     * @param level             The alert level of this alert.
     * @param title             The title of this alert.
     * @param descriptions      The descriptions of this alert.
     */
    public RocketAlert(AlertLevel level, String title, String... descriptions) {
        this.setAlignment(Pos.TOP_CENTER);
        this.title.setText(title);
        this.title.setStyle("-fx-font-weight: bold");
        this.getChildren().add(this.title);
        for (String description : descriptions) {
            this.getChildren().add(new Label(description));
        }

        this.setMaxHeight(VBox.USE_PREF_SIZE);
        this.setBorder(new Border(new BorderStroke(level.getColor(),
                BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3.3))));

        alertLevel = level;
    }

    /**
     * Basic enum for representing the different levels of Alerts that can be displayed on the right
     * hand side panel. This enum also controls the border colour of the alert.
     */
    public enum AlertLevel {
        ERROR(Colours.WARNING_ERROR),
        WARNING(Colours.WARNING_WARN),
        ALERT(Colours.WARNING_INFO);    // Blue

        private final Color color;

        AlertLevel(Color color) {
            this.color = color;
        }

        /**
         * Gets the colour that the border of an alert of this level should be.
         *
         * @return      The colour of the border.
         */
        public Color getColor() {
            return color;
        }
    }

    /**
     * Gets the alert level of the current RocketAlert.
     * @return the alert level of this RocketAlert.
     */
    public AlertLevel getAlertLevel() {
        return this.alertLevel;
    }
}

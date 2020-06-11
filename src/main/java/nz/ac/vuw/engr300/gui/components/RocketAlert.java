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

/**
 * Basic component to display events/warnings/alerts on the events pane on the right side of the UI.
 *
 * @author Tim Salisbury
 * @author Nalin Aswani
 */
public class RocketAlert extends VBox {

    private Label title = new Label();

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


    }


    public enum AlertLevel {
        ERROR(Color.RED),
        WARNING(Color.ORANGE),
        ALERT(Color.valueOf("#4267B2"));    // Blue

        private final Color color;

        AlertLevel(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}

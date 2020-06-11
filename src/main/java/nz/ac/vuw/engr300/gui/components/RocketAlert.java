package nz.ac.vuw.engr300.gui.components;

import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
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
    private Label description = new Label();

    public RocketAlert(String title, String description, AlertLevel level) {

        this.title.setText(title);
        this.description.setText(description);
        Group group = new Group(this.title, this.description);

        this.getChildren().add(group);

        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

    }


    public enum AlertLevel {
        ERROR,      // Red
        WARNING,    // Yellow
        ALERT       // Green
    }
}

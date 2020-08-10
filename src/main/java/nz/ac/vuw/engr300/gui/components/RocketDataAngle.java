package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.util.Colours;

/**
 * A component that can display an angle or a direction.
 *
 * @author Tim Salisbury
 */
public class RocketDataAngle extends Gauge implements RocketGraph {
    private GraphType type;
    private boolean isCompass;
    private boolean isVisible = true;

    /**
     * Constructs a new RocketDataAngle component, where the boolean
     * {@code isCompass} will define the appearance. If it is set to true, then it
     * will appear as a compass with N, NE, E, SE, S, SW, W, NW markings. If it is
     * set to false, it will appear with standard angle markings, E.G. 0, 45, 90,
     * 135, 180, 225, 270, 315.
     * Usage: {@code
     * <RocketDataAngle isCompass="true"/>
     * }
     *
     * @param isCompass Whether or not this compass is displaying an angle or a
     *                  direction.
     * @param graphType GraphType being represented by this RocketGraph
     */
    public RocketDataAngle(@NamedArg("isCompass") boolean isCompass, GraphType graphType) {
        super();
        this.isCompass = isCompass;
        // Make it pretty!
        this.setBorderPaint(Gauge.DARK_COLOR);
        this.setMinValue(isCompass ? 0 : -180);
        this.setMaxValue(isCompass ? 359 : 180);
        this.setAutoScale(false);
        this.setStartAngle(isCompass ? 180 : 270);
        this.setAngleRange(isCompass ? 360 : 180);
        this.setMinorTickMarksVisible(false);
        this.setMediumTickMarksVisible(false);
        this.setMajorTickMarksVisible(false);
        this.setCustomTickLabelsEnabled(true);
        if (isCompass) {
            this.setCustomTickLabels("N", "", "", "", "NE", "", "", "", "", "E", "", "", "", "SE", "", "", "", "", "S",
                    "", "", "", "SW", "", "", "", "", "W", "", "", "", "NW", "", "", "", "");
        } else {
            this.setCustomTickLabels("-180", "", "", "", "", "", "", "", "", "-90", "", "", "", "", "", "", "", "",
                    "0", "", "", "", "", "", "", "", "", "90", "", "", "", "", "", "", "", "", "180");
        }

        this.setCustomTickLabelFontSize(48);
        this.setKnobType(KnobType.FLAT);
        this.setKnobColor(Gauge.DARK_COLOR);
        this.setNeedleShape(NeedleShape.FLAT);
        this.setNeedleType(NeedleType.VARIOMETER);
        this.setNeedleColor(Colours.PRIMARY_COLOUR);
        this.setNeedleBehavior(NeedleBehavior.OPTIMIZED);
        this.setTickLabelColor(DARK_COLOR);
        this.setAnimated(false);
        this.setValueVisible(true);

        this.setBackground(new Background(new BackgroundFill(Colours.BACKGROUND_COLOUR,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Do not remove! If you remove you'll get exceptions! This is to stop it from overflowing the border.
        this.heightProperty().addListener((ObservableValue<? extends Number> observableValue,
                                           Number number, Number t1) -> {
            if (t1.intValue() > 0) {
                this.setPadding(new Insets(10));
            }
        });

        this.setGraphType(graphType);

        this.setId(graphType.getGraphID());
    }

    /**
     * Sets the direction of the finger, where 0 is N, 90 is E, 180 is S, and 270 is
     * W. You can pass it any value, even negative, and it should account for it.
     * E.G. An angle of -10 would be 350.
     *
     * @param angle The angle to set the finger.
     */
    public void setAngle(double angle) {
        Platform.runLater(() -> super.setValue(angle));
    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }

    @Override
    public void clear() {
        // Do nothing for now - not needed.
        return;
    }

    @Override
    public GraphType getGraphType() {
        return this.type;
    }

    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
        if (isCompass) {
            this.setTitle(g.getLabel());
        } else {
            this.setSubTitle(g.getLabel());
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

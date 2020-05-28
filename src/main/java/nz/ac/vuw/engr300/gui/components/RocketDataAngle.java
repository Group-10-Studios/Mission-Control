package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.model.GraphType;

/**
 * A component that can display an angle or a direction.
 *
 * @author Tim Salisbury
 */
public class RocketDataAngle extends Gauge implements RocketGraph {
    private GraphType type;

    /**
     * Constructs a new RocketDataAngle component, where the boolean
     * {@code isCompass} will define the appearance. If it is set to true, then it
     * will appear as a compass with N, NE, E, SE, S, SW, W, NW markings. If it is
     * set to false, it will appear with standard angle markings, E.G. 0, 45, 90,
     * 135, 180, 225, 270, 315.
     * Usage: {@code
     *  <RocketDataAngle isCompass="true"/>
     * }
     *
     * @param isCompass Whether or not this compass is displaying an angle or a
     *                  direction.
     */
    public RocketDataAngle(@NamedArg("title") String title, @NamedArg("isCompass") boolean isCompass) {
        super();
        this.setTitle(title);
        // Make it pretty!
        this.setBorderPaint(Gauge.DARK_COLOR);
        this.setMinValue(0);
        this.setMaxValue(359);
        this.setAutoScale(false);
        this.setStartAngle(180);
        this.setAngleRange(360);
        this.setMinorTickMarksVisible(false);
        this.setMediumTickMarksVisible(false);
        this.setMajorTickMarksVisible(false);
        this.setCustomTickLabelsEnabled(true);
        if (isCompass) {
            this.setCustomTickLabels("N", "", "", "", "NE", "", "", "", "", "E", "", "", "", "SE", "", "", "", "", "S",
                    "", "", "", "SW", "", "", "", "", "W", "", "", "", "NW", "", "", "", "");
        } else {
            this.setCustomTickLabels("0", "", "", "", "45", "", "", "", "", "90", "", "", "", "135", "", "", "", "",
                    "180", "", "", "", "225", "", "", "", "", "270", "", "", "", "315", "", "", "", "");
        }

        this.setCustomTickLabelFontSize(48);
        this.setKnobType(KnobType.FLAT);
        this.setKnobColor(Gauge.DARK_COLOR);
        this.setNeedleShape(NeedleShape.FLAT);
        this.setNeedleType(NeedleType.VARIOMETER);
        this.setNeedleColor(Color.valueOf("#4267B2"));
        this.setNeedleBehavior(NeedleBehavior.OPTIMIZED);
        this.setTickLabelColor(DARK_COLOR);
        this.setAnimated(false);
        this.setValueVisible(true);
    }

    /**
     * Sets the direction of the finger, where 0 is N, 90 is E, 180 is S, and 270 is
     * W. You can pass it any value, even negative, and it should account for it.
     * E.G. An angle of -10 would be 350.
     *
     * @param angle The angle to set the finger.
     */
    public void setAngle(double angle) {
        Platform.runLater(() -> super.setValue(angle % 360));
    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }

    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
    }

    @Override
    public GraphType getGraphType() {
        return this.type;
    }
}

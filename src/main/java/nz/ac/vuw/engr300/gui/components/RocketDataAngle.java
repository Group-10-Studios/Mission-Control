package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;

/**
 *
 * @author Tim Salisbury
 */
public class RocketDataCompass extends Gauge {

    public RocketDataCompass() {
        super();

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
        this.setCustomTickLabels("0", "", "", "", "45", "", "", "", "",
                                "90", "", "", "", "135", "", "", "", "",
                                "180", "", "", "", "225", "", "", "", "",
                                "270", "", "", "", "315", "", "", "", "");
        this.setCustomTickLabelFontSize(48);
        this.setKnobType(KnobType.FLAT);
        this.setKnobColor(Gauge.DARK_COLOR);
        this.setNeedleShape(NeedleShape.FLAT);
        this.setNeedleType(NeedleType.VARIOMETER);
        this.setNeedleBehavior(NeedleBehavior.OPTIMIZED);
        this.setTickLabelColor(DARK_COLOR);
        this.setAnimated(false);
        this.setValueVisible(true);
    }

    public void setAngle(double angle){
        Platform.runLater(()->super.setValue(angle % 360));
    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }
}

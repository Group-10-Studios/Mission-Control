package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.events.UpdateEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RocketDataCompass extends Gauge {

    public RocketDataCompass() {
        super();
//                                    .borderPaint(Gauge.DARK_COLOR)
//                .minValue(0)
//                .maxValue(359)
//                .autoScale(false)
//                .startAngle(180)
//                .angleRange(360)
//                .minorTickMarksVisible(false)
//                .mediumTickMarksVisible(false)
//                .majorTickMarksVisible(false)
//                .customTickLabelsEnabled(true)
//                .customTickLabels("N", "", "", "", "", "", "", "", "",
//                        "E", "", "", "", "", "", "", "", "",
//                        "S", "", "", "", "", "", "", "", "",
//                        "W", "", "", "", "", "", "", "", "")
//                .customTickLabelFontSize(48)
//                .knobType(KnobType.FLAT)
//                .knobColor(Gauge.DARK_COLOR)
//                .needleShape(NeedleShape.FLAT)
//                .needleType(NeedleType.FAT)
//                .needleBehavior(NeedleBehavior.OPTIMIZED)
//                .tickLabelColor(Gauge.DARK_COLOR)
//                .animated(true)
//                .animationDuration(500)
//                .valueVisible(false)

        this.setMinValue(0);
        this.setMinValue(359);
        this.setAutoScale(false);
        this.setStartAngle(180);
        this.setAngleRange(360);
        this.setCustomTickLabels("N", "", "", "", "", "", "", "", "",
                        "E", "", "", "", "", "", "", "", "",
                        "S", "", "", "", "", "", "", "", "",
                        "W", "", "", "", "", "", "", "", "");


    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }
}

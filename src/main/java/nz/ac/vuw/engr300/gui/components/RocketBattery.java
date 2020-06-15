package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Fonts;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.LcdFont;
import eu.hansolo.medusa.Section;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import nz.ac.vuw.engr300.gui.model.GraphType;

public class RocketBattery extends Gauge {

    /**
     * A RocketBattery object that represents one of the rocket's battery.
     */
    public RocketBattery() {
        super();
        this.setSkinType(SkinType.BATTERY);
        this.setAnimated(true);
        this.setCustomFontEnabled(true);
        this.setCustomFont(Fonts.latoRegular(50.0));
    }

    /**
     * Change the battery percentage.
     * @param percentage - The new battery percentage
     */
    public void setBatteryLevel(double percentage) {
        this.setValue(percentage);
        if (percentage > 0 && percentage <= 20) {
            this.setBarColor(Color.rgb(200, 0, 0, 0.8));
        } else if (percentage > 20 && percentage <= 50) {
            this.setBarColor(Color.rgb(200, 200, 0, 0.8));
        } else if (percentage > 50 && percentage <= 100) {
            this.setBarColor(Color.rgb(0, 200, 0, 0.8));
        }
    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }

}

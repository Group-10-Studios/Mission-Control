package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Fonts;
import eu.hansolo.medusa.Gauge;
import javafx.scene.paint.Color;

public class RocketBattery extends Gauge {
    private final String batteryName;
    private final String batteryUnit;
    /**
     * Define the expected battery voltage. This is used to calculate the percentage from the voltage.
     */
    private double batteryVoltage = 12d;

    /**
     * A RocketBattery object that represents one of the rocket's battery.
     */
    public RocketBattery(String name, String batteryUnit) {
        super();
        this.setSkinType(SkinType.BATTERY);
        this.setAnimated(true);
        this.setCustomFontEnabled(true);
        this.setCustomFont(Fonts.latoRegular(50.0));
        this.batteryName = name;
        this.batteryUnit = batteryUnit;
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

    /**
     * Set the battery value from a voltage incoming from communications. This converts it to a percentage
     * against the expected value.
     *
     * @param value Incoming double voltage value.
     */
    public void setBatteryValue(double value) {
        // Must multiply by 100 to convert to percentage
        setBatteryLevel((value / batteryVoltage) * 100);
    }

    /**
     * Get the battery name of this RocketBattery.
     *
     * @return String battery name to identify this battery.
     */
    public String getBatteryName() {
        return batteryName;
    }

    /**
     * Set this batteries expected voltage.
     *
     * @param voltage Double voltage value expected for this battery.
     */
    public void setBatteryVoltage(double voltage) {
        this.batteryVoltage = voltage;
    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }

}

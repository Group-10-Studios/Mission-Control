package nz.ac.vuw.engr300.gui.model;

import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.gui.components.RocketBattery;
import nz.ac.vuw.engr300.gui.views.InformationView;
import nz.ac.vuw.engr300.gui.views.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all a list of all of the batteries within this application for tracking and updating with configuration.
 *
 * This is similar to the GraphMasterList but instead of storing references to the battery types it will store the
 * actual batteries which should be displayed on the GUI.
 *
 * @author Nathan Duckett
 */
public class BatteryMasterList {
    private static final BatteryMasterList batteryMasterList = new BatteryMasterList();
    private final List<RocketBattery> batteries;
    private final List<View> views;

    /**
     * Create a new BatteryMasterList instance for the beginning of the application.
     */
    private BatteryMasterList() {
        batteries = new ArrayList<>();
        views = new ArrayList<>();
    }

    /**
     * Get the instance of the battery master list.
     *
     * @return BatteryMasterList for this application.
     */
    public static BatteryMasterList getInstance() {
        return batteryMasterList;
    }

    /**
     * Register a view to this battery master list which will be updated on any list change.
     *
     * @param batteryView BatteryView to be updated when a change occurs.
     */
    public void registerView(View batteryView) {
        this.views.add(batteryView);
    }

    /**
     * Register a battery to the master list.
     *
     * @param battery RocketBattery to be registered to the list.
     */
    public void registerBattery(RocketBattery battery) {
        batteries.add(battery);

        // Force GUI update on view assuming it is an InformationView for now
        for (View v : views) {
            ((InformationView) v).setupBatteries();
        }
    }

    /**
     * Update function to handle updating the battery values with the incoming data from the CSV table information.
     *
     * @param data Incoming list of data from the communication method to be updated onto the battery.
     * @param table CSV table definition this incoming data is based from the structure.
     */
    public void updateBatteryValue(List<Object> data, CsvTableDefinition table) {
        for (RocketBattery battery : batteries) {
            // Set the battery value with the value from the CSV table for this battery name.
            Object valueFromIncomingData = data.get(table.getCsvIndexOf(battery.getBatteryName()));
            double castedValue = table.matchValueToColumn(valueFromIncomingData,
                    battery.getBatteryName(), Double.class);
            battery.setBatteryValue(castedValue);
        }
    }

    /**
     * Get all of the batteries as an array for displaying on the GUI.
     *
     * @return RocketBattery array of batteries to be displayed.
     */
    public RocketBattery[] allBatteries() {
        return batteries.toArray(RocketBattery[]::new);
    }
}

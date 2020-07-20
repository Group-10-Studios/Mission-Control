package nz.ac.vuw.engr300.gui.controllers;

import nz.ac.vuw.engr300.gui.components.RocketBattery;

public class InformationController {
    /**
     * Separate thread to run the battery timers on.
     */
    private Thread batteryThread;

    private void runBatteryThread(RocketBattery primaryBattery, RocketBattery secondaryBattery) {
        this.batteryThread = new Thread(() -> {
            double b1Level = 100.0;
            double b2Level = 100.0;
            secondaryBattery.setBatteryLevel(b2Level);
            while (b1Level >= 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Error while updating primaryBattery percentage", e);
                }
                primaryBattery.setBatteryLevel(b1Level);
                b1Level -= 1.0;
            }
            while (b2Level >= 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Error while updating primaryBattery percentage", e);
                }
                secondaryBattery.setBatteryLevel(b2Level);
                b2Level -= 1.0;
            }
        });
        this.batteryThread.start();
    }

    /**
     * Callback for when the cross at top right gets pressed, this function should
     * be used to cleanup any resources and close any ongoing threads.
     */
    public void shutdown() {
//        simulationImporter.stop();
        this.batteryThread.interrupt();
    }
}

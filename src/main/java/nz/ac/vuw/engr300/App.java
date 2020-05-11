package nz.ac.vuw.engr300;

import nz.ac.vuw.engr300.gui.GUI;

/**
 * Basic App wrapper to launch the GUI application. Required for the fat JAR.
 *
 * @author Nathan Duckett
 */
public class App {

    /**
     * Main application to start the GUI.
     */
    public static void main(String[] args) {
        GUI.main(args);
    }
}

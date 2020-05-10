package nz.ac.vuw.engr300;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import nz.ac.vuw.engr300.gui.GUI;

/**
 * Basic App wrapper to launch the GUI application. Required for the fat JAR.
 *
 * @author Nathan Duckett
 */
public class App {
	private static final Logger LOGGER = Logger.getLogger(App.class);

    /**
     * Main application to start the GUI.
     */
    public static void main(String[] args) {
        try {
        	GUI.main(args);
        } catch (Error e) {
        	showError("An unhandled error has ocurred in the application. Please view the logs to find what caused this.");
        	// Dump entire error message into log files.
        	LOGGER.error(e.getMessage());
        	System.exit(1);
        } catch (RuntimeException e) {
        	showError("An unhandled RuntimeException has ocurred in the application. Please view the logs to find what caused this.");
        	// Dump entire error message into log files.
        	LOGGER.error(e.getMessage());
        	System.exit(1);
        }
    }
    
    /**
     * Opens a JOptionPane to display the error message. This must be done using Java swing, as
     * it helps show the 
     */
    private static void showError(String errorMessage) {
    	JOptionPane.showMessageDialog(null, errorMessage, "Mission-Control Error", JOptionPane.INFORMATION_MESSAGE);
    }
}

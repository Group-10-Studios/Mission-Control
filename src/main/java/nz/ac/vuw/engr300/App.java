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
			showError("error");
			// Dump entire error message into log files.
			LOGGER.error(e.getMessage());
			System.exit(1);
		} catch (RuntimeException e) {
			if (javaFxMissing(e)) {
				System.exit(2);
			}

			showError("RuntimeException");
			// Dump entire error message into log files.
			LOGGER.error(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Opens a JOptionPane to display the error message. This must be done using
	 * Java swing, as it helps show the panel no matter if JavaFX has failed (Known
	 * bug).
	 * 
	 * @param type Error type to refer in the pop-up message to send to the user.
	 */
	private static void showError(String type) {
		JOptionPane.showMessageDialog(null,
				"An unhandled " + type
						+ " has occurred in the application. Please view the logs to find what caused this.",
				"Mission-Control Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Check if the JavaFX SDK missing caused the exception and if it does present the correct error pop-up and
	 * exit out.
	 * 
	 * @param e RuntimeException received from the application.
	 * @return Boolean indicating if the JavaFX missing is the cause of the error.
	 */
	private static boolean javaFxMissing(RuntimeException e) {
		if (e.getMessage().equals("No toolkit found")) {
			JOptionPane.showMessageDialog(null,
					"The JavaFX SDK is missing on your machine please install this before running the software.",
					"Mission-Control Missing Dependency Error", JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e.getMessage());
			return true;
		}

		return false;
	}
}

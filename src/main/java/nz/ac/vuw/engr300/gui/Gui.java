package nz.ac.vuw.engr300.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.views.HomeView;

/**
 * Runs the main GUI application for Mission Control. This defaults to a
 * HomeView start.
 *
 * @author Nalin
 */
public class Gui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("ENGR301-Group10-Mission Control Software System");
        new HomeView(stage);
    }

    /**
     * Start the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

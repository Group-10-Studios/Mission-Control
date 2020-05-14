package nz.ac.vuw.engr300.gui.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Represents a Home Screen view which the application will default to.
 *
 * @author Nathan Duckett
 */
public class HomeView {

    public HomeView(Stage stage) {
        Parent root = null;
        URL layoutFile = getClass().getClassLoader().getResource("layouts/Home.fxml");
        if(layoutFile == null){
            throw new RuntimeException("Layout file for Home view not found!");
        }

        try {
            root = FXMLLoader.load(layoutFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

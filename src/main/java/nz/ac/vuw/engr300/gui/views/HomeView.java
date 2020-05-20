package nz.ac.vuw.engr300.gui.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.controllers.HomeController;

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

        FXMLLoader loader = new FXMLLoader(layoutFile);
        try {

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        HomeController controller = loader.getController();
        stage.setOnCloseRequest(e -> controller.shutdown());

        stage.show();
        // Set minimum dimensions to 720p - Doesn't support below this
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
    }
}

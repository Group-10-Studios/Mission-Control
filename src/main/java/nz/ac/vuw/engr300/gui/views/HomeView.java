package nz.ac.vuw.engr300.gui.views;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.controllers.HomeController;

/**
 * Represents a Home Screen view which the application will default to.
 *
 * @author Nathan Duckett
 */
public class HomeView {

    private Scene applicationScene;

    /**
     * Create a new HomeView and attach it to the stage.
     * 
     * @param stage Root application stage to have panels attached to.
     */
    public HomeView(Stage stage) {
        Parent root = null;
        URL layoutFile = getClass().getClassLoader().getResource("layouts/Home.fxml");
        if (layoutFile == null) {
            throw new RuntimeException("Layout file for Home view not found!");
        }

        FXMLLoader loader = new FXMLLoader(layoutFile);
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new Error(e);
        }

        this.applicationScene = new Scene(root);
        stage.setScene(this.applicationScene);

        HomeController controller = loader.getController();
        stage.setOnCloseRequest(e -> controller.shutdown());
        // stage.setMaximized(true);

        stage.show();
        // Set minimum dimensions to 720p - Doesn't support below this
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
    }

    /**
     * Get the internal application scene height.
     * 
     * @return Double representation of the scene height.
     */
    public double getSceneHeight() {
        return this.applicationScene.getHeight();
    }

    /**
     * Get the internal application scene width.
     * 
     * @return Double representation of the scene width.
     */
    public double getSceneWidth() {
        return this.applicationScene.getWidth();
    }
}

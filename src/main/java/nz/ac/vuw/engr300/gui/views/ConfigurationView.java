package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.communications.importers.MonteCarloImporter;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.model.LaunchParameters;
import java.util.function.Consumer;

public class ConfigurationView implements View {

    private static MonteCarloImporter monteCarloImporter = new MonteCarloImporter();
    private static Tab monteCarloTab;
    /**
     * Display the Launch Configurations popup window.
     *
     * @param callBack Callback function to accept LaunchParameters.
     */
    public static void display(Consumer<LaunchParameters> callBack) {


        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Launch Parameters");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        GridPane launchParametersGrid = UiUtil.createGridPane(0, 0, Insets.EMPTY);
        GridPane monteCarloGrid = UiUtil.createGridPane(0, 0, Insets.EMPTY);

        Tab launchParameterTab = new Tab("Launch Parameters", launchParametersGrid);
        monteCarloTab = new Tab("Monte Carlo", new Label("Drake"));
        monteCarloTab.setDisable(monteCarloImporter.getTable().size() == 0);

        tabPane.getTabs().addAll(launchParameterTab, monteCarloTab);

        Scene scene = new Scene(tabPane, 500, 600);

        popupwindow.setResizable(false);
        LaunchParameterView l = new LaunchParameterView(launchParametersGrid, callBack, monteCarloImporter, ConfigurationView::setMonteCarloTabEnabled);
        MonteCarloView m = new MonteCarloView(monteCarloGrid, monteCarloImporter);

        popupwindow.setScene(scene);
        l.initialize();
        m.initialize();
        popupwindow.showAndWait();
    }

    private static void setMonteCarloTabEnabled(){
        monteCarloTab.setDisable(false);
    }
}

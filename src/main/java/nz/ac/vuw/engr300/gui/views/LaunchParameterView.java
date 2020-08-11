package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.components.LaunchParameterInputField;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.model.LaunchParameters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LaunchParameterView implements View {
    private final GridPane root;
    private LaunchParameters parameters;
    private Consumer<LaunchParameters> callBack;

    private List<LaunchParameterInputField> inputFields = new ArrayList<>();

    public LaunchParameterView(GridPane root, LaunchParameters parameters, Consumer<LaunchParameters> callBack) {
        this.root = root;
        this.parameters = parameters;
        this.callBack = callBack;


        UiUtil.addPercentRows(root, 80, 20);
    }

    private void initialize() {

        initializeButtons();
        initializeFields();

    }

    private void initializeButtons() {
        Button exportWeather = new Button("Export Weather Data");
        Button save = new Button("Save");
        save.setOnAction(e -> {
            inputFields.forEach(LaunchParameterInputField::saveField);
            callBack.accept(parameters);

        });
        root.add(UiUtil.createMinimumVerticalSizeVBox(5, Insets.EMPTY, exportWeather, save), 0, 1);
    }

    private void initializeFields() {
        Class<LaunchParameters> clazz = LaunchParameters.class;
        Field[] fields = clazz.getDeclaredFields();
        VBox vbox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10));
        for (Field f : fields) {
            LaunchParameterInputField lpif = new LaunchParameterInputField(f, parameters);
            vbox.getChildren().add(lpif);
            inputFields.add(lpif);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);

        scrollPane.setContent(vbox);

        root.add(scrollPane, 0, 0);
    }


    public LaunchParameterView(GridPane root, Consumer<LaunchParameters> callBack) {
        this(root, new LaunchParameters(), callBack);
    }

    public static void display(Consumer<LaunchParameters> callBack) {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Launch Parameters");
        GridPane root = new GridPane();
        Scene scene= new Scene(root, 350, 400);

        popupwindow.setResizable(false);
        // TODO: Load in previous launch parameters here
        LaunchParameterView l = new LaunchParameterView(root, callBack);

        popupwindow.setScene(scene);
        l.initialize();
        popupwindow.showAndWait();
    }
}

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

/**
 * Represents the popup window that appears when Launch Configurations button is pressed.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class LaunchParameterView implements View {
    private final GridPane root;
    private LaunchParameters parameters;
    private Consumer<LaunchParameters> callBack;
    private List<LaunchParameterInputField> inputFields = new ArrayList<>();

    /**
     * Creates a LaunchParameterView Object.
     *
     * @param root The root GridPane where we will be adding nodes to.
     * @param parameters The LaunchParameters object.
     * @param callBack Callback function to accept LaunchParameters.
     */
    public LaunchParameterView(GridPane root, LaunchParameters parameters, Consumer<LaunchParameters> callBack) {
        this.root = root;
        this.parameters = parameters;
        this.callBack = callBack;
        UiUtil.addPercentRows(root, 80, 20);
    }

    /**
     * Secondary Constructor for when we don't have existing LaunchParameters so use default values.
     * @param root The root GridPane where we will be adding nodes to.
     * @param callBack Callback function to accept LaunchParameters.
     */
    public LaunchParameterView(GridPane root, Consumer<LaunchParameters> callBack) {
        this(root, new LaunchParameters(), callBack);
    }

    /**
     * Initialize buttons and fields for popup window.
     */
    private void initialize() {
        initializeButtons();
        initializeFields();
    }

    /**
     * Initialize buttons for popup window.
     */
    private void initializeButtons() {
        Button exportWeather = new Button("Export Weather Data");
        Button save = new Button("Save");
        save.setOnAction(e -> {
            inputFields.forEach(LaunchParameterInputField::saveField);
            callBack.accept(parameters);

        });
        root.add(UiUtil.createMinimumVerticalSizeVBox(5, Insets.EMPTY, exportWeather, save), 0, 1);
    }

    /**
     * Initialize input fields for popup window.
     */
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

    /**
     * Display the Launch Configurations popup window.
     * @param callBack Callback function to accept LaunchParameters.
     */
    public static void display(Consumer<LaunchParameters> callBack) {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Launch Parameters");
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 350, 400);

        popupwindow.setResizable(false);
        // TODO: Load in previous launch parameters here
        LaunchParameterView l = new LaunchParameterView(root, callBack);
        popupwindow.setScene(scene);
        l.initialize();
        popupwindow.showAndWait();
    }
}

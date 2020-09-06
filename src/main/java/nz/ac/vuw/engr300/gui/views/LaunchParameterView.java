package nz.ac.vuw.engr300.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.components.LaunchParameterInputField;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import nz.ac.vuw.engr300.model.LaunchParameters;
import nz.ac.vuw.engr300.weather.importers.PullWeatherApi;
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
    private final LaunchParameters parameters;
    private final Consumer<LaunchParameters> callBack;
    private final List<LaunchParameterInputField> inputFields = new ArrayList<>();

    /**
     * Creates a LaunchParameterView Object.
     *
     * @param root     The root GridPane where we will be adding nodes to.
     * @param callBack Callback function to accept LaunchParameters.
     */
    public LaunchParameterView(GridPane root, Consumer<LaunchParameters> callBack) {
        this.root = root;
        this.parameters = LaunchParameters.getInstance();
        this.callBack = callBack;
        UiUtil.addPercentRows(root, 4, 66, 30);
    }

    /**
     * Display the Launch Configurations popup window.
     *
     * @param callBack Callback function to accept LaunchParameters.
     */
    public static void display(Consumer<LaunchParameters> callBack) {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Launch Parameters");
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 500, 550);

        popupwindow.setResizable(false);
        LaunchParameterView l = new LaunchParameterView(root, callBack);
        popupwindow.setScene(scene);
        l.initialize();
        popupwindow.showAndWait();
    }

    /**
     * Initialize buttons and fields for popup window.
     */
    private void initialize() {
        this.initializeButtons();
        this.initializeFields();
        this.initializeHeader();
    }

    private void initializeHeader() {
        GridPane header = new GridPane();
        UiUtil.addPercentColumns(header, 45, 45, 10);

        HBox nameWrapper = new HBox(new Label("Name"));
        nameWrapper.setAlignment(Pos.CENTER);

        HBox valueWrapper = new HBox(new Label("Value"));
        valueWrapper.setAlignment(Pos.CENTER);

        HBox enabledWrapper = new HBox(new Label("Use"));
        enabledWrapper.setAlignment(Pos.CENTER);

        header.add(nameWrapper, 0, 0);
        header.add(valueWrapper, 1, 0);
        header.add(enabledWrapper, 2, 0);

        GridPane.setMargin(header, new Insets(0, 2.5, 0, 2.5));
        root.add(header, 0, 0);
    }

    /**
     * Initialize buttons for popup window.
     */
    private void initializeButtons() {
        Label pullDataDescription = new Label("Save and then pull weather and map information based on "
                + "the current latitude and longitude.");
        pullDataDescription.setWrapText(true);
        Button pullData = new Button("Save and Pull data");
        Button exportWeather = new Button("Export Weather Data");
        Button save = new Button("Save");

        save.setOnAction(e -> saveLaunchParameters());

        pullData.setOnAction(e -> {
            saveLaunchParameters();
            try {
                MapImageImporter.importImage(KeyImporter.getKey("maps"),
                        parameters.getLatitude().getValue(), parameters.getLongitude().getValue());
                PullWeatherApi.importWeatherData(KeyImporter.getKey("weather"),
                        parameters.getLatitude().getValue(), parameters.getLongitude().getValue(),
                        "src/main/resources/weather-data");
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error fetching Data");
                alert.setHeaderText("Failed to fetch Map or Weather data");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        });
        root.add(UiUtil.createMinimumVerticalSizeVBox(5, Insets.EMPTY, pullDataDescription,
                pullData, save, exportWeather), 0, 2);
    }

    /**
     * Saves the input field values to the parameters object.
     */
    private void saveLaunchParameters() {
        inputFields.forEach(LaunchParameterInputField::saveField);
        callBack.accept(parameters);
    }

    /**
     * Initialize input fields for popup window.
     */
    private void initializeFields() {
        Class<? extends LaunchParameters> clazz = parameters.getClass();
        Field[] fields = clazz.getDeclaredFields();
        VBox vbox = UiUtil.createMinimumVerticalSizeVBox(0, new Insets(0, 2.5, 0, 2.5));
        for (Field f : fields) {
            if (f.getType().getSimpleName().equals("LaunchParameter")) {
                try {
                    f.setAccessible(true);
                    LaunchParameters.LaunchParameter<?> lp = (LaunchParameters.LaunchParameter<?>) f.get(parameters);

                    LaunchParameterInputField lpif = new LaunchParameterInputField(f, lp);
                    vbox.getChildren().add(lpif);
                    inputFields.add(lpif);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create inputfield!", e);
                }

            }

        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vbox);
        root.add(scrollPane, 0, 1);
    }
}

package nz.ac.vuw.engr300.gui.views;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.components.LaunchParameterInputField;
import nz.ac.vuw.engr300.gui.controllers.WeatherController;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import nz.ac.vuw.engr300.model.LaunchParameters;
import nz.ac.vuw.engr300.weather.importers.PullWeatherApi;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static nz.ac.vuw.engr300.gui.util.UiUtil.addNodeToGrid;

/**
 * Represents the popup window that appears when Launch Configurations button is pressed.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class LaunchParameterView implements View {
    private final GridPane root;
    private final GridPane contentPane;
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

        UiUtil.addPercentRows(root, 5, 95);
        this.contentPane = UiUtil.createGridPane(10, 10, new Insets(10));
        this.contentPane.setBackground(new Background(new BackgroundFill(Colours.CONTENT_BACKGROUND_COLOUR,
                CornerRadii.EMPTY, Insets.EMPTY)));
        UiUtil.addPercentRows(this.contentPane, 70, 30);
        UiUtil.addNodeToGrid(this.contentPane, this.root, 1, 0);
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
        GridPane root = UiUtil.createGridPane(0, 0, Insets.EMPTY);
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

    /**
     * Initializes the header of the LaunchParameterView.
     */
    private void initializeHeader() {
        GridPane header = new GridPane();

        header.setBackground(new Background(new BackgroundFill(Colours.PRIMARY_COLOUR,
                CornerRadii.EMPTY, Insets.EMPTY)));

        UiUtil.addPercentColumns(header, 45, 40, 15);

        header.add(setupHeaderLabel("Name"), 0, 0);
        header.add(setupHeaderLabel("Value"), 1, 0);
        header.add(setupHeaderLabel("Use"), 2, 0);

        root.add(header, 0, 0);
    }

    /**
     * Initialize buttons for popup window.
     */
    private void initializeButtons() {
        Label pullDataDescription = new Label("Save and then pull weather and map information based on "
                + "the current latitude and longitude.");
        pullDataDescription.setTextAlignment(TextAlignment.CENTER);
        pullDataDescription.setWrapText(true);

        Button pullData = new Button("Save and Pull data");
        Button exportSimulationParameters = new Button("Export Simulation Parameters");
        Button save = new Button("Save");

        pullData.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
        save.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
                CornerRadii.EMPTY, Insets.EMPTY)));
        exportSimulationParameters.setBackground(new Background(new BackgroundFill(Color.YELLOW,
                CornerRadii.EMPTY, Insets.EMPTY)));

        save.setOnAction(e -> saveLaunchParameters());

        exportSimulationParameters.setOnAction(e -> exportSimulationParameters());

        pullData.setOnAction(e -> {
            saveLaunchParameters();
            try {
                MapImageImporter.importImage(KeyImporter.getKey("maps"),
                        parameters.getLatitude().getValue(), parameters.getLongitude().getValue());
                PullWeatherApi.importWeatherData(KeyImporter.getKey("weather"),
                        parameters.getLatitude().getValue(), parameters.getLongitude().getValue(),
                        "src/main/resources/weather-data");
            } catch (Exception | Error exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error fetching Data");
                alert.setHeaderText("Failed to fetch Map or Weather data");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        });

        VBox vbox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10), pullDataDescription,
                pullData, save, exportSimulationParameters);
        // Literally just for setting background colour
        vbox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Set it to hug the warnings above it
        GridPane.setValignment(vbox, VPos.TOP);

        contentPane.add(vbox, 0, 1);
    }

    /**
     * Saves the input field values to the parameters object.
     */
    private void saveLaunchParameters() {
        inputFields.forEach(LaunchParameterInputField::saveField);
        callBack.accept(parameters);
    }

    /**
     * Creates a label with the given text and sets it's properties for use as a header label.
     *
     * @param labelText The label text to use.
     * @return          The label after creation and configuration.
     */
    private Label setupHeaderLabel(String labelText) {
        Label label = new Label(labelText);
        label.setFont(new Font("Arial", 18));
        label.setTextFill(Color.WHITE);
        GridPane.setValignment(label, VPos.CENTER);
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    /**
     * Initialize input fields for popup window.
     */
    private void initializeFields() {
        Class<? extends LaunchParameters> clazz = parameters.getClass();
        Field[] fields = clazz.getDeclaredFields();
        ListView<LaunchParameterInputField> listView = new ListView<>();
        for (Field f : fields) {
            if (f.getType().getSimpleName().equals("LaunchParameter")) {
                try {
                    f.setAccessible(true);
                    LaunchParameters.LaunchParameter<?> lp = (LaunchParameters.LaunchParameter<?>) f.get(parameters);

                    LaunchParameterInputField lpif = new LaunchParameterInputField(f, lp);

                    inputFields.add(lpif);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create inputfield!", e);
                }

            }

        }

        listView.setItems(FXCollections.observableList(inputFields));
        contentPane.add(listView, 0, 0);
    }

    private void exportSimulationParameters() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a place to save the file.");
        fileChooser.setInitialDirectory(new File("src/main/resources/"));
        File selectedFile = fileChooser.showSaveDialog(null);

        WeatherData weatherData = WeatherController.getInstance().getWeatherData();

        if (weatherData == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Weather Data not Found.");
            alert.setHeaderText("Please pull weather Data.");
            alert.showAndWait();
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(selectedFile));
            writer.println("windSpeed,windSpeedSigma,rodAngle,rodAngleSigma,rodDirection,rodDirectionSigma,lat,long");

            // Note: windSpeedSigma, rodAngle, rodAngleSigma, rodDirection, rodDirectionSigma are currently hardcoded.
            writer.printf("%f,%f,%f,%f,%f,%f,%f,%f", weatherData.getWindSpeed(), 5d, 45d, 5d, 0d, 5d,
                    parameters.getLatitude().getValue(), parameters.getLongitude().getValue());

            writer.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Exporting Simulation Parameters");
            alert.setHeaderText("Failed to export simulation parameters.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

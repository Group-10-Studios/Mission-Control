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
import nz.ac.vuw.engr300.communications.importers.MonteCarloImporter;
import nz.ac.vuw.engr300.gui.components.LaunchParameterInputField;
import nz.ac.vuw.engr300.gui.controllers.WeatherController;
import nz.ac.vuw.engr300.gui.util.Colours;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import nz.ac.vuw.engr300.model.LaunchParameters;
import nz.ac.vuw.engr300.weather.importers.PullWeatherApi;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;
import nz.ac.vuw.engr300.weather.model.WeatherData;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
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
    private final GridPane contentPane;
    private final LaunchParameters parameters;
    private final Consumer<LaunchParameters> callBack;
    private final List<LaunchParameterInputField> inputFields = new ArrayList<>();

    private final MonteCarloImporter monteCarloImporter = new MonteCarloImporter();

    private static String WEATHER_SAVE_FILE_DIR = "src/main/resources/weather-data/";
    private static String MAP_SAVE_FILE_DIR = "src/main/resources/map-data/";
    private static String BASE_FILE_DIRECTORY = "src/main/resources/";

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
        Scene scene = new Scene(root, 500, 600);

        popupwindow.setResizable(false);
        LaunchParameterView l = new LaunchParameterView(root, callBack);
        popupwindow.setScene(scene);
        l.initialize();
        popupwindow.showAndWait();
    }

    private static void displayPopup(Alert.AlertType alertType, String title, String subtitle, String body) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(subtitle);
        alert.setContentText(body);
        alert.showAndWait();
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
        Button importMonteCarlo = new Button("Import Monte Carlo");
        pullData.setId("pullDataBtn");
        exportSimulationParameters.setId("exportSimulationParametersBtn");
        save.setId("saveBtn");
        importMonteCarlo.setId("importMonteCarloBtn");

        pullData.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
        save.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED,
                CornerRadii.EMPTY, Insets.EMPTY)));
        exportSimulationParameters.setBackground(new Background(new BackgroundFill(Color.YELLOW,
                CornerRadii.EMPTY, Insets.EMPTY)));
        importMonteCarlo.setBackground(new Background(new BackgroundFill(Color.THISTLE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        save.setOnAction(e -> saveLaunchParameters());

        exportSimulationParameters.setOnAction(e -> exportSimulationParameters());

        pullData.setOnAction(e -> {
            saveLaunchParameters();
            try {
                MapImageImporter.importImage(KeyImporter.getKey("maps"),
                        parameters.getLatitude().getValue(), parameters.getLongitude().getValue(),
                        MAP_SAVE_FILE_DIR);
                PullWeatherApi.importWeatherData(KeyImporter.getKey("weather"),
                        parameters.getLatitude().getValue(), parameters.getLongitude().getValue(),
                        WEATHER_SAVE_FILE_DIR);
                // Update weather data.
                WeatherController.getInstance().setWeatherData(
                        WeatherController.getInstance().buildWeatherFileFromLocation(
                                parameters.getLatitude().getValue(), parameters.getLongitude().getValue()));
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error fetching Data");
                alert.setHeaderText("Failed to fetch Map or Weather data");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        });

        importMonteCarlo.setOnAction(e -> importMonteCarlo() );
        VBox vbox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10), pullDataDescription,
                pullData, save, exportSimulationParameters, importMonteCarlo);
        // Literally just for setting background colour
        vbox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Set it to hug the warnings above it
        GridPane.setValignment(vbox, VPos.TOP);

        contentPane.add(vbox, 0, 1);
    }

    private void importMonteCarlo() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a place to save the file.");
        fileChooser.setInitialDirectory(new File(BASE_FILE_DIRECTORY));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile == null) {
            displayPopup(Alert.AlertType.WARNING,
                    "File was not selected.", "Please select a valid file", "");
            return;
        }

        monteCarloImporter.importData(selectedFile.getAbsolutePath());
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
     * @return The label after creation and configuration.
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

    /**
     * Saves the simulation parameters and weather data into a csv file that the user chooses.
     * If there is no weather data found a alert is thrown.
     * Saves the following parameters:
     * WindSpeed, windSpeedSigma, rodAngle, rodAngleSigma, rodDirection, rodDirectionSigma, lat, long.
     */
    private void exportSimulationParameters() {
        WeatherData weatherData;

        try {
            WeatherImporter weatherImporter = new WeatherImporter(
                    WEATHER_SAVE_FILE_DIR + parameters.getLatitude().getValue()
                            + "-" + parameters.getLongitude().getValue() + ".json");
            weatherData = weatherImporter.getWeather(0);
        } catch (FileNotFoundException e) {
            displayPopup(Alert.AlertType.WARNING, "Weather Data not found",
                    "Please pull weather data.",
                    e.getMessage());
            return;
        }

        File selectedFile = getCsvFile();
        if (selectedFile == null) {
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(selectedFile, StandardCharsets.UTF_8));
            writer.println("windSpeed,windSpeedSigma,rodAngle,rodAngleSigma,rodDirection,rodDirectionSigma,lat,long");

            // Note: windSpeedSigma, rodAngle, rodAngleSigma, rodDirection, rodDirectionSigma are currently hardcoded.
            writer.printf("%f,%f,%f,%f,%f,%f,%f,%f", weatherData.getWindSpeed(), 5d, 45d, 5d, 0d, 5d,
                    parameters.getLatitude().getValue(), parameters.getLongitude().getValue());

            writer.close();
        } catch (IOException e) {
            displayPopup(Alert.AlertType.ERROR,
                    "Error Exporting Simulation Parameters",
                    "Failed to export simulation parameters", e.getMessage());
        }
    }

    /**
     * Displays a FileChooser dialog to the user for selecting a file to save a CSV into. This function
     * will also ensure the selected file is valid (not null), and has the correct extension.
     *
     * @return  The csv file (if successful - null otherwise)
     */
    private File getCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a place to save the file.");
        fileChooser.setInitialDirectory(new File(BASE_FILE_DIRECTORY));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile == null) {
            displayPopup(Alert.AlertType.WARNING,
                    "File was not selected.", "Please select a valid file", "");
            return null;
        }

        if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
            return new File(selectedFile.getAbsolutePath() + ".csv");
        }

        return selectedFile;
    }
}

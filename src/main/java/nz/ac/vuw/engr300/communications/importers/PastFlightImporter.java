package nz.ac.vuw.engr300.communications.importers;

import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * PastFlightImporter is a simulation class to allow loading in PastFlight information recorded from
 * SerialCommunications into log files which can be replayed to the user. This allows selecting a file
 * generated when the application ran a previous flight and playing back what happened in the GUI.
 *
 * @author Nathan Duckett
 */
public class PastFlightImporter implements RocketDataImporter<List<Object>>  {
    private static final Logger LOGGER = Logger.getLogger(SerialCommunications.class);
    private final List<Consumer<List<Object>>> observers = new ArrayList<>();
    private final List<String> rawData = new ArrayList<>();
    private long previousTimeStamp = -1;
    private boolean streamRunning = false;
    private String tableName;

    /**
     * Import the data from the provided previous flight file to be simulated into the application.
     *
     * @param filePath String path to the previous flight file to be read.
     */
    public void importData(String filePath) {
        rawData.clear();
        File file = new File(filePath);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            LOGGER.warn("Invalid file name provided", e);
            throw new IllegalArgumentException("Invalid file name provided.", e);
        } catch (IOException e) {
            LOGGER.warn("Past flight file has invalid formatting", e);
            throw new IllegalArgumentException("Invalid encoding.", e);
        }

        try {
            // Runs under the assumption the format is "# tableName" with single space splitting table name.
            String tableLine = reader.readLine();
            // Have to assert even though checked in checkValidHeader due to spotbugs
            assert tableLine != null;
            checkValidHeader(tableLine);
            this.tableName = tableLine.split(" ")[1];
            String headerLine = reader.readLine();
            // Have to assert even though checked in checkValidHeader due to spotbugs
            assert headerLine != null;
            checkValidHeader(headerLine);
            String[] splitHeader = headerLine.split(" ");
            List<String> expectedHeaders = CsvConfiguration.getInstance().getTable(tableName).getTitles();
            // Start from 1 to avoid "#"
            for (int i = 1; i < splitHeader.length; i++) {
                if (!expectedHeaders.get(i - 1).equals(splitHeader[i])) {
                    LOGGER.error("There was an issue with the past flight file not matching the configuration");
                    LOGGER.error("<" + expectedHeaders.get(i - 1) + "> does not match <" + splitHeader[i] + ">");
                    throw new RuntimeException("The headers provided in the past flight log file does not"
                            + " match your communications.json file");
                }
            }

            // Read all lines into rawData (skip any commented lines)
            reader.lines().filter(s -> !s.startsWith("#")).forEach(rawData::add);
        } catch (IOException e) {
            LOGGER.error("Error while reading file contents", e);
            throw new RuntimeException(e);
        }
        try {
            reader.close();
        } catch (IOException e) {
            LOGGER.error("Error closing reader", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the stream of data to all of the subscribed clients.
     */
    public void start() {
        this.streamRunning = true;
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable(tableName);
        int positionOfTimeStamp = table.getCsvIndexOf("timestamp");

        new Thread(() -> {
            try {
                long previousTime = 0;
                List<String> dataCopy = new ArrayList<>(this.rawData);
                for (String rawData : dataCopy) {
                    String timestamp = rawData.split(",")[positionOfTimeStamp];
                    long currentTime = Long.parseLong(timestamp);
                    // Only run when timestamp is above 0 already
                    if (previousTime > 0) {
                        Thread.sleep(currentTime - previousTime);
                    }
                    if (!streamRunning) {
                        break;
                    }
                    table.addRow(rawData);

                    // Send information to observers
                    List<Object> data = table.latestData();
                    long dataTimeStamp = table.matchValueToColumn(data.get(table.getCsvIndexOf("timestamp")),
                            "timestamp", Long.class);
                    // Check if previous time stamp exists otherwise start from 0
                    long difference = previousTimeStamp != -1 ? dataTimeStamp - previousTimeStamp : 0;
                    previousTimeStamp = dataTimeStamp;
                    data.set(0, difference);

                    observers.forEach((observer) -> observer.accept(data));
                    previousTime = currentTime;
                }
                this.streamRunning = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Get the table name of this past flight log file.
     *
     * @return String containing the table name this log file uses.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Check that the extractedLine matches a valid header for the past flights log file.
     *
     * @param extractedLine Extracted string from the log file to be checked.
     */
    private void checkValidHeader(String extractedLine) {
        if (extractedLine == null) {
            throw new RuntimeException("Incoming Past Flight file is not valid");
        } else if (!extractedLine.startsWith("# ")) {
            throw new RuntimeException("Past Flight log file does not match the expected comment header layout");
        }
    }

    /**
     * Stops the stream of data to subscribed clients.
     */
    public void stop() {
        this.streamRunning = false;
    }

    @Override
    public void subscribeObserver(Consumer<List<Object>> observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeObserver(Consumer<List<Object>> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void unsubscribeAllObservers() {
        this.observers.clear();
    }
}

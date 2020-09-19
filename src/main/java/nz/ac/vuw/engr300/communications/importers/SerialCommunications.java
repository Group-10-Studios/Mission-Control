package nz.ac.vuw.engr300.communications.importers;

import com.fazecast.jSerialComm.SerialPort;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * SerialCommunications handle the incoming/outgoing data to serial ports.
 * All data is imported through into a CsvTableDefinition defined for the connection.
 *
 * @author Nathan Duckett
 */
public class SerialCommunications implements RocketDataImporter<List<Object>> {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
    private static final Logger LOGGER = Logger.getLogger(SerialCommunications.class);
    private final List<Consumer<List<Object>>> observers = new ArrayList<>();
    private boolean systemRunning = true;
    private Thread listenThread;
    private long previousTimeStamp = -1;
    private boolean lastFailed = false;
    private SerialPort comPort;

    /**
     * Contents for the serialApplicationThread which handles the incoming data and sending
     * to the corresponding CSV table which requires the data.
     *
     * @param incomingTableName Defined table definition within communications.json to handle this data.
     */
    private void serialApplicationThread(String incomingTableName) {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable(incomingTableName);

        // Break if no serial device is selected or if the table doesn't exist.
        if (comPort == null || table == null) {
            return;
        }

        // Only create this outputFile if the comPort is valid.
        String outputFileName = createOutputFile(incomingTableName,
                table.getTitles().stream().reduce("", (a, b) -> a + " " + b));

        comPort.openPort();

        // Required to set timeout to blocking temporarily while receiving data.
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        while (systemRunning) {
            InputStream in = comPort.getInputStream();

            try {
                StringBuilder stringBuilder = new StringBuilder();
                char nextValue;
                while ((nextValue = (char) in.read()) != '\n') {
                    stringBuilder.append(nextValue);
                }

                table.addRow(stringBuilder.toString());
                // Send information to observers
                List<Object> data = table.latestData();
                long timestamp = table.matchValueToColumn(data.get(table.getCsvIndexOf("timestamp")),
                        "timestamp", Long.class);
                // Check if previous time stamp exists otherwise start from 0
                long difference = previousTimeStamp != -1 ? timestamp - previousTimeStamp : 0;
                previousTimeStamp = timestamp;
                data.set(0, difference);
                observers.forEach(observer -> observer.accept(data));

                in.close();
                lastFailed = false;
                recordDataToOutputFile(outputFileName, stringBuilder.toString());
            } catch (Exception e) {
                LOGGER.error("Error reading data within serial communications", e);
                // Try to recover on next rotation of usage.
                if (lastFailed) {
                    LOGGER.error("Could not recover on second rotation of incoming data, exiting application");
                    throw new Error(e);
                } else {
                    lastFailed = true;
                }
            }
        }
        comPort.closePort();
    }

    /**
     * Stop the system from listening to the serial port.
     */
    public void stopListening() {
        systemRunning = false;

        // Ensure the thread exists.
        if (this.listenThread != null) {
            this.listenThread.interrupt();
        }
    }

    /**
     * Start the system listening to the serial port.
     */
    public void startListening() {
        systemRunning = true;

        this.listenThread = new Thread(() -> serialApplicationThread("incoming-avionics"));
        this.listenThread.start();
    }

    /**
     * Method to update which serial device the application is listening to.
     *
     * @param serialPort The new serial device for the application to listen to.
     */
    public void updateSerialPort(SerialPort serialPort) {
        stopListening();
        this.comPort = serialPort;
        startListening();
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

    /**
     * Create an incomingRocketData log file which all incoming serial communications will be written for historical
     * purposes. This creates a CSV file with a set header containing the tableName and space separated column headers.
     *
     * @param tableName Name of the table this log file represents from the communications.json configuration.
     * @param tableStructure String built of space separated column headers from the table used.
     * @return FileName of the corresponding log file to write the content into.
     */
    private String createOutputFile(String tableName, String tableStructure) {
        File outputFile = new File("incomingRocketData_" + LocalDateTime.now().format(dateFormatter) + ".csv");
        try {
            // Use PrintStream to create the file using Java File creation above.
            PrintStream printStream = new PrintStream(outputFile, StandardCharsets.UTF_8);
            printStream.println("# " + tableName);
            // No space necessary as already indented.
            printStream.append("#").append(tableStructure).append("\n");
            printStream.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not create output file <" + outputFile.getAbsolutePath() + ">", e);
            LOGGER.warn("This could be caused by file permission errors on your machine");
            throw new Error("Unable to record data to a log file, please check the logs for more information", e);
        } catch (IOException e) {
            LOGGER.error("Error while creating log file due to charset violations", e);
            throw new RuntimeException("Recording of incoming serial data failed.", e);
        }

        return outputFile.getAbsolutePath();
    }

    /**
     * Record data to the incomingRocketData log file with the data values in the form of the dataString.
     *
     * @param outputFileName Log file name for the corresponding file to write the content into.
     * @param dataString RAW CSV string extracted from the serial communications to write into the log file.
     */
    private void recordDataToOutputFile(String outputFileName, String dataString) {
        try {
            // Must be opened in append mode using a BufferedWriter for append mode and less IO operations.
            // Must be wrapped in an OutputStreamWriter to allow for UTF-8 charset spec to match project restrictions.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileName, true), StandardCharsets.UTF_8));
            // Ensure line break at end
            writer.write(dataString + "\n");
            writer.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not find output file <" + outputFileName + ">", e);
            LOGGER.error("Please verify this was created previously as it should already exist");
            LOGGER.warn("This could be caused by file permission errors on your machine");
            throw new RuntimeException("Recording of incoming serial data failed.", e);
        } catch (IOException e) {
            LOGGER.error("Error while writing incoming serial data to log file", e);
            throw new RuntimeException("Recording of incoming serial data failed.", e);
        }
    }
}

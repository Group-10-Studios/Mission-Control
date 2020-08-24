package nz.ac.vuw.engr300.communications.importers;

import com.fazecast.jSerialComm.SerialPort;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import org.apache.log4j.Logger;

import java.io.InputStream;
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
    private static final Logger LOGGER = Logger.getLogger(SerialCommunications.class);
    private final List<Consumer<List<Object>>> observers = new ArrayList<>();
    private boolean systemRunning = true;
    private Thread listenThread;
    private long previousTimeStamp = -1;
    private boolean lastFailed = false;

    /**
     * Contents for the serialApplicationThread which handles the incoming data and sending
     * to the corresponding CSV table which requires the data.
     *
     * @param incomingTableName Defined table definition within communications.json to handle this data.
     */
    private void serialApplicationThread(String incomingTableName) {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable(incomingTableName);

        // Assuming port three for now will allow user choice later.
        SerialPort[] comPorts = SerialPort.getCommPorts();
        if (comPorts.length < 3) {
            // Break early and don't register as no serial ports available.
            return;
        }
        SerialPort comPort = comPorts[2];
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

                // Verify the table is not null in case of bad definition or communications.json
                if (table != null) {
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
                }

                in.close();
                lastFailed = false;
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
     * Temporary testing method before implementing actual tests.
     * @param args Application args
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting application");
        SerialCommunications s = new SerialCommunications();
        s.startListening();
        // Run for 50 seconds
        Thread.sleep(5000);
        s.stopListening();
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

package nz.ac.vuw.engr300.communications.importers;

import com.fazecast.jSerialComm.SerialPort;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.communications.model.RocketData;
import nz.ac.vuw.engr300.communications.model.RocketStatus;

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
public class SerialCommunications implements RocketDataImporter {
    private final List<Consumer<RocketData>> observers = new ArrayList<>();
    private boolean systemRunning = true;
    private Thread listenThread;

    /**
     * Contents for the serialApplicationThread which handles the incoming data and sending
     * to the corresponding CSV table which requires the data.
     *
     * @param incomingTableName Defined table definition within communications.json to handle this data.
     */
    private void serialApplicationThread(String incomingTableName) {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable(incomingTableName);

        // Assuming port three for now will allow user choice later.
        SerialPort comPort = SerialPort.getCommPorts()[2];
        comPort.openPort();

        // Required to set timeout to blocking temporarily while receiving data.
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        try {
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
                        RocketData data = parseIncomingData(table.latestData());
                        observers.forEach(observer -> observer.accept(data));
                    }

                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }

    /**
     * Method to parse the Object list within a CSV row into a RocketData object to interface with our graphs.
     *
     * @param incomingData List of objects contained within the data extracted from serial.
     * @return RocketData object which can be used to update graph values.
     */
    private RocketData parseIncomingData(List<Object> incomingData) {

        return null;
    }

    /**
     * Stop the system from listening to the serial port.
     */
    public void stopListening() {
        systemRunning = false;
        this.listenThread.interrupt();
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
    public void subscribeObserver(Consumer<RocketData> observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeObserver(Consumer<RocketData> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void unsubscribeAllObservers() {
        this.observers.clear();
    }
}

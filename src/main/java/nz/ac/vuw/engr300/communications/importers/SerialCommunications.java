package nz.ac.vuw.engr300.communications.importers;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;

/**
 * SerialCommunications handle the incoming/outgoing data to serial ports.
 * All data is imported through into a CsvTableDefinition defined for the connection.
 *
 * @author Nathan Duckett
 */
public class SerialCommunications {

    private boolean systemRunning = true;

    /**
     * Contents for the serialApplicationThread which handles the incoming data and sending
     * to the corresponding CSV table which requires the data.
     */
    private void serialApplicationThread() {
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
                    System.out.println(stringBuilder.toString());
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }

    /**
     * Stop the system from listening to the serial port.
     */
    public void stopListening() {
        systemRunning = false;
    }

    /**
     * Temporary testing method before implementing actual tests.
     * @param args Application args
     */
    public static void main(String[] args) {
        new SerialCommunications().serialApplicationThread();
    }
}

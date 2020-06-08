package nz.ac.vuw.engr300.communications.importers;

import nz.ac.vuw.engr300.communications.model.RocketData;
import nz.ac.vuw.engr300.communications.model.RocketEvent;
import nz.ac.vuw.engr300.communications.model.RocketStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple implementation of the template interface, used for importing and
 * stream any of the dummy data files provided.
 *
 * @author Tim Salisbury
 */
public class OpenRocketImporter implements RocketDataImporter {

    private static final Pattern EVENT_REGEX = Pattern
            .compile("^# Event (\\w+) occurred at t=(\\d*\\.?\\d*?) seconds$");
    private static final Pattern STATUS_REGEX = Pattern.compile("^(((-?\\d*\\.?\\d*(e-?\\d*)?)\\s*)|NaN)+$");
    // Note requires to be split like this for checkstyle
    // private static final Pattern HEADER_REGEX = Pattern.compile("#.*Time
    // \\(s\\).*Altitude \\(m\\).*Total
    // velocity \\(m/s\\).*Total acceleration \\(m/s²\\).*Latitude \\(°\\).
    // *Longitude \\(°\\).*Angle of attack \\(°\\)\\n");

    // NOTE: This has to be in order in which they appear in the exported OpenRocketData
    private static final List<String> REQUIRED_VALUES = Arrays.asList(
            "Time (s)",
            "Altitude (m)",
            "Vertical velocity (m/s)",
            "Vertical acceleration (m/s²)",
            "Total velocity (m/s)",
            "Total acceleration (m/s²)",
            "Lateral velocity (m/s)",
            "Lateral acceleration (m/s²)",
            "Latitude (°)",
            "Longitude (°)",
            "Angle of attack (°)",
            "Roll rate (°/s)",
            "Pitch rate (°/s)",
            "Yaw rate (°/s)");

    private static final Pattern HEADER_REGEX;

    static {
        StringBuilder headerlineBuilder = new StringBuilder();
        headerlineBuilder.append("#.*");
        REQUIRED_VALUES.forEach(value -> {
            headerlineBuilder.append(value).append(".*");
        });

        String pattern = headerlineBuilder.toString();
        pattern = pattern.replaceAll("\\(", "\\\\(");
        pattern = pattern.replaceAll("\\)", "\\\\)");
        HEADER_REGEX = Pattern.compile(pattern);
    }

    private final List<Consumer<RocketData>> observers = new ArrayList<>();
    private final List<RocketData> data = new ArrayList<>();
    private volatile boolean streamRunning = false;

    public OpenRocketImporter() {

    }

    /**
     * Imports the data given in the file path and populates the data array with
     * said data.
     *
     * @param filePath The path of the file to import data from
     */
    public void importData(String filePath) {
        data.clear();
        File file = new File(filePath);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Invalid file name provided.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid encoding.", e);
        }
        int[] parameterIndices = getParameterIndicesFromHeader(reader);
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Matcher statusRegexMatcher = STATUS_REGEX.matcher(line);
            Matcher eventRegexMatcher = EVENT_REGEX.matcher(line);

            if (statusRegexMatcher.find()) {
                // Get all of the values
                final double[] values = Arrays.stream(line.split("\\s+")).map(Double::parseDouble)
                        .mapToDouble(Double::doubleValue).map(value -> Double.isNaN(value) ? 0 : value).toArray();
                // Extract only the required ones
                double[] actualValues = new double[parameterIndices.length];
                Arrays.setAll(actualValues, i -> values[parameterIndices[i]]);
                data.add(new RocketStatus(actualValues));
            } else if (eventRegexMatcher.find()) {
                data.add(new RocketEvent(RocketEvent.EventType.valueOf(eventRegexMatcher.group(1)), // Event type
                        Double.parseDouble(eventRegexMatcher.group(2)) // Event time
                ));
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the header line from the given reader, then extracts the indices of the
     * columns in the CSV of the attributes that Mission Control requires.
     *
     * @param reader The buffered reader to find the header line in
     * @return The indices of the attributes that mission control requires
     */
    private int[] getParameterIndicesFromHeader(BufferedReader reader) {
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                break;
            }

            Matcher headerlineMatcher = HEADER_REGEX.matcher(line);

            if (headerlineMatcher.find()) {
                String[] splitValues = line.toLowerCase().substring(2).split("\t");
                return REQUIRED_VALUES.stream().map(value -> Arrays.asList(splitValues).indexOf(value.toLowerCase()))
                        .mapToInt(Integer::intValue).toArray();
            }

        }

        throw new IllegalArgumentException("File provided does not contain a header line!");
    }

    public List<RocketData> getData() {
        return data;
    }

    /**
     * Starts the stream of data to all of the subscribed clients.
     */
    public void start() {
        this.streamRunning = true;
        new Thread(() -> {
            try {
                long previousTime = 0;
                List<RocketData> dataCopy = new ArrayList<>(this.data);
                for (RocketData data : dataCopy) {
                    if (!streamRunning) {
                        break;
                    }
                    long currentTime = (long) (data.getTime() * 1000);
                    Thread.sleep(currentTime - previousTime);
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
     * Stops the stream of data to subscribed clients.
     */
    public void stop() {
        this.streamRunning = false;
    }

    @Override
    public void subscribeObserver(Consumer<RocketData> observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeObserver(Consumer<RocketData> observer) {
        this.observers.remove(observer);
    }
}

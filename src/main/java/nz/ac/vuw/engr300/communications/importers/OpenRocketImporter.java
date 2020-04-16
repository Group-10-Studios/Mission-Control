package nz.ac.vuw.engr300.communications.importers;

import nz.ac.vuw.engr300.communications.model.RocketData;
import nz.ac.vuw.engr300.communications.model.RocketEvent;
import nz.ac.vuw.engr300.communications.model.RocketStatus;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple implementation of the template interface, used for importing and stream any of the dummy data files provided
 */
public class OpenRocketImporter implements RocketDataImporter {

    private static final Pattern EVENT_REGEX = Pattern.compile("^# Event (\\w+) occurred at t=(\\d*\\.?\\d*?) seconds$");
    private static final Pattern STATUS_REGEX = Pattern.compile("(\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)\\t(-?\\d*\\.?\\d*(e-?\\d*)?)");
//    private static final Pattern STATUS_REGEX = Pattern.compile("^(\\d*\\.?\\d*)(\\t-?\\d*\\.?\\d*(e-?\\d*)?){7}$");

    private List<Consumer<RocketData>> observers;
    private List<RocketData> data;

    public OpenRocketImporter(String filePath) throws IOException {
        this.observers = new ArrayList<>();
        this.data = new ArrayList<>();
        importData(filePath);
    }

    /**
     * Imports the data given in the file path and populates the data array with said data
     *
     * @param filePath  The path of the file to import data from
     * @throws IOException  If the file is invalid
     */
    private void importData(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine()) != null){
            Matcher statusRegexMatcher = STATUS_REGEX.matcher(line);
            Matcher eventRegexMatcher = EVENT_REGEX.matcher(line);

            if(statusRegexMatcher.find()){
                data.add(new RocketStatus(
                        Double.parseDouble(statusRegexMatcher.group(1)),    //Time (s)
                        Double.parseDouble(statusRegexMatcher.group(3)),    //Altitude (m)
                        Double.parseDouble(statusRegexMatcher.group(5)),    //Vertical Velocity (m/s)
                        Double.parseDouble(statusRegexMatcher.group(7)),    //Vertical Acceleration (m/s^2)
                        Double.parseDouble(statusRegexMatcher.group(9)),    //Total Velocity (m/s)
                        Double.parseDouble(statusRegexMatcher.group(11)),    //Total Acceleration (m/s^2)
                        Double.parseDouble(statusRegexMatcher.group(13)),    //Distance east of launch (m)
                        Double.parseDouble(statusRegexMatcher.group(15))     //Distance north of launch (m)
                ));
            }else if(eventRegexMatcher.find()){
                data.add(new RocketEvent(
                        RocketEvent.EventType.valueOf(eventRegexMatcher.group(1)),    //Event type
                        Double.parseDouble(eventRegexMatcher.group(2))                //Event time
                ));
            }
        }
        reader.close();
    }

    public List<RocketData> getData() {
        return data;
    }

    /**
     * Starts the stream of data to all of the subscribed clients
     */
    public void start(){
        new Thread(()->{
            long start = System.currentTimeMillis();
            try{
                long previousTime = 0;
                for (RocketData data : this.data) {
                    long currentTime = (long)(data.getTime() * 1000);
                    Thread.sleep(currentTime - previousTime);
                    observers.forEach((observer)->observer.accept(data));
                    previousTime = currentTime;
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("Completed in: " + (System.currentTimeMillis() - start));
        }).start();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void subscribeObserver(Consumer<RocketData> observer) {
        observers.add(observer);
    }
}

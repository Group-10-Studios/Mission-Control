package nz.ac.vuw.engr300.communications;

import nz.ac.vuw.engr300.communications.importers.OpenRocketImporter;
import nz.ac.vuw.engr300.communications.model.RocketData;
import nz.ac.vuw.engr300.communications.model.RocketEvent;
import nz.ac.vuw.engr300.communications.model.RocketStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to ensure that the OpenRocketImporter class works as expected, these tests require
 * that two files are present in the tests resource directory, that being "RocketDataTestFile1.csv",
 * and "RocketDataTestFile2.csv"
 *
 * @author Tim Salisbury
 */
public class OpenRocketImporterTests {

    /**
     * Basic data import, just to ensure the importer
     * parses the correct file correctly.
     */
    @Test
    public void test_basic_import(){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData(getClass().getClassLoader().getResource("RocketDataTestFile1.csv").getPath());
    }

    /**
     * Ensures all the data within the file is parsed and stored
     */
    @Test
    public void test_import_data_sizes(){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData(getClass().getClassLoader().getResource("RocketDataTestFile1.csv").getPath());
        assertEquals(425, importer.getData().size());

        int numOfEvents = (int) importer.getData().stream()
                .filter(rocketData -> rocketData instanceof RocketEvent).count();

        int numOfStatuses = (int) importer.getData().stream()
                .filter(rocketData -> rocketData instanceof RocketStatus).count();

        assertEquals(416, numOfStatuses);
        assertEquals(9, numOfEvents);
    }

    /**
     * Ensures all the data within the file is parsed correctly
     */
    @Test
    public void test_import_data_values(){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData(getClass().getClassLoader().getResource("RocketDataTestFile2.csv").getPath());
        List<RocketData> data = importer.getData();

        testRocketEvent(data.get(0), 0.25, RocketEvent.EventType.LAUNCH);
        testRocketStatus(data.get(1), 0.375, 1.1, 1.2, 1.3 ,1.4, 1.5, 1.6);
        testRocketEvent(data.get(2), 0.5, RocketEvent.EventType.IGNITION);
        testRocketStatus(data.get(3), 0.625, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6);
        testRocketEvent(data.get(4), 0.75, RocketEvent.EventType.LIFTOFF);
        testRocketStatus(data.get(5), 0.875, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6);

    }

    /**
     * Basic function contract testing
     */
    @Test
    public void test_invalid_file_name(){
        OpenRocketImporter importer = new OpenRocketImporter();
        assertThrows(IllegalArgumentException.class, ()->importer.importData("invalid file path"));
    }

    /**
     * Ensures the callback functionality of the OpenRocketImporter works as expected
     */
    @Test
    public void test_call_back(){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData(getClass().getClassLoader().getResource("RocketDataTestFile2.csv").getPath());
        List<RocketData> callBackData = new ArrayList<>();
        importer.subscribeObserver(callBackData::add);
        importer.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail(e);
        }

        assertEquals(importer.getData(), callBackData);
    }

    /**
     * Tests a RocketData instance against the values provided
     */
    public void testRocketEvent(RocketData data, double time, RocketEvent.EventType eventType){
        assertTrue(data instanceof RocketEvent);
        RocketEvent event = (RocketEvent)data;
        assertEquals(time, event.getTime());
        assertEquals(eventType, event.getEventType());
    }

    /**
     * Tests a RocketData instance against the values provided
     */
    public void testRocketStatus(RocketData data, double time, double altitude, double totalVelocity, double totalAcceleration,
                                 double latitude, double longitude, double angleOfAttack){

        assertTrue(data instanceof RocketStatus);
        RocketStatus status = (RocketStatus)data;
        assertEquals(time, status.getTime());
        assertEquals(altitude, status.getAltitude());
        assertEquals(totalVelocity, status.getTotalVelocity());
        assertEquals(totalAcceleration, status.getTotalAcceleration());
        assertEquals(latitude, status.getLatitude());
        assertEquals(longitude, status.getLongitude());
        assertEquals(angleOfAttack, status.getAngleOfAttack());
    }
}

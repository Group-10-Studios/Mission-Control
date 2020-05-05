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
 * that two files are present in the tests resource directory, that being "TestDataWithExtraAttributes.csv",
 * and "FullyCorrectTestData.csv"
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
        importer.importData(getClass().getClassLoader().getResource("FullyCorrectRocketData.csv").getPath());
    }

    /**
     * Tests that the importer can account for extra attributes exported with the data
     */
    @Test
    public void test_extra_attribute_import(){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData("src/test/resources/RocketDataWithExtraAttributes.csv");
    }

    @Test
    public void test_missing_attribute_import(){
        OpenRocketImporter importer = new OpenRocketImporter();
        assertThrows(IllegalArgumentException.class, ()-> importer.importData("src/test/resources/RocketDataMissingAttributes.csv"));
    }

    /**
     * Ensures all the data within the file is parsed and stored
     */
    @Test
    public void test_import_data_sizes(){
        testRocketDataSize("src/test/resources/FullyCorrectRocketData.csv", 10, 172);
        testRocketDataSize("src/test/resources/RocketDataWithExtraAttributes.csv", 10, 168);
    }

    /**
     * Ensures all the data within the file is parsed correctly
     */
    @Test
    public void test_import_data_values(){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData("src/test/resources/FullyCorrectTestData.csv");
        List<RocketData> data = importer.getData();

        testRocketEvent(data.get(0), 0.1, RocketEvent.EventType.LAUNCH);
        testRocketStatus(data.get(1), 0.11, 1.2, 1.3, 1.4,1.5, 1.6, 1.7);
        testRocketEvent(data.get(2), 0.2, RocketEvent.EventType.LIFTOFF);
        testRocketStatus(data.get(3), 0.22, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7);
        testRocketEvent(data.get(4), 0.3, RocketEvent.EventType.LAUNCHROD);
        testRocketStatus(data.get(5), 0.33, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7);

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
        importer.importData("src/test/resources/FullyCorrectTestData.csv");
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

    public void testRocketDataSize(String file, int eventSize, int statusSize){
        OpenRocketImporter importer = new OpenRocketImporter();
        importer.importData(file);
        assertEquals(eventSize + statusSize, importer.getData().size());

        int numOfEvents = (int) importer.getData().stream()
                .filter(rocketData -> rocketData instanceof RocketEvent).count();

        int numOfStatuses = (int) importer.getData().stream()
                .filter(rocketData -> rocketData instanceof RocketStatus).count();

        assertEquals(statusSize, numOfStatuses);
        assertEquals(eventSize, numOfEvents);
    }
}

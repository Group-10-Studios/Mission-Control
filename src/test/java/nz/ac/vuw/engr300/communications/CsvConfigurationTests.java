package nz.ac.vuw.engr300.communications;

import nz.ac.vuw.engr300.communications.importers.CsvConfiguration;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests to verify that CsvConfiguration and CsvTableDefinition work as expected.
 *
 * @author Nathan Duckett
 */
public class CsvConfigurationTests {

    /**
     * Pre-configuration step for the test to ensure it loads test data.
     */
    @BeforeEach
    public void setupConfig() {
        try {
            CsvConfiguration.getInstance().loadNewConfig("src/test/resources/TestCommunications.json");
        } catch (FileNotFoundException e) {
            fail("Could not find src/test/resources/TestCommunications.json to execute CsvConfiguration tests");
        }
    }

    /**
     * Test that the config loads correctly.
     */
    @Test
    public void test_loadConfig() {
        // Must be updated to the number of configurations within the TestCommunications.json file.
        assertEquals(4, CsvConfiguration.getInstance().getNumberOfTables());
    }

    /**
     * Test that the config loads correctly.
     */
    @Test
    public void test_loadInvalidConfig() {
        try {
            CsvConfiguration.getInstance().loadNewConfig("src/test/resources/Non_Existent_TestCommunications.json");
            fail("Didn't throw a file not found when trying to load a non-existent configuration");
        } catch (FileNotFoundException e) {
            // All good
        }
    }

    /**
     * Test that the table contents match what was defined within the configuration file.
     */
    @Test
    public void test_TableContentsMatch() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");
        assertEquals(2, table.getTitles().size());
        assertEquals("\t", table.getDelimiter());

        System.out.println(table.getColumn("time"));

        assertNotNull(table.getColumn("time"));
        assertNotNull(table.getColumn("angle"));
    }


    /**
     * Test adding a row to the table and that the returning data matches as expected.
     */
    @Test
    public void test_AddGetValueInType() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");
        String timestamp = "13/08/2020";
        String angle = "13.1";
        table.addRow(timestamp + "\t" + angle);

        List<Object> contents = table.getRow(0);
        assertEquals(timestamp, contents.get(0).toString());
        assertEquals(angle, contents.get(1).toString());

        String timeValue = table.matchValueToColumn(contents.get(0), "time", String.class);
        Double angleValue = table.matchValueToColumn(contents.get(1), "angle", Double.class);

        assertEquals(timestamp, timeValue);
        assertEquals(13.1d, angleValue);
    }

    /**
     * Test adding a row to the table and that the data is as expected. Test trying to cast to an invalid class
     * and that it throws an error.
     */
    @Test
    public void test_AddGetValueInInvalidType() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");
        String timestamp = "13/08/2020";
        String angle = "13.1";
        table.addRow(timestamp + "\t" + angle);

        List<Object> contents = table.getRow(0);
        assertEquals(timestamp, contents.get(0).toString());
        assertEquals(angle, contents.get(1).toString());

        try {
            Float timeValue = table.matchValueToColumn(contents.get(0), "time", Float.class);
            fail("Did not throw error on invalid type cast based on column");
        } catch (Error e) {
            assert (true);
        }
    }

    /**
     * Test when retrieving latest data with no data present it will return null.
     */
    @Test
    public void test_GetLatestWithNoData() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");

        List<Object> latestData = table.latestData();
        Object latestTimestamp = table.latestData("timestamp");

        assertNull(latestData);
        assertNull(latestTimestamp);
    }

    /**
     * Test getting an invalid column index. Using an invalid column name ensure it returns null, and -1 when
     * requesting its' position instead of throwing an error.
     */
    @Test
    public void test_GetInvalidColumnIndex() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");

        assertNull(table.getColumn("Some_Invalid_Column_Name"));
        assertEquals(-1, table.getCsvIndexOf("Some_Invalid_Column_Name"));
    }

    /**
     * Test that matching a value to an invalid column will return null.
     */
    @Test
    public void test_MatchAgainstInvalidColumn() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");
        table.addRow("1\t1");
        List<Object> latestData = table.latestData();

        // Using valid column settings with incorrect name
        assertNull(table.matchValueToColumn(latestData.get(0), "Some_Invalid_Column_Name", String.class));
    }

    /**
     * Test that when adding an invalid CSV string it will return false on addition.
     */
    @Test
    public void test_AddInvalidCSV() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("simulation-outgoing");
        // Invalid CSV for this table
        boolean addResponse = table.addRow("1\t\t1");

        assertFalse(addResponse);
    }

    /**
     * Test all different type checking values from csv-tests csv definition.
     */
    @Test
    public void test_ExpandedTypeChecking() {
        CsvTableDefinition table = CsvConfiguration.getInstance().getTable("csv-tests");

        // Bad code but manually define values for each column in table to check values and type checking.
        long timestamp = 1597975831;
        long longValue = 120;
        double doubleValue = 1.5d;
        float floatValue = 1.2f;
        int intValue = 2;
        String stringValue = "I am a test";
        double angleValue = 22.5;
        double velValue = 13.0;
        double accValue = 5.2;
        double altitudeValue = 130.0;

        String csv = constructCsvString(",", timestamp, longValue, doubleValue, floatValue, intValue,
                stringValue, angleValue, velValue, accValue, altitudeValue);

        table.addRow(csv);
        assertEquals(1, table.size());

        // Check all values in the table match.
        List<Object> contents = table.latestData();
        assertEquals(10, contents.size());
        // Note must be against their class value not primitive classes.
        assertEquals(timestamp, getValue(table, "time", Long.class));
        assertEquals(longValue, getValue(table, "longValue", Long.class));
        assertEquals(doubleValue, getValue(table, "doubleValue", Double.class));
        assertEquals(floatValue, getValue(table, "floatValue", Float.class));
        assertEquals(intValue, getValue(table, "intValue", Integer.class));
        assertEquals(stringValue, getValue(table, "stringValue", String.class));
        assertEquals(angleValue, getValue(table,"angleValue", Double.class));
        assertEquals(velValue, getValue(table, "velValue", Double.class));
        assertEquals(accValue, getValue(table, "accValue", Double.class));
        assertEquals(altitudeValue, getValue(table, "altitudeValue", Double.class));
    }

    /**
     * Helper function to construct a CSV string from an array of values.
     *
     * @param delimiter Delimiter to combine values together in CSV format.
     * @param values Array of values to be added into the CSV row.
     * @return String combined from the values in a CSV format.
     */
    private String constructCsvString(String delimiter, Object...values) {
        return Stream.of(values).map(Object::toString).reduce("", (a, b) -> {
            // Check its' not the first value to prevent preceding comma.
            if ("".equals(a)) {
                return b;
            } else {
                return a + delimiter + b;
            }
        });
    }

    /**
     * Helper function get the latest value from the table for the specified column and cast its' type.
     *
     * @param table Table definition to retrieve expected column types for conversion.
     * @param columnName Name of the column we want to retrieve the data of.
     * @param type The type of object we expect the column to be casted to.
     * @param <T> The type of object this will be casted to, expects to be the same as the type of class set in type.
     * @return The value casted to the provided type from the contents list.
     */
    private <T> T getValue(CsvTableDefinition table, String columnName, Class<T> type) {
        return table.matchValueToColumn(table.latestData(columnName), columnName, type);
    }
}

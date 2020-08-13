package nz.ac.vuw.engr300.communications;

import nz.ac.vuw.engr300.communications.importers.CsvConfiguration;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        CsvConfiguration.getInstance().loadNewConfig("src/test/resources/TestCommunications.json");
    }

    /**
     * Test that the config loads correctly.
     */
    @Test
    public void test_loadConfig() {
        assertEquals(1, CsvConfiguration.getInstance().getNumberOfTables());
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
}

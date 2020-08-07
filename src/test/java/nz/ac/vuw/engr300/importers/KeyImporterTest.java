package nz.ac.vuw.engr300.importers;

import nz.ac.vuw.engr300.exceptions.KeyNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyImporterTest {

    @Test
    public void test_getAPIKeyForWeather() throws KeyNotFoundException {
        String actual = KeyImporter.getKey("weather");
        String expected = "cc5a7a7ec97d0a02e950717c0e632548";
        assertEquals(expected, actual);

    }

}

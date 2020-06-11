package nz.ac.vuw.engr300.importers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import nz.ac.vuw.engr300.App;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Static access class to load JSON data into the application. Access this using
 * a static method 'load' to import data.
 * 
 * @author Nathan Duckett
 *
 */
public class JsonImporter {

    private static final Logger LOGGER = Logger.getLogger(JsonImporter.class);

    /**
     * Load the specified JSON file into the application.
     * 
     * @param fileName String filename to import the JSON data from.
     * @return JsonElement with the contents of the file in Java JSON format.
     * @throws FileNotFoundException Exception can be thrown when the file can't be
     *                               found.
     */
    public static JsonElement load(String fileName) throws FileNotFoundException {
        try (FileReader fr = new FileReader(new File(fileName), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(fr);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            LOGGER.error("IOException caught!", e);
        }

        return null;
    }
}

package nz.ac.vuw.engr300.importers;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Exports an object to a JSON file.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class JsonExporter {

    private static final Logger LOGGER = Logger.getLogger(JsonExporter.class);

    /**
     * Saves the object to the specified filepath.
     *
     * @param fileName the path and filename for the json object.
     * @param obj the Object to save.
     */
    public static void save(String fileName, Object obj) {
        try (FileWriter fw = new FileWriter(new File(fileName), StandardCharsets.UTF_8)) {
            Gson g = new Gson();
            fw.write(g.toJson(obj));
        } catch (IOException e) {
            LOGGER.error("IOException caught!", e);
        }
    }
}

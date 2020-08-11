package nz.ac.vuw.engr300.importers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonExporter {

    private static final Logger LOGGER = Logger.getLogger(JsonExporter.class);

    public static void save(String fileName, Object obj) {
        try (FileWriter fw = new FileWriter(new File(fileName), StandardCharsets.UTF_8)) {
            Gson g = new Gson();
            fw.write(g.toJson(obj));
        } catch (IOException e) {
            LOGGER.error("IOException caught!", e);
        }
    }
}

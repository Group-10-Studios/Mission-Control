package nz.ac.vuw.engr300.communications.importers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.importers.JsonImporter;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Management class to handle our CSV configurations and loading in different setups for each
 * communication channel with other services.
 *
 * @author Nathan Duckett
 */
public class CsvConfiguration {
    private static final Logger LOGGER = Logger.getLogger(CsvConfiguration.class);
    private static final CsvConfiguration instance = new CsvConfiguration();

    /**
     * Define the default column separator. This can be overridden within the config
     */
    private static final String COL_SEP = ",";

    /**
     * Define the default row separator. This can be overriden within the config.
     */
    private static final String ROW_SEP = "\n";

    private final Map<String, CsvTableDefinition> csvMappings;


    private CsvConfiguration() {
        csvMappings = new HashMap<>();
        try {
            loadNewConfig("src/main/resources/config/communications.json");
        } catch (FileNotFoundException e) {
            System.err.println("CsvConfiguration not found, launching with no communication support");
        }
    }

    /**
     * Load a new configuration file to define the available tables.
     *
     * @param configFileName String filepath to the configuration file to load into the application.
     * @throws FileNotFoundException If the provided configuration file does not exist.
     */
    public void loadNewConfig(String configFileName) throws FileNotFoundException {
        clearAllTables();
        try {
            JsonElement contents = JsonImporter.load(configFileName);
            assert contents != null;
            JsonObject jsonObject = Objects.requireNonNull(JsonImporter.load(configFileName)).getAsJsonObject();
            for (String key : jsonObject.keySet()) {
                JsonObject mapObject = jsonObject.getAsJsonObject(key);

                String sep = COL_SEP;
                if (mapObject.has("col_split")) {
                    sep = mapObject.get("col_split").getAsString();
                }
                CsvTableDefinition table = new CsvTableDefinition(mapObject.get("headers"), sep);

                csvMappings.put(key, table);
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn("CsvConfiguration not found, launching with no communication support", e);
            throw e;
        }
    }

    /**
     * Get the instance of the CsvConfiguration.
     * @return The instance for the application for CsvConfiguration.
     */
    public static CsvConfiguration getInstance() {
        return instance;
    }

    /**
     * Get the total number of tables within the application.
     *
     * @return int count of the number of tables.
     */
    public int getNumberOfTables() {
        return this.csvMappings.size();
    }

    /**
     * Get a specific table by its' name.
     *
     * @param tableName Table name to retrieve.
     * @return CsvTableDefinition of the table if it exists.
     */
    public CsvTableDefinition getTable(String tableName) {
        return this.csvMappings.get(tableName);
    }

    /**
     * Private helper method to clear all tables from the instance.
     */
    private void clearAllTables() {
        this.csvMappings.clear();
    }
}

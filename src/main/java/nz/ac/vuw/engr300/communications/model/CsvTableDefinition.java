package nz.ac.vuw.engr300.communications.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines a CSV table definition containing all of the defined headers and contents which have been loaded
 * into the application so far.
 *
 * @author Nathan Duckett
 */
public class CsvTableDefinition {
    private static final Logger LOGGER = Logger.getLogger(CsvTableDefinition.class);

    private final String columnSeparator;

    private final List<Column> headerMapping;
    private final List<List<Object>> contentRows;

    /**
     * Create a new Table definition from the provided JSON object mapping provided (as defined in the config)
     * and with the set column separator for this instance.
     *
     * @param mapping JsonObject extracted from the config containing a mapping of headings to data types.
     * @param columnSeparator String column separator to delimit this CSV inputted.
     */
    public CsvTableDefinition(JsonElement mapping, String columnSeparator) {
        this.headerMapping = new ArrayList<>();
        this.contentRows = new ArrayList<>();
        processHeadings(mapping);

        this.columnSeparator = columnSeparator;
    }

    /**
     * Process the incoming headings from the mapping JSON definition.
     *
     * @param mapping JsonObject extracted from the config containing a mapping of headings to data types.
     */
    private void processHeadings(JsonElement mapping) {
        JsonObject headerObject = mapping.getAsJsonObject();

        for (String headerKey : headerObject.keySet()) {
            // Map each key/value into a column object
            headerMapping.add(new Column(headerKey, headerObject.get(headerKey).getAsString()));
        }

    }

    /**
     * Get the index of the provided property to retrieve from the CSV.
     *
     * @param columnName Attribute to find the position of.
     * @return Integer representing the position of the attribute in the CSV columns.
     */
    public int getCsvIndexOf(String columnName) {
        for (int i = 0; i < headerMapping.size(); i++) {
            if (headerMapping.get(i).name.equals(columnName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Get a list of the titles/headings for this CSV settings instance.
     *
     * @return List of strings representing the CSV column titles.
     */
    public List<String> getTitles() {
        return headerMapping.stream().map(h -> h.name).collect(Collectors.toList());
    }

    /**
     * Add a row into this CSV table.
     *
     * @param rawRowCsv Raw string contents to be parsed into this CSV table.
     * @return Boolean indicating successful addition to the table.
     */
    public boolean addRow(String rawRowCsv) {
        String[] contents = rawRowCsv.split(this.columnSeparator);

        if (contents.length != headerMapping.size()) {
            LOGGER.error("CSV content length doesn't match header mappings");
            return false;
        }

        // Convert to List of Objects
        List<Object> contentObjects = new ArrayList<>();

        for (int i = 0; i < contents.length; i++) {
            Object parsedObject = parseObject(contents[i], i);
            contentObjects.add(parsedObject);
        }

        return contentRows.add(contentObjects);
    }

    /**
     * Parse the content into the appropriate object based on the header definition.
     *
     * @param content String content extracted from the CSV to be set.
     * @param index Index of the column to match this content against.
     * @return Formatted object containing the value from the content.
     */
    private Object parseObject(String content, int index) {
        switch (headerMapping.get(index).type) {
            case "double": {
                return Double.parseDouble(content);
            }
            case "float": {
                return Float.parseFloat(content);
            }
            case "int": {
                return Integer.parseInt(content);
            }
            default:
                return content;
        }
    }

    /**
     * Get a list of objects representing the contents within a row specified.
     *
     * @param rowIndex Index of the row to retrieve data from.
     * @return List of objects stored in this row.
     */
    public List<Object> getRow(int rowIndex) {
        return this.contentRows.get(rowIndex);
    }

    /**
     * Get a column instance containing its' name, and type.
     *
     * @param columnName Name of the column to be retrieved.
     * @return Internal column class representing the column if it exists, otherwise null.
     */
    public Column getColumn(String columnName) {
        for (Column column : headerMapping) {
            if (column.getName().equals(columnName)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Get the current delimiter this table uses.
     *
     * @return String delimiter used to parse incoming CSV.
     */
    public String getDelimiter() {
        return this.columnSeparator;
    }

    /**
     * Match a value to the column and cast the object. This is designed to be a safe method to cast the
     * returning objects to their corresponding types without causing too many difficulties.
     *
     * @param rawObject Raw Object extracted from the list of objects retrieved for the row.
     * @param columnName Name of the column the value belongs to.
     * @param typeCast The expected class this should be typed to.
     * @param <T> The type of the class e.g. Float,String which casts the object.
     * @return The object casted to the type provided.
     */
    public <T> T matchValueToColumn(Object rawObject, String columnName, Class<T> typeCast) {
        for (Column column : headerMapping) {
            if (column.getName().equals(columnName)) {
                if (column.getCorrespondingClass().equals(typeCast)) {
                    return typeCast.cast(rawObject);
                } else {
                    LOGGER.error("Invalid cast type to " + typeCast.toString()
                            + " when this column supports " + column.type);
                    throw new Error("Invalid type cast when matching CSV value to a column. Can't cast from <"
                            + column.type + "> to <" + typeCast.toString() + ">");
                }

            }
        }
        return null;
    }

    /**
     * Defines a column within the CSV including a readable name, and a data type.
     *
     * @author Nathan Duckett
     */
    public static class Column {
        private final String name;
        private final String type;

        /**
         * Create a new column.
         *
         * @param name String friendly name of the column
         * @param type String data type stored within this column
         */
        public Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Get the name of this column.
         * @return Friendly name to define this column.
         */
        public String getName() {
            return name;
        }

        /**
         * Get the corresponding class which refers to the datatype stored within this column.
         * @return Class which the data should be a type of in this column.
         */
        public Class<?> getCorrespondingClass() {
            switch (type) {
                case "string": {
                    return String.class;
                }
                case "double": {
                    return Double.class;
                }
                case "float": {
                    return Float.class;
                }
                case "int": {
                    return Integer.class;
                }
                default:
                    return Object.class;
            }
        }
    }
}

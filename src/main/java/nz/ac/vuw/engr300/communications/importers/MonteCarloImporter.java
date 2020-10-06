package nz.ac.vuw.engr300.communications.importers;

import jdk.jshell.spi.ExecutionControl;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.communications.model.RocketData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Imports Monte Carlo simulations.
 *
 * @author Tim Salisbury
 * @author Joshua Harwood
 */
public class MonteCarloImporter implements RocketDataImporter<List<Object>> {

    private List<Consumer<List<Object>>> observers = new ArrayList<>();

    private CsvTableDefinition table;

    public MonteCarloImporter() {
        table = CsvConfiguration.getInstance().getTable("monte-carlo");
    }

    /**
     * Imports the monte carlo simulation at the given file path.
     *
     * @param filepath  The file path to import MOnte Carlo simulation from.
     */
    public void importData(String filepath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath, StandardCharsets.UTF_8));
            reader.readLine();

            String r = "";
            while ((r = reader.readLine()) != null) {
                if (!table.addRow(r)) {
                    throw new RuntimeException("Failed to add row from Monte Carlo");
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Monte Carlo simulation not found!", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }

    }

    @Override
    public void subscribeObserver(Consumer<List<Object>> observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeObserver(Consumer<List<Object>> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void unsubscribeAllObservers() {
        this.observers.clear();
    }

    public CsvTableDefinition getTable() {
        return table;
    }
}

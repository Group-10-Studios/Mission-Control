package nz.ac.vuw.engr300.communications.importers;

import jdk.jshell.spi.ExecutionControl;
import nz.ac.vuw.engr300.communications.model.CsvTableDefinition;
import nz.ac.vuw.engr300.communications.model.RocketData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MonteCarloImporter implements RocketDataImporter<List<Object>> {

    private List<Consumer<List<Object>>> observers = new ArrayList<>();

    private CsvTableDefinition table;

    public MonteCarloImporter() {
        table = CsvConfiguration.getInstance().getTable("monte-carlo");
    }

    public void importData(String filepath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            reader.readLine();

            String r = "";
            while((r = reader.readLine()) != null) {
                table.addRow(r);

            }

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
}

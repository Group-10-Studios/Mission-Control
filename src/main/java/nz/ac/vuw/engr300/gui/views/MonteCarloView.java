package nz.ac.vuw.engr300.gui.views;

import nz.ac.vuw.engr300.communications.importers.MonteCarloImporter;

public class MonteCarloView implements View {

    private final MonteCarloImporter monteCarloImporter;


    public MonteCarloView(MonteCarloImporter monteCarloImporter) {
        this.monteCarloImporter = monteCarloImporter;
    }
}

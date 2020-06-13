package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import nz.ac.vuw.engr300.gui.model.GraphType;

public class RocketBattery extends Gauge implements RocketGraph {

    private GraphType type;

    public RocketBattery(GraphType graphType) {
        super();

        this.setGraphType(graphType);
        this.setId(graphType.getGraphID());
        this.setSkinType(SkinType.BATTERY);
    }

    @Override
    public void setGraphType(GraphType g) {
        this.type = g;
    }

    @Override
    public String getUserAgentStylesheet() {
        return Gauge.class.getResource("gauge.css").toExternalForm();
    }

    @Override
    public GraphType getGraphType() {
        return this.type;
    }

    @Override
    public void clear() {

    }
}

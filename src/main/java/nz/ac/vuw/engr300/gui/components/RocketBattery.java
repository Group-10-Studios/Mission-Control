package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import nz.ac.vuw.engr300.gui.model.GraphType;

public class RocketBattery extends Gauge implements RocketGraph {

    public RocketBattery() {
        super();
        this.setSkinType(SkinType.BATTERY);
    }

    @Override
    public void setGraphType(GraphType g) {

    }

    @Override
    public GraphType getGraphType() {
        return null;
    }

    @Override
    public void clear() {

    }
}

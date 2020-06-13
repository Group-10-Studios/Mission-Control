package nz.ac.vuw.engr300.gui.components;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import nz.ac.vuw.engr300.gui.model.GraphType;

public class RocketBattery extends Gauge implements RocketGraph {

    private GraphType type;

    public RocketBattery(GraphType graphType) {
        super();

        this.setGraphType(graphType);
        this.setId(graphType.getGraphID());
        this.setSkinType(SkinType.BATTERY);
        this.sectionsVisibleProperty();
        this.setAnimated(true);

//        this.setSections(
//                new Section(0, 20, Color.rgb(200, 0, 0, 0.8)),
//                new Section(20, 50, Color.rgb(200, 200, 0, 0.8)),
//                new Section(50, 100, Color.rgb(0, 200, 0, 0.8))
//        );
    }

    public void setBatteryLevel(double percentage) {
        this.setValue(percentage);
        if (percentage > 0 && percentage <= 20) {
            this.setBarColor(Color.rgb(200, 0, 0, 0.8));
        } else if (percentage > 20 && percentage <= 50) {
            this.setBarColor(Color.rgb(200, 200, 0, 0.8));
        } else if (percentage > 50 && percentage <= 100) {
            this.setBarColor(Color.rgb(0, 200, 0, 0.8));
        }
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

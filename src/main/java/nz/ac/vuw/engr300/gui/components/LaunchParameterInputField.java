package nz.ac.vuw.engr300.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import nz.ac.vuw.engr300.gui.util.UiUtil;

import java.lang.reflect.Field;

public class LaunchParameterInputField extends GridPane {

    public LaunchParameterInputField(Field f) {
        Label fieldLabel = new Label(formatString(f.getName()));
        if (f.getType().equals(double.class)) {
            TextField numberField = new TextField();
            numberField.textProperty().addListener((observableValue, s, t1) -> {
                if(!t1.matches("^\\d*\\.?\\d*?$")) {
                    numberField.setText(t1.replaceAll("[^\\d*\\.?]", ""));
                }
            });


            UiUtil.addPercentColumns(this, 50, 50);
            this.add(fieldLabel, 0, 0);
            this.add(numberField, 1, 0);

//            this.getChildren().addAll(fieldLabel, numberField);




//            fieldLabel.setMaxWidth(Double.MAX_VALUE);
//            numberField.setMaxWidth(Double.MAX_VALUE);
//
//            this.getChildren().add(fieldLabel);
//            this.getChildren().add(numberField);
//
//            HBox.setHgrow(fieldLabel, Priority.ALWAYS);
//            HBox.setHgrow(numberField, Priority.ALWAYS);
        }
    }

    public String formatString(String s) {
        s = s.replaceAll("([A-Z])", " $1");
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}

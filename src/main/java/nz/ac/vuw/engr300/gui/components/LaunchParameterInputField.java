package nz.ac.vuw.engr300.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.model.LaunchParameters;

import java.lang.reflect.Field;

public class LaunchParameterInputField extends GridPane {

    private TextField inputField;
    private LaunchParameters parameters;
    private Field field;

    public LaunchParameterInputField(Field f, LaunchParameters parameters) {
        this.field = f;
        this.parameters = parameters;
        Label fieldLabel = new Label(formatString(f.getName()));

        if (f.getType().equals(double.class)) {
            UiUtil.addPercentColumns(this, 50, 50);
            this.add(fieldLabel, 0, 0);
            inputField = createDoubleInputField();
            this.add(inputField, 1, 0);
            try {
                inputField.setText(String.valueOf(f.getDouble(parameters)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private TextField createDoubleInputField() {
        TextField numberField = new TextField();
        numberField.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.matches("^-?\\d*\\.?\\d*?$")) {
                numberField.setText(t1.replaceAll("[^-?\\d*\\.?]", ""));
            }
        });
        return numberField;
    }

    public String formatString(String s) {
        s = s.replaceAll("([A-Z])", " $1");
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public void saveField() {
        try {
            field.setDouble(parameters, Double.parseDouble(inputField.getText()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

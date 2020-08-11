package nz.ac.vuw.engr300.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.model.LaunchParameters;
import java.lang.reflect.Field;

/**
 * Represents a pair of input fields, including the text box and label.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class LaunchParameterInputField extends GridPane {

    private TextField inputField;
    private LaunchParameters parameters;
    private Field field;

    /**
     * Creates a LaunchParameterInputField.
     * @param field The field object from the LaunchParameters object that needs to be updated.
     * @param parameters LaunchParameters object to modify.
     */
    public LaunchParameterInputField(Field field, LaunchParameters parameters) {
        this.field = field;
        this.parameters = parameters;
        Label fieldLabel = new Label(formatString(field.getName()));
        if (field.getType().equals(double.class)) {
            UiUtil.addPercentColumns(this, 50, 50);
            this.add(fieldLabel, 0, 0);
            inputField = createDoubleInputField();
            this.add(inputField, 1, 0);
            try {
                inputField.setText(String.valueOf(field.getDouble(parameters)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates an input field for the double type, so only numbers and decimal points can be typed.
     * @return TextField object with the appropriate numbers.
     */
    private TextField createDoubleInputField() {
        TextField numberField = new TextField();
        numberField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("^-?\\d*\\.?\\d*?$")) {
                numberField.setText(t1.replaceAll("[^-?\\d*\\.?]", ""));
            }
        });
        return numberField;
    }

    /**
     * Formats a string to be appropriately titled, from camel case to title case.
     * @param str The String to format.
     * @return The newly formatted string.
     */
    public String formatString(String str) {
        str = str.replaceAll("([A-Z])", " $1");
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Save the value of the field to the LaunchParameter's object field.
     */
    public void saveField() {
        try {
            field.setDouble(parameters, Double.parseDouble(inputField.getText()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

package nz.ac.vuw.engr300.gui.components;

import com.sun.javafx.scene.control.InputField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.model.LaunchParameters;
import org.w3c.dom.Text;
import java.lang.reflect.Field;

/**
 * Represents a pair of input fields, including the text box and label.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class LaunchParameterInputField extends GridPane {

    private final Control inputField;
    private final LaunchParameters.LaunchParameter<?> parameter;
    private final Field field;
    private final Field valueField;
    private final Class<?> fieldType;

    /**
     * Creates a LaunchParameterInputField.
     *
     * @param field      The field object from the LaunchParameters object that needs to be updated.
     * @param parameters LaunchParameters object to modify.
     */
    public LaunchParameterInputField(Field field, LaunchParameters.LaunchParameter<?> parameter) {
        this.field = field;
        this.parameter = parameter;
        try {
            this.valueField = parameter.getClass().getDeclaredField("value");
            this.fieldType = parameter.getType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Invalid field passed to InputField!", e);
        }

        this.valueField.setAccessible(true);

        UiUtil.addPercentColumns(this, 50, 50);

        Label fieldLabel = new Label(formatString(field.getName()));
        this.inputField = createInputField();

        this.add(fieldLabel, 0, 0);
        this.add(inputField, 1, 0);


        setInputFieldValue();
    }

    /**
     * Sets the input field to its respective value from the parameters object.
     */
    private void setInputFieldValue() {
        try {
            if (fieldType.equals(boolean.class)) {
                ((CheckBox) inputField).setSelected(valueField.getBoolean(parameter));
            } else {
                ((TextField) inputField).setText(getValueFromField());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set input field to value.", e);
        }
    }

    /**
     * Gets the value from parameters object that this input field represents.
     *
     * @return The value from the parameters object.
     */
    private String getValueFromField() {
        try {
            return String.valueOf(valueField.get(parameter));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value from field.", e);
        }
    }

    /**
     * Creates an input field (generic as control) tailored to the type of field this input field
     * is representing.
     *
     * @return The tailored input field.
     */
    private Control createInputField() {
        switch (fieldType.getSimpleName()) {
            case "Double":
                return createDoubleInputField();
            case "Integer":
                return createIntegerInputField();
            case "Boolean":
                return new CheckBox();
            default:
                return new TextField();
        }
    }

    /**
     * Creates an input field for the double type, so only numbers and decimal points can be typed.
     *
     * @return TextField object with the appropriate numbers.
     */
    private TextField createDoubleInputField() {
        TextField numberField = new TextField();
        numberField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("^-?\\d*\\.?\\d*?$")) {
                numberField.setText(t1.replaceAll("[^-?\\d*\\.?]", ""));

                int first = t1.indexOf(".") + 1;

                String afterReplace = t1.substring(0, first)
                        + t1.substring(first).replaceAll("\\.", "");
                numberField.setText(afterReplace);

            }
        });
        return numberField;
    }

    /**
     * Creates an input field for the integer type, so only numbers can be typed.
     *
     * @return A textfield object tailored for integers.
     */
    private TextField createIntegerInputField() {
        TextField numberField = new TextField();
        numberField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("^-?\\d*")) {
                numberField.setText(t1.replaceAll("[^-?\\d*]", ""));
            }
        });
        return numberField;
    }

    /**
     * Formats a string to be appropriately titled, from camel case to title case.
     *
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
            switch (fieldType.getSimpleName()) {
                case "Double":
                    valueField.set(parameter, ((TextField) inputField).getText());
                    break;
                case "Integer":
                    valueField.setInt(parameter, Integer.parseInt(((TextField) inputField).getText()));
                    break;
                case "Boolean":
                    valueField.setBoolean(parameter, ((CheckBox) inputField).isSelected());
                    break;
                default:
                    valueField.set(parameter, ((TextField) inputField).getText());
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to save field.", e);
        }
    }
}

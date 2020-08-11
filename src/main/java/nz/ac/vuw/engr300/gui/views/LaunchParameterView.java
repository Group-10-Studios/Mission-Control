package nz.ac.vuw.engr300.gui.views;

import com.sun.javafx.scene.control.InputField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.components.LaunchParameterInputField;
import nz.ac.vuw.engr300.gui.util.UiUtil;
import nz.ac.vuw.engr300.model.LaunchParameters;

import java.lang.reflect.Field;

public class LaunchParameterView implements View {
    private final GridPane root;
    private LaunchParameters parameters;

    public LaunchParameterView(GridPane root, LaunchParameters parameters) {
        this.root = root;
        this.parameters = parameters;


    }

    private void setupInputsFields(){
        Class<LaunchParameters> clazz = LaunchParameters.class;
        Field[] fields = clazz.getDeclaredFields();
        VBox vbox = UiUtil.createMinimumVerticalSizeVBox(5, new Insets(10));
        for (Field f : fields) {
            LaunchParameterInputField lpif = new LaunchParameterInputField(f);
            vbox.getChildren().add(lpif);
        }
        root.getChildren().add(vbox);

    }



    public LaunchParameterView(GridPane root) {
        this(root, new LaunchParameters());
    }

    public static void display()
    {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("This is a pop up window");

        GridPane root = new GridPane();

        Scene scene= new Scene(root, 300, 250);

        // TODO: Load in previous launch parameters here
        LaunchParameterView l = new LaunchParameterView(root);

        popupwindow.setScene(scene);
        l.setupInputsFields();
        popupwindow.showAndWait();


    }
}

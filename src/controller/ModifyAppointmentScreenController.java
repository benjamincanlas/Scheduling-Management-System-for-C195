package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifyAppointmentScreenController {
    Stage stage;
    Parent scene;

    public TextField modApptIdTxt;
    public TextField modApptTitleTxt;
    public TextField modApptDescTxt;
    public TextField modApptLocationTxt;
    public DatePicker modApptStartDate;
    public DatePicker modApptEndDate;
    public ComboBox modApptTypeCombo;
    public ComboBox addApptContactIdCombo;
    public ComboBox modApptCustIdCombo;
    public ComboBox modApptUserIdCombo;
    public ComboBox modApptStartTimeCombo;
    public ComboBox modApptEndTimeCombo;
    public Button saveBtn;
    public Button cancelBtn;

    public void onStartDate(ActionEvent event) {
    }

    public void onEndDate(ActionEvent event) {
    }

    public void onTypeCombo(ActionEvent event) {
    }

    public void onContactIdCombo(ActionEvent event) {
    }

    public void onCustIdCombo(ActionEvent event) {
    }

    public void onUserIdCombo(ActionEvent event) {
    }

    public void onStartTime(ActionEvent event) {
    }

    public void onEndTime(ActionEvent event) {
    }

    public void onSave(ActionEvent event) {
    }

    public void onCancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}

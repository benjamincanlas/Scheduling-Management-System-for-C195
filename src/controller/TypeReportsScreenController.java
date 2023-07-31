package controller;

import DAO.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 *
 *  Class for the Type and Month reports interface showing the number appointments for each
 *
 */
public class TypeReportsScreenController implements Initializable {
    Stage stage;
    Parent scene;

    ObservableList<String> Month = FXCollections.observableList(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
    ObservableList<String> selectedType = FXCollections.observableList(Arrays.asList("Lunch", "Break", "Meeting"));


    public ToggleGroup toggleRadioGroup;
    public RadioButton radioType;
    public RadioButton radioCountry;
    public RadioButton radioContact;
    public ComboBox<String> typeReportsCombo;
    public ComboBox<String> monthReportsCombo;

    public Label numberLabelVariable;
    public Button exitButton;

    public void onRadioType(ActionEvent event) {
    }

    /**
     * This leads to the Country reports scene
     * @param event
     * @throws IOException
     */
    public void onRadioCountry(ActionEvent event) throws IOException {
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CountryReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * This leads to the Contacts reports scene
     * @param event
     * @throws IOException
     */
    public void onRadioContact(ActionEvent event) throws IOException {
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ContactsReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method will display the appointment types in the dropdown menu and is dependent on the months combo box
     * to display the number of appointments for both
     * @param event
     * @throws SQLException
     */
    public void onTypeCombo(ActionEvent event) throws SQLException {

        String selectedType = typeReportsCombo.getValue();
        String selectedMonth = monthReportsCombo.getValue();
        numberLabelVariable.setText(String.valueOf(AppointmentsDAO.getMonthFromType(selectedType, selectedMonth)));
        if (selectedMonth == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select an month");
            alert.showAndWait();
        }
    }

    /**
     * This method, in conjunction with the Type combobox, will display an array of months in the drop down combo box
     * and then show the number of appointments.
     * @param event
     * @throws SQLException
     */
    public void onMonthCombo(ActionEvent event) throws SQLException {
        String selectedType = typeReportsCombo.getValue();
        String selectedMonth = monthReportsCombo.getValue();
        if (selectedType == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select an appointment type");
            alert.showAndWait();
        }
        else {
            numberLabelVariable.setText(String.valueOf(AppointmentsDAO.getMonthFromType(selectedType, selectedMonth)));
        }
    }
    /**
     * This exits the reports and leads to the main menu.
     * @param event
     * @throws IOException
     */
    public void onExit(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This sets up and initializes the comboboxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            radioType.setSelected(true);

            typeReportsCombo.setItems(AppointmentsDAO.getAppointmentTypes());
            monthReportsCombo.setItems(Month);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}


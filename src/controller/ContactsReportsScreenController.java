package controller;

import DAO.AppointmentsDAO;
import DAO.ContactsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointments;
import model.Contacts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Class for the Contact reports interface showing the number appointments for each
 */
public class ContactsReportsScreenController implements Initializable  {
//    public Button resetButton;
    public AnchorPane contact;
    Stage stage;
    Parent scene;
    public TableView<Appointments> reportsTableView;
    public TableColumn<Appointments, Integer> apptIdCol;
    public TableColumn<Appointments, String> apptTitleCol;
    public TableColumn<Appointments, String> apptDescCol;
    public TableColumn<Appointments, String> apptTypeCol;
    public TableColumn<Appointments, LocalDateTime> apptStartCol;
    public TableColumn<Appointments, LocalDateTime> apptEndCol;
    public TableColumn<Appointments, Integer> apptCustIdCol;

    public ToggleGroup toggleRadioGroup;
    public RadioButton radioContact;
    public RadioButton radioType;

    public RadioButton radioCountry;
    public ComboBox<Contacts> contactReportCombo;
    public Button exitButton;


    /**
     * This method displays the 3 contacts on the selected drop down menu of the combo box and displays the table.
     * @param event
     * @throws SQLException
     */
    public void onContactCombo(ActionEvent event) throws SQLException {
        ObservableList<Appointments> appt = AppointmentsDAO.getAllAppointmentsViaContactID(contactReportCombo.getValue().getContactID());
        reportsTableView.setItems(appt);

    }

    /**
     * This leads to the Types Report scene
     * @param event
     * @throws IOException
     */
    public void onRadioType(ActionEvent event) throws IOException {
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/TypeReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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

    public void onRadioContact(ActionEvent event) {
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
     * This initializes the radio buttons, the combo box and will the display the appointments based on the selected contact.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            radioContact.setSelected(true);
            reportsTableView.setItems(AppointmentsDAO.getAllAppointments());
            contactReportCombo.setItems(ContactsDAO.getAllContacts());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }


}

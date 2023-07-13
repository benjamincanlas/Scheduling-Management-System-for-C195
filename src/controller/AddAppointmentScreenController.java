package controller;

import DAO.AppointmentsDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import helper.WhiteSpaceChecker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.Appointments;
import model.Customer;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class AddAppointmentScreenController implements Initializable{

    Stage stage;
    Parent scene;
    public TextField addApptIdTxt;
    public TextField addApptTitleTxt;
    public TextField addApptDescTxt;
    public TextField addApptLocationTxt;

    public ComboBox<String> addApptTypeCombo;
//    public ComboBox<Contacts> addApptContactIdCombo;
    @FXML
    private ComboBox<Customer> addApptCustIdCombo;
    public ComboBox<Users> addApptUserIdCombo;
    public Button saveBtn;
    public Button cancelBtn;

    public DatePicker addApptStartDate;
    public DatePicker addApptEndDate;
    public Spinner<Integer> spinnerStartHours;
    public Spinner<Integer> spinnerStartMinutes;
    public Spinner<Integer> spinnerEndHours;
    public Spinner<Integer> spinnerEndMinutes;

    public static ZoneId ESTzoneID = ZoneId.of("America/New_York");
    public static ZoneId UTCzoneID = ZoneId.of("UTC");
    public static ZoneId defaultZoneID = ZoneId.systemDefault();

    public void onStartDate(ActionEvent event) {
    }

    public void onEndDate(ActionEvent event) {
    }




    /**===
     *
     *
     *         Country country1 = addCustCountryCombo.getValue();
     *         addCustStateCombo.setItems(FirstLevelDivisionsDAO.getCountryDivisions(country1.getCountryID()));
     *
     *
     *         comboBoxCustomer.setItems(customerDao.getAllCustomers());
     *         comboBoxUser.setItems(userDao.getAllUsers());
     *         comboBoxContact.setItems(contactDao.getAllContacts());
     *
     *
     *
     * */



    public void setComboBoxes() throws SQLException {
        addApptTypeCombo.setItems(AppointmentsDAO.getAppointmentTypes());
//        addApptContactIdCombo.setItems(ContactsDAO.getAllContacts());
        addApptCustIdCombo.setItems(CustomerDAO.getAllCustomers());
        addApptUserIdCombo.setItems(UserDAO.getAllUsers());
    }


    public void onTypeCombo() throws SQLException {
//        addApptTypeCombo.setItems(AppointmentsDAO.getAppointmentTypes());
    }

    public void onContactIdCombo() throws SQLException {
//        addApptContactIdCombo.setItems(ContactsDAO.getAllContacts());
    }

    public void onCustIdCombo() throws SQLException {
//        addApptCustIdCombo.setItems(CustomerDAO.getAllCustomers());
    }

    public void onUserIdCombo() throws SQLException {
//        addApptUserIdCombo.setItems(UserDAO.getAllUsers());

    }

    public void onSpinners() {
        spinnerStartHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinnerStartMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        spinnerEndHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinnerEndMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        setSpinnerListener(spinnerStartHours);
        setSpinnerListener(spinnerStartMinutes);
        setSpinnerListener(spinnerEndHours);
        setSpinnerListener(spinnerEndMinutes);
    }

    public void setSpinnerListener(Spinner<Integer> spinner) {
        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d+")) {
                spinner.getEditor().setText(oldValue);
            }
        });
    }

    public boolean validateTimes(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Date/Time Error");
            alert.setHeaderText("Start must be before end");
            alert.setContentText("Please select valid start and end times");
            alert.showAndWait();

            return false;
        }
        if (start.isBefore(LocalDateTime.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Date/Time Error");
            alert.setHeaderText("Start must be in the future.");
            alert.setContentText("Please select future start time.");
            alert.showAndWait();


            return false;
        }
        if (!validateWithinOfficeHours(start)) { return false;}

        return true;
    }







    public boolean validateWithinOfficeHours(LocalDateTime start) {
        boolean isValid = true;

        ZonedDateTime startEST = convertLocalToEST(start);
        if (outOfOfficeHours(startEST)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Date/Time Error");
            alert.setHeaderText("Start must within office hours.");
            alert.setContentText("Select start time between 0800 and 2200.");
            alert.showAndWait();


            isValid = false;
        }
        return isValid;
    }
    public boolean outOfOfficeHours(ZonedDateTime time) {
        return (time.getHour() < 8 || time.getHour() > 22 || (time.getHour() == 22 && time.getMinute() > 0));
    }


     LocalDateTime getLocalDateTime(LocalDate date, int hours, int minutes) {
        return date.atTime(hours, minutes);
    }
     ZonedDateTime convertLocalToEST(LocalDateTime time) {
        return time.atZone(defaultZoneID).withZoneSameInstant(ESTzoneID);
    }

     ZonedDateTime convertLocalToUTC(LocalDateTime time) {
        return time.atZone(defaultZoneID).withZoneSameInstant(UTCzoneID);
    }


// TODO: 7/11/2023 @ 1:30: need assistance in getting AddAppontment to work, selecting userID, ContactID or customerID causes isseus



    public void onSave(ActionEvent event) throws SQLException, IOException {
        String title = addApptTitleTxt.getText();
        String description = addApptDescTxt.getText();
        String location = addApptLocationTxt.getText();
        String type = addApptTypeCombo.getSelectionModel().getSelectedItem();
//        int type = Integer.parseInt(addApptTypeCombo.getSelectionModel().getSelectedItem());
//        String createdBy = "user";
//        String lastUpdatedBy ="user";

        String typeCombo = addApptTypeCombo.getSelectionModel().getSelectedItem();

//        Contacts contactCombo = addApptContactIdCombo.getValue();

//        Customer customerCombo = addApptCustIdCombo.getSelectionModel().getSelectedItem();
//        Users userCombo = addApptUserIdCombo.getSelectionModel().getSelectedItem();




        if (WhiteSpaceChecker.validate(title) || WhiteSpaceChecker.validate(description) || WhiteSpaceChecker.validate(location))
        {
            System.out.println(" White space ");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty values must be completed.");
            alert.showAndWait();
            return;
        }
        if ( typeCombo == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: An appointment type must be selected.");
            alert.showAndWait();
            return;
        }

//        if (contactCombo == null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning Dialog");
//            alert.setContentText("ERROR: A contact ID must be selected.");
//            alert.showAndWait();
//            return;
//        }
//        if (customerCombo == null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning Dialog");
//            alert.setContentText("ERROR: A customer ID must be selected.");
//            alert.showAndWait();
//            return;
//        }
//        if (userCombo == null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning Dialog");
//            alert.setContentText("ERROR: A user ID must be selected.");
//            alert.showAndWait();
//            return;
//        }
        LocalDateTime start = getLocalDateTime(addApptStartDate.getValue(), spinnerStartHours.getValue(), spinnerStartMinutes.getValue());
        LocalDateTime end = getLocalDateTime(addApptEndDate.getValue(), spinnerEndHours.getValue(), spinnerEndMinutes.getValue());
        if (!validateTimes(start, end)) {
            return;
        }
        else {

            int customer = addApptCustIdCombo.getValue().getCustomerID();
            int user = addApptUserIdCombo.getValue().getUserID();
//            int contact = addApptContactIdCombo.getValue().getContactID();
            int contact = 1;

            Appointments newAppointment = new Appointments(1, title, description, location, type, start, end,
                    LocalDateTime.now(), Main.getUser(), LocalDateTime.now(), Main.getUser(),
                    customer, user, contact);
            AppointmentsDAO.addAppointments(newAppointment);
        }

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();


    }

    public void onCancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }




    public void initialize(URL url, ResourceBundle resourceBundle){

//        try {
//            onTypeCombo();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            onContactIdCombo();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            onCustIdCombo();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            onUserIdCombo();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        try {
            setComboBoxes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        onSpinners();


    }


}

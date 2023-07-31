package controller;

import DAO.AppointmentsDAO;
import DAO.ContactsDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import helper.WhiteSpaceChecker;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.Appointments;
import model.Contacts;
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

/**
 * Class for modifying the selected appointment.
 */
public class ModifyAppointmentScreenController implements Initializable {
    private static Appointments selectedAppointment;
    Stage stage;
    Parent scene;
    public TextField modApptIdTxt;
    public TextField modApptTitleTxt;
    public TextField modApptDescTxt;
    public TextField modApptLocationTxt;
    public ComboBox<String> modApptTypeCombo;
    public ComboBox<Contacts> modApptContactIdCombo;
    public ComboBox<Customer> modApptCustIdCombo;
    public ComboBox<Users> modApptUserIdCombo;
    public DatePicker modApptStartDate;
    public DatePicker modApptEndDate;
    public Spinner<Integer> startHrsSpin;
    public Spinner<Integer> startMinSpin;
    public Spinner<Integer> endHrsSpin;
    public Spinner<Integer> endMinSpin;
    public static ZoneId ESTzoneID = ZoneId.of("America/New_York");
    public static ZoneId UTCzoneID = ZoneId.of("UTC");
    public static ZoneId defaultZoneID = ZoneId.systemDefault();

    public Button saveBtn;
    public Button cancelBtn;

    public void onStartDate(ActionEvent event) {
    }
    public void onEndDate(ActionEvent event) {
    }
    /**
     * Method is created to get the LocalDateTime from the spinner methods of the spinners and datepicker.
     * @param date from Datepicker combo
     * @param Hrs from spinner combo
     * @param Mins from spinner combo
     * @return LocalDateTime
     */
    LocalDateTime getLDT(LocalDate date, int Hrs, int Mins) {
        return date.atTime(Hrs, Mins);
    }
    /**
     * Converts LocalDateTime to EST ZoneDateTime for our office hours 0800-2200 called in the officehours check and filters.
     * @param time
     * @return ZoneDateTime EST
     */
    ZonedDateTime ldtToESTConverter(LocalDateTime time) {
        return time.atZone(defaultZoneID).withZoneSameInstant(ESTzoneID);
    }


    /**
     * This method sets multiple combo box methods from "type, contactID, customerID, and userID" into one method. Because each
     * method combos have their own SQL DAO controller, except for type, each will have that sql expression called from their
     * respective DAO controller. Each of their model pages will also have a "toString" method to convert the ids into names
     * in the drop-down combo menu.
     * @throws SQLException
     */
    public void setMultipleComboBoxes() throws SQLException {
        modApptTypeCombo.setItems(AppointmentsDAO.getAppointmentTypes());
        modApptContactIdCombo.setItems(ContactsDAO.getAllContacts());
        modApptCustIdCombo.setItems(CustomerDAO.getAllCustomers());
        modApptUserIdCombo.setItems(UserDAO.getAllUser());
    }
    public void onTypeCombo(ActionEvent event) {
    }
    public void onContactIdCombo(ActionEvent event) {
    }
    public void onCustIdCombo(ActionEvent event) {
    }
    public void onUserIdCombo(ActionEvent event) {
    }
    /**
     * This method sets up multiple spinner methods into one from Start: Hours/Minutes and End: Hours/Minutes. For each line
     * of ValueFactory, the integers must follow arrays counting principle n-1 to display correct military times. Each spinner is
     * called with a filter from minHrSpinIntFilter method to ensure only integers are accepted.
     * Method must be called under initialize method to work properly.
     */
    public void setMultipleComboHrMinSpinner() {
        startHrsSpin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        minHrSpinIntFilter(startHrsSpin);
        startMinSpin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        minHrSpinIntFilter(startMinSpin);
        endHrsSpin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        minHrSpinIntFilter(endHrsSpin);
        endMinSpin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        minHrSpinIntFilter(endMinSpin);
    }
    /**
     * This method will filter the minute and hour time spinners to only accept integer values. If any other value is inputted,
     * specifically from the user keyboard, an alert will be triggered.
     * @param spinFilter
     */
    public void minHrSpinIntFilter(Spinner<Integer> spinFilter) {
        Spinner spinHours;
        Spinner spinMins;
        spinFilter.setEditable(true);
        spinFilter.getEditor().textProperty().addListener(((observable, StringoldValue, StringnewValue) ->
        {
            if (!StringnewValue.matches("\\d+"))
            { spinFilter.getEditor().setText(StringoldValue);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Times only accepts integers or adjustable by clicking on up and down arrows");
                alert.showAndWait();
                System.out.println("User keyboard input detected ");
                return;
            }
        }
        ));
    }

    /**
     * This boolean method expands on the outsideOfficeHrsFilter method above as it includes a trigger alert and mainly converts EST as the
     * office hour bounds are in EST.
     * @param startTime Converts to EST
     * @return If time falls outside 0800-2200 EST range then alert triggered and is false. True only if insideOffice hours
     * is true and within the interval range.
     */
    public boolean checkForValidTimesTrigger(LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        ObservableList<Appointments> overlapList = AppointmentsDAO.getAllAppointments();

        if (startTime.isAfter(endTime)) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("Start times cannot be scheduled after end time. Select valid start and end times");
            alert1.showAndWait();
            System.out.println("Check trigger for wrong time order");
            return false;
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Cannot create appointments in the past. Future start time must be created.");
            alert.showAndWait();
            System.out.println("Check trigger for wrong date order");
            return false;
        }
        if (!checkForInsideWorkHoursTrigger(startTime)) {
            return false;
        }
        return true;
    }
    /**
     * This method creates the boolean method where the interval range is specifically outside the required office hours
     * @param startTime
     * @return Time that is outside 800-2200 interval range
     */
    public boolean outsideOfOfficeHrsFilter(ZonedDateTime startTime) {
        return ((startTime.getHour() < 8 )|| (startTime.getHour() > 22) ||
                ((startTime.getHour() == 22) && (startTime.getMinute() > 0))
        );
    }
    /**
     * This boolean method expands on the outsideOfficeHrsFilter method above as it includes a trigger alert and mainly converts EST as the
     * office hour bounds are in EST.
     * @param startTime Converts to EST
     * @return If time falls outside 0800-2200 EST range then alert triggered and is false. True only if insideOffice hours
     * is true and within the interval range.
     */
    public boolean checkForInsideWorkHoursTrigger(LocalDateTime startTime) {
        boolean notOfficeHrs = ((startTime.getHour() < 8 || startTime.getHour() > 22 || //
                (startTime.getHour() == 22 && startTime.getMinute() > 0))); //
        boolean insideOfficeHrs = true;
        //must convert to EST
        ZonedDateTime startTimeConvertedToEST = ldtToESTConverter(startTime);
        if (outsideOfOfficeHrsFilter(startTimeConvertedToEST)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Time selected are not within office hours. Create appointment time between 0800 and 2200 military.");
            alert.showAndWait();
            insideOfficeHrs = false;
        }
        return insideOfficeHrs;
    }




    /**
     * This boolean method checks for any overlapping appointment times already in place by the same customer based on their ID.
     * This covers appointments with same start and end times.
     * If triggered, alert popups will display until times outside its range are chosen.
     * @param custID Customer ID being referenced and compared to previous appointments
     * @param apptID Appointment ID specific to one the being added
     * @param apptStart Appointment times being started
     * @param apptEnd Appointment times ended
     * @return True only when overlaps are found
     * @throws Exception
     */
    public boolean checkForOverlappingTrigger(int custID, int apptID, LocalDateTime apptStart, LocalDateTime apptEnd) throws Exception {
        ObservableList<Appointments> oList = AppointmentsDAO.getAllAppointments();
        LocalDateTime checForkApptStartTimes = getLDT(modApptStartDate.getValue(), startHrsSpin.getValue(), startMinSpin.getValue());
        LocalDateTime checkForApptEndTimes = getLDT(modApptEndDate.getValue(), endHrsSpin.getValue(), endMinSpin.getValue());
        //borrowed from Addappt should also work

        for (Appointments o : oList) {
            checForkApptStartTimes = o.getStart();
            checkForApptEndTimes = o.getEnd();
            if (custID != o.getCustomerID()) {
                continue;
            }
            if (apptID == o.getAppointmentID()) {
            }
        }
        if (checForkApptStartTimes.isEqual(apptStart) || checForkApptStartTimes.isEqual(apptEnd) || checkForApptEndTimes.isEqual(apptStart) || checkForApptEndTimes.isEqual(apptEnd)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("The appointment time you are trying to schedule cannot begin or end concurrently with another existing appointment");
            alert.showAndWait();
            return true;
        }
        else if (((apptStart.isAfter(checForkApptStartTimes) && (apptStart.isBefore(checkForApptEndTimes))) ||
                ((apptEnd.isAfter(checForkApptStartTimes) && (apptEnd.isBefore(checkForApptEndTimes))))) ||
                ((apptStart.isBefore(checForkApptStartTimes)) && (apptEnd.isAfter(checkForApptEndTimes)))  ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("The appointment time you are trying to schedule cannot overlap with another existing appointment");
            alert.showAndWait();
            return true; }
        return false;
    }

    /**
     * This method modifies and saves the selected appointment from the Appointment table so long all parameters are met. This save
     * method calls other methods and even other class to ensure appropriate fields are entered. Once met, this lead us back
     * the main menu.
     * @param event
     * @throws Exception
     */
    public void onSave(ActionEvent event) throws Exception {
        String title = modApptTitleTxt.getText();
        String description = modApptDescTxt.getText();
        String location = modApptLocationTxt.getText();
        String type = modApptTypeCombo.getSelectionModel().getSelectedItem();
//        int type = Integer.parseInt(addApptTypeCombo.getSelectionModel().getSelectedItem());
//        String createdBy = "user";
//        String lastUpdatedBy ="user";

        String typeCombo = modApptTypeCombo.getSelectionModel().getSelectedItem();

        Contacts contactCombo = modApptContactIdCombo.getSelectionModel().getSelectedItem();

        Customer customerCombo = modApptCustIdCombo.getSelectionModel().getSelectedItem();
        Users userCombo = modApptUserIdCombo.getSelectionModel().getSelectedItem();


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
            alert.setContentText("An appointment type must be selected.");
            alert.showAndWait();
            return;
        }

        if (contactCombo == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(" A contact ID must be selected.");
            alert.showAndWait();
            return;
        }
        if (customerCombo == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("A customer ID must be selected.");
            alert.showAndWait();
            return;
        }
        if (userCombo == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("A user ID must be selected.");
            alert.showAndWait();
            return;
        }
        LocalDateTime start = getLDT(modApptStartDate.getValue(), startHrsSpin.getValue(), startMinSpin.getValue());
        LocalDateTime end = getLDT(modApptEndDate.getValue(), endHrsSpin.getValue(), endMinSpin.getValue());
        if (!checkForValidTimesTrigger(start, end)) {
            return;
        }

            int customer = modApptCustIdCombo.getValue().getCustomerID();
            int user = modApptUserIdCombo.getValue().getUserID();
            int contact = modApptContactIdCombo.getValue().getContactID();
            if (checkForOverlappingTrigger(customer, 1, start, end)) {
                return;
            }

            Appointments updatedAppointment = new Appointments(selectedAppointment.getAppointmentID(), title, description, location, type, start, end,
                    selectedAppointment.getCreateDate(), selectedAppointment.getCreatedBy(), LocalDateTime.now(), Main.getUser(),
                    customer, user, contact);
            AppointmentsDAO.updateAppointments(updatedAppointment);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * This method exits the current controller file and back into the main menu screen without any info saved.
     * @param event
     * @throws IOException
     */
    public void onCancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method receives the selected appointment into a variable to used for receiving the appointment info.
     * @param appointment the appointment selected to be modified
     */
    public static void receiveSelectedAppointment(Appointments appointment) {
        selectedAppointment = appointment;
    }

    /**
     * This method populates the received appointment info into the textfields  as well as for initializing the multiple combobox
     * methods called.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

        modApptIdTxt.setText(((String.valueOf(selectedAppointment.getAppointmentID()))));
        modApptTitleTxt.setText(selectedAppointment.getTitle());
        modApptDescTxt.setText(selectedAppointment.getDescription());
        modApptLocationTxt.setText(selectedAppointment.getLocation());
        modApptTypeCombo.setValue(selectedAppointment.getType());

        try {
            modApptCustIdCombo.setValue(CustomerDAO.getCustomer(selectedAppointment.getCustomerID()));
            modApptUserIdCombo.setValue(UserDAO.getUserBasedOnID(selectedAppointment.getUserID()));
            modApptContactIdCombo.setValue(ContactsDAO.getContact(selectedAppointment.getContactID()));
            setMultipleComboBoxes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//Put methods such as onSpinners() before lines of spinners for them to work properly. Order actually matters here!!!!
        setMultipleComboHrMinSpinner();
        modApptStartDate.setValue(selectedAppointment.getStart().toLocalDate());
        modApptEndDate.setValue(selectedAppointment.getEnd().toLocalDate());
        startHrsSpin.getValueFactory().setValue(selectedAppointment.getStart().getHour());
        startMinSpin.getValueFactory().setValue(selectedAppointment.getStart().getMinute());
        endHrsSpin.getValueFactory().setValue(selectedAppointment.getEnd().getHour());
        endMinSpin.getValueFactory().setValue(selectedAppointment.getEnd().getMinute());




    }









    }

package controller;

import DAO.AppointmentsDAO;
import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionsDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Country;
import model.Customer;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main menu of the scheduler
 */
public class SchedulerScreenController implements Initializable {



    private static int modAppointment;
    private static int modCustomer;
    public static int getModCustomer() {
        return modCustomer;
    }
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList();
    public Button apptAddBtn;
    public Button apptModBtn;
    public Button apptDelBtn;
    public ToggleGroup viewByToggle;
    public RadioButton weekViewRadioBtn;
    public RadioButton monthViewRadioBtn;
    public RadioButton allViewRadioBtn;
    public TableView<Appointments> appointmentTable;
    public TableColumn<Appointments, Integer> apptIdCol;
    public TableColumn<Appointments, String> apptTitleCol;
    public TableColumn<Appointments, String> apptDescCol;
    public TableColumn<Appointments, String> apptLocationCol;
    public TableColumn<Appointments, String> apptTypeCol;
    public TableColumn<Appointments, LocalDateTime>  apptStartCol;
    public TableColumn<Appointments, LocalDateTime>  apptEndCol;
    public TableColumn<Appointments, String> apptCustIdCol;
    public TableColumn<Appointments, String> apptUserIdCol;
    public TableColumn<Appointments, String> apptContactCol;
    public Button custAddBtn;
    public Button custModBtn;
    public Button custDelBtn;
    public Button logOutBtn;
    public Button reportsBtn;
    public TableView<Customer> customerTable;
    public TableColumn<Customer, Integer> custIdCol;
    public TableColumn<Customer, String> custNameCol;
    public TableColumn<Customer, String> custAddressCol;
    public TableColumn<Customer, Integer> custZipCol;
    public TableColumn<Customer, Integer> custPhoneCol;
    public TableColumn<Customer, FirstLevelDivisions> custStateCol;
    public TableColumn<Customer, Country> custCountryCol;


    /** Both the "stage" and "parent" scenes transfer us from our current FXML scene to a new load scene that we set.  */
    Stage stage;
    Parent scene;

    @Override

    /**
     * This will initialize and set data placement for the appointments and customers table.
     * The sample and test data from the main Scheduler Screen menu will be retrieved and preloaded into this scene's fields.
     * 2 Lambda expressions: A for contactIDs and B for establishing the Country and FirstLevelDiv combo boxes
     *
     *
     *  Lambda Expression A (line 124- 129)
     *      Gets ContactName from ContactID ensuring that it will display the name rather just the number on the appointments table.
     *
     *  Lambda Expression B (line 148 - 157)
     *       Lambda usage for getting State/Province from DivisionID which ensures that the State or Province is displayed rather than just the number.
     *       This also retrieves info from the State/Province which leads to which Country it is in. Thus First Level divisions
     *       has a dependency dependant the country selected.
     *
     *
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allViewRadioBtn.setSelected(true);
        //Appointments Table
        try {
            appointmentTable.setItems(AppointmentsDAO.getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
//        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
//todo make lambda for appt
        /**Lambda A
         * */
        apptContactCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getContactName());
            } catch (SQLException | RuntimeException ignored) {
            }
            return null;
        });

        //Customers Table
        try {
            customerTable.setItems(CustomerDAO.getAllCustomers());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custZipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        custStateCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        /**
         *Lambda Expression B
         */
        custStateCol.setCellValueFactory(CellData ->
        {
            ObservableValue<FirstLevelDivisions> result = new SimpleObjectProperty(FirstLevelDivisionsDAO.getDivision(CellData.getValue().getDivisionID()));
            return result;
        });
        custCountryCol.setCellValueFactory(CellData ->
        {
            FirstLevelDivisions div = FirstLevelDivisionsDAO.getDivision(CellData.getValue().getDivisionID());
            ObservableValue<Country> result = new SimpleObjectProperty(CountryDAO.getCountry(div.getCountryID()));
            return result;
        });
//        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
    }


    /**
     * This leads to new scene to open the Add Appointments.
     * @param event
     * @throws IOException
     */
    public void onAddAppt(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This leads to modify appointments scene as long a selection is made.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void onModifyAppt(ActionEvent event) throws IOException, SQLException {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        modAppointment = AppointmentsDAO.getAllAppointments().indexOf(selectedAppointment);
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        } else {
            ModifyAppointmentScreenController.receiveSelectedAppointment(appointmentTable.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointmentScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This scene will delete the selected appointment along with confirmation alerts.
     * @param event
     * @throws SQLException
     */
    public void onDeleteAppt(ActionEvent event) throws SQLException {
        if (appointmentTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an appointment for deletion.");
            alert.showAndWait();
        } else {
            Appointments appt = appointmentTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Press ok to continue deleting " +
                    "Appointment ID:" + appt.getAppointmentID() +" " + appt.getTitle() + " " + appt.getType() +" appointment.");
            Optional<ButtonType> result = alert.showAndWait();

            Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (result.isPresent() && result.get() == ButtonType.OK) {
//                selectedAppointment  = appointmentTable.getSelectionModel().getSelectedItem();
//                CustomerDAO.deleteCustomer(selectedCustomer);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Appointment "+ appt.getAppointmentID() + " " +
                        "" + appt.getTitle() + " " + appt.getType() +" has been deleted from the schedule.");
                Optional<ButtonType> result1 = alert1.showAndWait();

                AppointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentID());
                appointmentTable.setItems(AppointmentsDAO.getAllAppointments());
            }


            /**==============================FIX THIS BY CREATING DAO FOR APPT!!!!!!    =================*/
//            AppointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentID());
//            appointmentTable.setItems(AppointmentsDAO.getAllAppointments());


            /**=================================*/


        }
    }


    /**
     * This filters the table based on a sql expression filtering 7 days from the current date.
     * @param event
     * @throws SQLException
     */
    public void onWeekViewRadio(ActionEvent event) throws SQLException {
        appointmentTable.setItems(AppointmentsDAO.filterAppointmentsViaWeek(LocalDate.now()));
    }

    /**
     * This method filters appointments a month or 30 days from Local date.
     * @param event
     * @throws SQLException
     */
    public void onMonthViewRadio(ActionEvent event) throws SQLException {
        appointmentTable.setItems(AppointmentsDAO.filterAppointmentsViaMonth(LocalDate.now()));
    }

    /**
     * This method is the default table view and shows all scheduled appointments.
     * @param event
     * @throws SQLException
     */
    public void onAllViewRadio(ActionEvent event) throws SQLException {
        appointmentTable.setItems(AppointmentsDAO.getAllAppointments());

    }





//Customer methods
    /**
     *Leads to add customer scene.
     * @param event
     * @throws IOException
     */
    public void onAddCust(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method leads to the modify customer page with the selected customer.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void onModifyCust(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        modCustomer = CustomerDAO.getAllCustomers().indexOf(selectedCustomer);
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
        }
        else {
            ModifyCustomerScreenController.receiveSelectedCustomer(customerTable.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomerScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     *This leads to a customer a selected customer being deleted. An alert will trigger letting the use know the associated
     * appointments, if any, will also be deleted. A sql expression from the customer sql DAO will base this on their ID and
     * appointment ID.
     * @param event
     * @throws SQLException
     */
    public void onDeleteCust(ActionEvent event) throws SQLException {
        if (customerTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select a customer for deletion.");
            alert.showAndWait();

        } else {
            Customer custName = customerTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer " + custName.getCustomerName() + " and " +
                    "their associated appointments will be deleted. Press ok to proceed with deleting " + custName.getCustomerName() +
                    " from the schedule.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Appointments> deleteList = AppointmentsDAO.getAllAppointments();
                for (Appointments appt : deleteList) {
                    if (appt.getCustomerID() == customerTable.getSelectionModel().getSelectedItem().getCustomerID()) {
                            AppointmentsDAO.deleteCustomerAndAppointmens(customerTable.getSelectionModel().getSelectedItem().getCustomerID(), appt.getAppointmentID());
                            //code from delete appt
//                            Appointments selectedAppointment  = appointmentTable.getSelectionModel().getSelectedItem();
//                            AppointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentID());
//                            appointmentTable.setItems(AppointmentsDAO.getAllAppointments());

                        }
                    }
                }

                    //use the count method from ApptDAO for finding customerID with appoitmant ID
                    if (AppointmentsDAO.deleteCustNumberOfCustAppt(customerTable.getSelectionModel().getSelectedItem().getCustomerID()) > 0) {
                    } else {
                        String removedCustomerName = customerTable.getSelectionModel().getSelectedItem().getCustomerName();
                        CustomerDAO.deleteCustomer(customerTable.getSelectionModel().getSelectedItem().getCustomerID());

                        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                        alert3.setContentText("Customer " + removedCustomerName + " has been removed from the schedule.");
                        alert3.showAndWait();

                        customerObservableList = CustomerDAO.getAllCustomers();
                        customerTable.setItems(customerObservableList);
                        appointmentsObservableList = AppointmentsDAO.getAllAppointments();
                        appointmentTable.setItems(appointmentsObservableList);
                        customerTable.refresh();
                        appointmentTable.refresh();
                    }
                }
                }

//                selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

//                //todo need to add code to delete appt for customer
//                CustomerDAO.deleteCustomer(selectedCustomer.getCustomerID());
//                customerTable.setItems(CustomerDAO.getAllCustomers());
//                CustomerDAO.deleteCustomer(selectedCustomer);

            /*==============================THIS ACTUALLY WORKED!!!!!!    =================*/


    /**
     * This will log out the user from the scheduling system.
     * @param event
     * @throws IOException
     */
    public void onLogOut(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Press ok to continue logging out the application.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method gets the Reports scene.
     * @param event
     * @throws IOException
     */
    public void onReports(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ContactsReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }



}

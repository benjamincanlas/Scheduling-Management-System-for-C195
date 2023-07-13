package controller;

import DAO.AppointmentsDAO;
import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionsDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class SchedulerScreenController implements Initializable {


//public class SchedulerScreenController extends SceneTransfer {



    /** This variable is called when we define our search part function. */
    private static int modAppointment;
    /** This variable is called when we define our search product function. */
    private static int modCustomer;


    /**
     * @return modify Part selection when searching
     * This variable is later used for index id creation and passed in our "modifyAppointment" controller scene.
     */
    public static int getModAppointment() {
        return modAppointment;
    }
    /**
     * @return modify product selection during search
     * This variable is later used for index id creation and passed in our "modifyCustomer" controller scene.
     */
    public static int getModCustomer() {
        return modCustomer;
    }
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

    /** This will initialize and set data placement for the appointments and customers table.
     The sample and test data from the main Scheduler Screen menu will be retrieved and preloaded into this scene's fields. */

    /**
     *
     *
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {




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
        /**
         * Lambda gets ContactName from ContactID.
         */
        //lambda
        apptContactCol.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getContactName());
            } catch (SQLException | RuntimeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("title");
                alert.setHeaderText("header");
                alert.setContentText("Error");
                alert.showAndWait();

            }
            System.exit(0);
            return null;
        });





        //Customers Table
        /**
         *
         * Lambda usage for getting State/Province from DivisionID.
         */

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






        /**============ lambda =====================*/



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



    public void onAddAppt(ActionEvent event) throws IOException {







        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onModifyAppt(ActionEvent event) throws IOException, SQLException {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        modAppointment = AppointmentsDAO.getAllAppointments().indexOf(selectedAppointment);
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        } else {

//            ModifyAppointmentScreenController.receiveSelectedAppointment(appointmentTable.getSelectionModel().getSelectedItem());


            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointmentScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public void onDeleteAppt(ActionEvent event) throws SQLException {

        if (appointmentTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an appointment for deletion.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Press ok to continue deleting appointment.");
            Optional<ButtonType> result = alert.showAndWait();
            Appointments selectedAppointment = null;
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedAppointment  = appointmentTable.getSelectionModel().getSelectedItem();
//                CustomerDAO.deleteCustomer(selectedCustomer);
            }


            /**==============================FIX THIS BY CREATING DAO FOR APPT!!!!!!    =================*/
            AppointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentID());
            appointmentTable.setItems(AppointmentsDAO.getAllAppointments());


            /**=================================*/


        }
    }

    public void onWeekViewRadio(ActionEvent event) {
    }

    public void onMonthViewRadio(ActionEvent event) {
    }

    public void onAllViewRadio(ActionEvent event) {
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void onAddCust(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

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
     *
     * @param event
     * @throws SQLException
     */
    public void onDeleteCust(ActionEvent event) throws SQLException {
        if (customerTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a customer for deletion.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This customer and all associated appointments will be deleted. Are you sure? Press ok to continue deleting customer.");
            Optional<ButtonType> result = alert.showAndWait();
            Customer selectedCustomer = null;
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                //todo need to add code to delete appt for customer
                CustomerDAO.deleteCustomer(selectedCustomer.getCustomerID());
                customerTable.setItems(CustomerDAO.getAllCustomers());
//                CustomerDAO.deleteCustomer(selectedCustomer);
            }


            /*==============================THIS ACTUALLY WORKED!!!!!!    =================*/



            /*=================================*/


        }
    }




    public void onLogOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Press ok to continue logging out the application.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }




    public void onReports(ActionEvent event) {
    }






}

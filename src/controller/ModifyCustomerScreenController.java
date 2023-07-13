package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionsDAO;
import helper.WhiteSpaceChecker;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ModifyCustomerScreenController implements Initializable {
    private static Customer selectedCustomer;
    Stage stage;
    Parent scene;

    public TextField modCustIDTxt;
    public TextField modCustNameTxt;
    public TextField modCustAddressTxt;
    public TextField modCustZipTxt;
    public TextField modCustPhoneTxt;
    public ComboBox<Country> modCustCountryCombo;
    public ComboBox<FirstLevelDivisions> modCustStateCombo;
    public Button saveBtn;
    public Button cancelBtn;


    /**
     * Setting the 2nd combobox to null ensures a change in country resets the states/divisions.
     * @param event
     * @throws SQLException
     */
    public void onCountryCombo(ActionEvent event) throws SQLException {
        modCustStateCombo.setValue(null);
        Country country2 = modCustCountryCombo.getValue();
        modCustStateCombo.setItems(FirstLevelDivisionsDAO.getCountryDivisions(country2.getCountryID()));
    }
    public void onStateCombo(ActionEvent event) {
    }

    /**
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void onSave(ActionEvent event) throws SQLException, IOException {

        /**====================================*/

        String customerName = modCustNameTxt.getText();
        String address = modCustAddressTxt.getText();
        String postalCode = modCustZipTxt.getText();
        String phone = modCustPhoneTxt.getText();
        String updatedBy = "user"; //////must add a login page

        /**=======================================*/

        Country C = modCustCountryCombo.getSelectionModel().getSelectedItem();
        FirstLevelDivisions D = modCustStateCombo.getSelectionModel().getSelectedItem();

        if (WhiteSpaceChecker.validate(customerName) || WhiteSpaceChecker.validate(address) || WhiteSpaceChecker.validate(postalCode)
                || WhiteSpaceChecker.validate(phone))
        {
            System.out.println(" White space ");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty values must be completed.");
            alert.showAndWait();
            return;
        }
        if ( C == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: The country must not be empty");
            alert.showAndWait();
            return;
        }
        if (D == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: The division must not be empty");
            alert.showAndWait();
            return;
        }
        else {
            int divisionID = modCustStateCombo.getValue().getDivisionID();

            Customer updatedCustomer = new Customer(selectedCustomer.getCustomerID(), customerName, address, postalCode, phone,
                    selectedCustomer.getCreateDate(), selectedCustomer.getCreatedBy(), LocalDateTime.now(), updatedBy, divisionID);
            CustomerDAO.updateCustomer(updatedCustomer);
        }
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     *
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
     *
     * @param customer
     */
    public static void receiveSelectedCustomer(Customer customer) {
    selectedCustomer = customer;
}
    /**
     *
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FirstLevelDivisions division = FirstLevelDivisionsDAO.getDivision(selectedCustomer.getDivisionID());
            Country country = CountryDAO.getCountry(division.getCountryID());
            ObservableList<FirstLevelDivisions> divisionsObservableList = FirstLevelDivisionsDAO.getCountryDivisions(country.getCountryID());
            modCustCountryCombo.setValue(country);
            modCustStateCombo.setItems(divisionsObservableList);
            modCustStateCombo.setValue(division);
            modCustCountryCombo.setItems(CountryDAO.getAllCountry());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        modCustIDTxt.setText(((String.valueOf(selectedCustomer.getCustomerID()))));
        modCustNameTxt.setText(selectedCustomer.getCustomerName());
        modCustAddressTxt.setText(selectedCustomer.getAddress());
        modCustZipTxt.setText(selectedCustomer.getPostalCode());
        modCustPhoneTxt.setText(selectedCustomer.getPhone());
    }
}

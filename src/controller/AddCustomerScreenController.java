package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionsDAO;
import helper.WhiteSpaceChecker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Country;
import model.Customer;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Class for adding customers
 */
public class AddCustomerScreenController implements Initializable {
    Stage stage;
    Parent scene;
    public TextField addCustIdTxt;
    public TextField addCustNameTxt;
    public TextField addCustAddressTxt;
    public TextField addCustZip;
    public TextField addCustPhone;
    public ComboBox<Country> addCustCountryCombo;
    public ComboBox<FirstLevelDivisions> addCustStateCombo;
    public Button saveBtn;
    public Button cancelBtn;


    /**
     * This method sets the Country combo boxes based on the State/Province country IDs to match their given values. This way
     * it prevents the First Level Divisions combo from being select until a country is first selected.
     * @param event
     * @throws SQLException
     */
    public void onCountryCombo(ActionEvent event) throws SQLException {
        Country country1 = addCustCountryCombo.getValue();
        addCustStateCombo.setItems(FirstLevelDivisionsDAO.getCountryDivisions(country1.getCountryID()));
    }


public void onStateCombo(ActionEvent event) {
    }


    /**
     * This method adds and saves the inputted customer info as long as the fields are properly entered. Any missing info
     * or unselected combo boxes will trigger an alert.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void onSave(ActionEvent event) throws IOException, SQLException {


            String customerName = addCustNameTxt.getText();
            String address = addCustAddressTxt.getText();
            String postalCode = addCustZip.getText();
            String phone = addCustPhone.getText();
            String createdBy = Main.getUser();
            Country C = addCustCountryCombo.getSelectionModel().getSelectedItem();
            FirstLevelDivisions D = addCustStateCombo.getSelectionModel().getSelectedItem();

            if (WhiteSpaceChecker.validate(customerName) || WhiteSpaceChecker.validate(address) || WhiteSpaceChecker.validate(postalCode)
                     || WhiteSpaceChecker.validate(phone))
            {
                System.out.println(" White space ");

                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Empty values must be completed.");
                alert1.showAndWait();
                return;
            }
            if ( C == null) {
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setContentText("The country must not be empty");
                alert2.showAndWait();
                return;
            }
            if (D == null) {
                Alert alert3 = new Alert(Alert.AlertType.WARNING);
                alert3.setContentText("The division must not be empty");
                alert3.showAndWait();
                return;
            }
             else {
                int divisionID = addCustStateCombo.getValue().getDivisionID();

                Customer newCustomer = new Customer(1, customerName, address, postalCode, phone, LocalDateTime.now(), createdBy, LocalDateTime.now(), createdBy, divisionID);
                CustomerDAO.addCustomer(newCustomer);
            }
//        transferScene("/view/SchedulerScreen.fxml");
             stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
             scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
             stage.setScene(new Scene(scene));
             stage.show();
        }



    /**
     * This method leads back to the main menu without saving any info.
     * @param event
     * @throws IOException
     */
  @FXML

    void onCancel(ActionEvent event) throws IOException, SQLException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * This method initializes the Country combobox to populate which in turn the First Level Divisions is dependent on in
     * order to populate.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            addCustCountryCombo.setItems(CountryDAO.getAllCountry());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

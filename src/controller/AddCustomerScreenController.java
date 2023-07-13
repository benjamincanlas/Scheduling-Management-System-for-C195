package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionsDAO;
import helper.SceneTransfer;
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
import model.Country;
import model.Customer;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

//public class AddCustomerScreenController implements Initializable {
public class AddCustomerScreenController extends SceneTransfer implements Initializable {
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



    /**-========combo boxes =====*/
    /**
     *
     * @param event
     * @throws SQLException
     */
    public void onCountryCombo(ActionEvent event) throws SQLException {
        Country country1 = addCustCountryCombo.getValue();
        addCustStateCombo.setItems(FirstLevelDivisionsDAO.getCountryDivisions(country1.getCountryID()));


    }
/** ========Division based on Country ID=====*/
    public void onStateCombo(ActionEvent event) {

    }


    /**
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void onSave(ActionEvent event) throws IOException, SQLException {


            String customerName = addCustNameTxt.getText();
            String address = addCustAddressTxt.getText();
            String postalCode = addCustZip.getText();
            String phone = addCustPhone.getText();
            String createdBy = "user";
            Country C = addCustCountryCombo.getSelectionModel().getSelectedItem();
            FirstLevelDivisions D = addCustStateCombo.getSelectionModel().getSelectedItem();


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
     *
     * @param event
     * @throws IOException
     */
  @FXML

    void onCancel(ActionEvent event) throws IOException, SQLException {
//      setStage((Stage) cancelBtn.getScene().getWindow());
////      Node node = (Node) event.getSource();
////      transferScene(event, "/view/SchedulerScreen.fxml");
//      transferScene(event,"/view/SchedulerScreen.fxml");




        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     *This gets the country combo boxes to appear.
     *
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

package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Class for the Country customer reports interface showing the number customers for each
 */
public class CountryReportsScreenController implements Initializable {
    Stage stage;
    Parent scene;
    public RadioButton radioType;
    public ToggleGroup toggleRadioGroup;
    public RadioButton radioCountry;
    public RadioButton radioContact;
    public ComboBox<Country> countryReportsCombo;
    public Label numberLabelVariable;
    public Button exitButton;

    /**
     * This leads to the Types Months Report scene
     * @param event
     * @throws IOException
     */
    public void onRadioType(ActionEvent event) throws IOException {
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/TypeReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    public void onRadioCountry(ActionEvent event) throws IOException {
    }
    /**
     * This leads to the Contacts Report scene
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
     * This Method will display the countries in the drop down combo box and the selected one will display the amount
     * of customers it has scheduled
     * @param event
     * @throws SQLException
     */
    public void onCountryCombo(ActionEvent event) throws SQLException {
        Country selectedCountry = countryReportsCombo.getValue();
        int selectedCountryID = selectedCountry.getCountryID();
        numberLabelVariable.setText(String.valueOf(CustomerDAO.getCustomersViaCountryID(selectedCountryID)));
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
     *Method initializes and sets up the country combo boxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            radioCountry.setSelected(true);

            countryReportsCombo.setItems(CountryDAO.getAllCountry());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}

package controller;

import DAO.AppointmentsDAO;
import DAO.UserDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;
import model.Users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The login page for accessing the scheduling management system. All login attempts will be recorded.
 * No need to adjust location as ZoneID automatically sets  it based on user location.
 */
public class LoginScreenController implements Initializable {
    public Label loginFormLabel;
    Stage stage;
    Parent root;
    Parent scene;
    public TextField userNameTxt;
    public PasswordField passwordTxt;
    public Button loginButton;
    public Button resetButton;

    public Label zoneID;
    public Label usernameLabel;
    public Label passowordLabel;
    public Label locationLabel;
    ResourceBundle rb = ResourceBundle.getBundle("bundles/language", Locale.getDefault());


    /**
     * Method validates whether the user can login or not while recording all attempts. Once the user has logged in,
     * a sql expression from appointment DAO will alert if there is an upcoming appointment within 15 minutes in user's time.
     * Time zone conversions were made to be UTC as well as French conversions depending on user system default.
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void onLogin(ActionEvent event) throws SQLException, IOException {
        String username = userNameTxt.getText();
        String password = passwordTxt.getText();

        //Input Output text file to record all user log-in attempts, dates, and time stamps, as well as if they are successful or not

        String filename = "login_activity.txt", item;
        File file = new File(filename);
        FileWriter fWriter = new FileWriter(file, true);
        PrintWriter outputFile = new PrintWriter(fWriter);
        LocalDateTime localTimeNow = LocalDateTime.now();
        ZonedDateTime localTimeNowConvert = localTimeNow.atZone(ZoneId.systemDefault());
        ZonedDateTime localTimeNowUTC = localTimeNowConvert.withZoneSameInstant(ZoneId.of("UTC"));



        // TODO: 7/19/2023 : for both alerts, add French translation

        if (username.isBlank() || password.isBlank())
        {
            item = username;
            outputFile.println("User " + item + " was unable to login at the time of " + localTimeNowUTC);
            outputFile.close();
            System.out.println("Invalid Login");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Error"));
            alert.setContentText(rb.getString("EmptyDialog"));
            alert.showAndWait();
            return;
        }
        else{
            Users user = UserDAO.userNamePassWordValidator(username, password);

            if (user!=null) {
                item = username;
                outputFile.println("User " + item + " was able to login at the time of " + localTimeNowUTC);
                outputFile.close();

                ObservableList<Appointments> apptWithin15 = AppointmentsDAO.getAppointmentsWiting15Minuntes(LocalDateTime.now());

                if (apptWithin15.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(rb.getString("Warning"));
                    alert.setContentText(rb.getString("NoAppts15"));
                    alert.showAndWait();

                } else {

                    String upcomingAppt = rb.getString("upcomingAppt");
                    for (Appointments appointmentYes : apptWithin15) {
                        String readableDate = appointmentYes.getStart().format(DateTimeFormatter.ofPattern("MMM dd, yyyy  |  HH:mm"));
                        upcomingAppt  += "\nAppointment ID: " + appointmentYes.getAppointmentID() + " on " + readableDate + " ";
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(rb.getString("Warning"));
//                    alert.setContentText(rb.getString("YesAppts15"));
//                    alert.setContentText(rb.getString(upcomingAppt));
                    alert.setContentText((upcomingAppt));
                    alert.showAndWait();

                }
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else {
                item = username;
                outputFile.println("User " + item + " was unable to login at the time of " + localTimeNowUTC);
                outputFile.close();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("InvalidDialog"));
                alert.showAndWait();
            }
        }
    }

    /**
     * Method resets both username and password fields.
     * @param event
     */
    public void onReset(ActionEvent event) {
            userNameTxt.clear();
            passwordTxt.clear();
    }


    /**
     * This initializes the login language to either English or French based on the user default system location zone.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginFormLabel.setText(rb.getString("LoginForm"));
        usernameLabel.setText(rb.getString("Username"));
        passowordLabel.setText(rb.getString("Password"));
        locationLabel.setText(rb.getString("Location"));
        loginButton.setText(rb.getString("Login"));
        resetButton.setText(rb.getString("Reset"));
        zoneID.setText(ZoneId.systemDefault().toString());



    }
}

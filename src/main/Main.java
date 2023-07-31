package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {
    ResourceBundle rb = ResourceBundle.getBundle("bundles/language", Locale.getDefault());
    public static String user;
    public static String getUser() {
        return user;}
    public static void setUser(String user) {
        Main.user = user;}


//Uncomment below to skip login screen

//    @Override
//    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));
//
//        stage.setTitle("Scheduling Management System");
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

    /**
     * This Method transfers and loads to the Login screen.
     * @param stage
     * @throws Exception
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setTitle(rb.getString("SchedulingManagementSystem"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This opens the connection to the JDB database and closes it when exiting.
     * @param args
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}






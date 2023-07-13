package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**===============Uncomment below to get the project to show the Screens!!!!.=================================*/
public class Main extends Application {

    public static String user = null;
    public static String location = null;

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        Main.location = location;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Main.user = user;
    }





    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/SchedulerScreen.fxml"));

        stage.setTitle("Scheduling Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }








    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}



        /** Below this are practice examples, delete before submitting!
         * JDBC practice webinar
         * Using FruitsQuery helper
         * INSERT UPDATE DELETE   , SELECT  */

//public class Main {
//    public static void main(String[] args) throws SQLException {
////        launch(args);
//
//
//        JDBC.openConnection();
//
//
//
//
//        /** Below this are practice examples, delete before submitting!
//         * JDBC practice webinar
//         * Using FruitsQuery helper
//         * INSERT UPDATE DELETE   , SELECT  */
//
//
//        /**=========INSERT==============*/
//
////        int rowsAffected = FruitsQuery.insert("Loquat", 2);
////
////
////        if(rowsAffected > 0){
////            System.out.println("Insert worked!");
////        }
////        else {
////            System.out.println("Insert fail!");
////
////        }
//
//        /** ============UPDATE ===============*/
//
//        int rowsAffected = FruitsQuery.update(14, 3, "Loquats");
//
//
//        if(rowsAffected > 0){
//            System.out.println("Update worked!");
//        }
//        else {
//            System.out.println("Update fail!");
//
//        }
////
//
//
//        /**======== DELETE==================*/
//
////        int rowsAffected = FruitsQuery.delete(9);
////
////
////        if(rowsAffected > 0){
////            System.out.println("Delete worked!");
////        }
////        else {
////            System.out.println("Delete fail!");
////
////        }
//
//
//        /**===== SELECT ==============*/
//
////        FruitsQuery.select(3);
//
//
//
//
//
//
//        /** Above this are practice examples, delete before submitting! */
//
//
//
//
//
//
//
//
//
//
//        JDBC.closeConnection();
//    }
//}
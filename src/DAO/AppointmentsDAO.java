package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static helper.JDBC.connection;

public class AppointmentsDAO {
    /**
     * This list contains the Appointments.
     */
    private static ObservableList<String> appointmentTypes = FXCollections.observableArrayList("Meeting","Lunch", "Break");

    /**
     * The generated index for the part id starts at 1.
     */
    private static int autoId = 1;

    /**
     * @return This part id will be returned and generated when called.
     */
    public static int getAutoId() {
        return autoId++;
    }

    /**
     * @param newAppointments to be added
     */
    public static void addAppointments(Appointments newAppointments) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By,"
        + "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, newAppointments.getTitle());
        ps.setString(2, newAppointments.getDescription());
        ps.setString(3, newAppointments.getLocation());
        ps.setString(4, newAppointments.getType());
        ps.setTimestamp(5, Timestamp.valueOf(newAppointments.getStart()));
        ps.setTimestamp(6, Timestamp.valueOf(newAppointments.getEnd()));
        ps.setTimestamp(7, Timestamp.valueOf(newAppointments.getCreateDate()));
        ps.setString(8, newAppointments.getCreatedBy());
        ps.setTimestamp(9, Timestamp.valueOf(newAppointments.getLastUpdate()));
        ps.setString(10, newAppointments.getLastUpdatedBy());
        ps.setInt(11, newAppointments.getCustomerID());
        ps.setInt(12, newAppointments.getUserID());
        ps.setInt(13, newAppointments.getContactID());
        ps.executeUpdate();
    }


    /**
     * @param selectedAppointments set to be modified
     */
    public static void updateAppointments(Appointments selectedAppointments) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?,"
                + "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, selectedAppointments.getTitle());
        ps.setString(2, selectedAppointments.getDescription());
        ps.setString(3, selectedAppointments.getLocation());
        ps.setString(4, selectedAppointments.getType());
        ps.setTimestamp(5, Timestamp.valueOf(selectedAppointments.getStart()));
        ps.setTimestamp(6, Timestamp.valueOf(selectedAppointments.getEnd()));
        ps.setTimestamp(7, Timestamp.valueOf(selectedAppointments.getLastUpdate()));
        ps.setString(8, selectedAppointments.getLastUpdatedBy());
        ps.setInt(9, selectedAppointments.getCustomerID());
        ps.setInt(10, selectedAppointments.getUserID());
        ps.setInt(11, selectedAppointments.getContactID());
        ps.setInt(12, selectedAppointments.getAppointmentID());
        ps.executeUpdate();
    }



    public static void deleteAppointment (int appointmentID) throws SQLException {

        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
    }


    /**
     * @return Appointments list
     */
    public static ObservableList<Appointments> getAllAppointments() throws SQLException {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Appointments";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();


        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentsAdded = new Appointments(appointmentID, title, description,
                    location, type, start.toLocalDateTime(), end.toLocalDateTime(), createDate.toLocalDateTime(),
                    createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, customerID, userID, contactID);


            allAppointments.add(appointmentsAdded);

        }
        return allAppointments;
    }

    public static ObservableList<String> getAppointmentTypes() throws SQLException {
        //if appt isnt in db it will add it

        String sql = "SELECT distinct type FROM Appointments";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String type = rs.getString("Type");
            if (appointmentTypes.contains(type)){
                continue;
            }
            appointmentTypes.add(type);
        }
        return appointmentTypes;
    }

}




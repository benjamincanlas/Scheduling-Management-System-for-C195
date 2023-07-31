package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static helper.JDBC.connection;

/**
 * This class implements the database methods for appointments with SQL statements.
 */
public class AppointmentsDAO {
    /**
     * This list contains the Appointments.
     */
    private static ObservableList<String> appointmentTypes = FXCollections.observableArrayList("Meeting","Lunch", "Break");


    /**
     * Method adds the appointment into the table which gets it from the JBDC database server.
     * @param newAppointments new created with new info
     * @throws SQLException
     */
    public static void addAppointments(Appointments newAppointments) throws SQLException {
        //sql not case sensitive
        String sqlaappt = "INSERT INTO Appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By,"
        + "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                ;
        PreparedStatement ps = connection.prepareStatement(sqlaappt);
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
     * THis method will update/modify the selected appointment back in the DB.
     * @param selectedAppointments set to be modified
     */
    public static void updateAppointments(Appointments selectedAppointments) throws SQLException {
        String sqluappt = "UPDATE Appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?,Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ? "
                ;
        PreparedStatement ps = connection.prepareStatement(sqluappt);

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


    /**
     * This method will delete an appointment using its ID.
     * @param appointmentID used to delete and identify
     * @throws SQLException
     */
    public static void deleteAppointment (int appointmentID) throws SQLException {

        String sqldappt = "DELETE FROM Appointments " +
                "WHERE Appointment_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sqldappt);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
    }

    /**
     * Used for deleting customer methods with appoitnments via ID.
     * @param custID selected
     * @param apptID number of appointments
     */
    public static void deleteCustomerAndAppointmens (int custID, int apptID) throws SQLException {
        try {
            String sqldelcustappt = "DELETE FROM Appointments " +
                    "WHERE Customer_ID = ? AND Appointment_ID = ?";
            PreparedStatement ps = connection.prepareStatement(sqldelcustappt);
            ps.setInt(1, custID);
            ps.setInt(2, apptID);
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e)
                    ;
        }

    }

    /**
     * This method finds the number of appoints of the customer based on their ID, used for delete customer method when the
     * customer has any appointments more than one.
     * @param custID count the amount of appointments they have
     * @return amount of appointment with from same customer
     * @throws SQLException
     */
    public static int deleteCustNumberOfCustAppt(int custID) throws SQLException {
        String sqlcustcount = "SELECT COUNT(*)  AS CustAppt FROM Appointments " +
                "WHERE Customer_ID  = '" + custID + "'"
                ;
        //spacing matters, space between count and(*) caused headaches
        PreparedStatement ps = connection.prepareStatement(sqlcustcount);
        int countCustApptRS = 0;
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            countCustApptRS = rs.getInt("CustAppt")
            ;
            return countCustApptRS;
        }
        return countCustApptRS;
    }

    /**
     * This method is called in Type and Month reports where the number of selected types are filter counted based on the selected
     * month.
     * @param selectedType filtered by type
     * @param month filtered by month
     * @return Number of appointment types  based on month selected
     * @throws SQLException
     */
    public static int getMonthFromType(String selectedType, String month) throws SQLException {
        String sqlmfromt = "SELECT COUNT(*) AS monthType " +
                "FROM Appointments " +
                "WHERE Type  = '" +selectedType+ "' AND MONTHNAME(Start) = '" +month+ "'";
        PreparedStatement ps = connection.prepareStatement(sqlmfromt);
        int countMonthTypeResult = 0;
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            countMonthTypeResult = rs.getInt("monthType")
            ;

            return countMonthTypeResult;
        }
        return countMonthTypeResult;
    }


    /**
     * This method will display all appointments in the DB.
     * @return All appointments without any filter
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAllAppointments() throws SQLException {
        ObservableList<Appointments> allAppointmentsList = FXCollections.observableArrayList();

        String sqlallappt = "SELECT * FROM Appointments";
        PreparedStatement ps = connection.prepareStatement(sqlallappt);
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

            Appointments appointmentsAdded = new Appointments(appointmentID, title, description, location,
                    type, start.toLocalDateTime(), end.toLocalDateTime(), createDate.toLocalDateTime(),
                    createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, customerID, userID, contactID);

            allAppointmentsList.add(appointmentsAdded)
            ;
        }
        return allAppointmentsList;
    }

    /**
     * This method gets all the specific type of appointments for use in the combobox drop down from DB. Type does not
     * have their own model class.
     * @return List appointment types filter
     * @throws SQLException
     */
    public static ObservableList<String> getAppointmentTypes() throws SQLException {
        //if appt isnt in db it will add it

        String sqlappttype = "SELECT Distinct Type " +
                "FROM Appointments";
        PreparedStatement ps = connection.prepareStatement(sqlappttype);
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


    /**
     * This method get all apppointments based on their contact ID from DB.
     * @param contactID filtered by contact ID
     * @return appointment via Contact ID
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAllAppointmentsViaContactID(int contactID) throws SQLException {
        ObservableList<Appointments> appointmentViaContactID = FXCollections.observableArrayList();

        String sqlCID = "SELECT * FROM Appointments " +
                "WHERE Contact_ID = ? " +
                "ORDER BY Start";
        PreparedStatement ps = connection.prepareStatement(sqlCID);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();
        //do not mispell the column labels as they ARE case sensitve

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

            Appointments thisAppt = new Appointments(appointmentID, title, description, location, type,
                    start.toLocalDateTime(), end.toLocalDateTime(), createDate.toLocalDateTime(), createdBy,
                    lastUpdate.toLocalDateTime(), lastUpdatedBy, customerID, userID, contactID);
            appointmentViaContactID.add(thisAppt);
        }
        return appointmentViaContactID;
    }


    /**
     * This method is called on the login page where it finds any appointment in the DB or table that will begin
     * within 15 minutes of the user logging in their credentials.
     * @param timeSTamp15 the value the login time in parameter 1 and the appointment time in parameter 2
     * @return any appointments filtered within 15 minutes
     * @throws SQLException
     */

    public static ObservableList<Appointments> getAppointmentsWiting15Minuntes (LocalDateTime timeSTamp15) throws SQLException {
        ObservableList<Appointments> appointmentsWithin15 = FXCollections.observableArrayList();
        //interval with the + is 15 minutes in the future
        //spelling matters, minute, adding an s into minutes caused sql headaches

        String sql15 = "SELECT * FROM Appointments " +
                "WHERE Start BETWEEN ? AND (? + Interval 15 Minute)"
                ;
        //instead of 15 in the sql we can also plusMInute(15) on lines365
        PreparedStatement ps = connection.prepareStatement(sql15);
        ps.setTimestamp(1, Timestamp.valueOf(timeSTamp15));
        ps.setTimestamp(2, Timestamp.valueOf(timeSTamp15));
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

            Appointments thisAppt15Min = new Appointments(appointmentID, title, description, location, type, start.toLocalDateTime(),
                    end.toLocalDateTime(), createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy,
                    customerID, userID, contactID);
            appointmentsWithin15.add(thisAppt15Min);
        }
        return appointmentsWithin15;
    }

    /**
     * This method filters the list of appointments displayed within a month or (plus 30 days) from the date the user is in as
     * well the earliest time of teh the day. Could have just put LocalTime of now rather than 0 hours and 0 minutes.
     * @param nowDate current date
     * @return filtered list of appointments within 30 days upcoming
     * @throws SQLException
     */
    public static ObservableList<Appointments> filterAppointmentsViaMonth(LocalDate nowDate) throws SQLException {
        ObservableList<Appointments> appointmentsfMonths = FXCollections.observableArrayList();
        LocalDateTime ldt = LocalDateTime.of(nowDate, LocalTime.of(0,0));
        String sqlW = "SELECT * FROM Appointments " +
                "WHERE Start Between ? AND ?"
                ;

        PreparedStatement ps = connection.prepareStatement(sqlW);
        ps.setTimestamp(1, Timestamp.valueOf(ldt));
        ps.setTimestamp(2,  Timestamp.valueOf(ldt.plusDays(30)));
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

            Appointments thisApptMonth = new Appointments(appointmentID, title, description, location, type, start.toLocalDateTime(),
                    end.toLocalDateTime(), createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(),
                    lastUpdatedBy, customerID, userID, contactID);
            appointmentsfMonths.add(thisApptMonth);
        }
        return appointmentsfMonths;
    }


    /**
     * This method filters the list of appointments displayed within a week or 7 days from the date the user is in as
     *  well the earliest time of the day.
     * @param nowDate current date
     * @return filtered list of appointments within 7 days upcoming
     * @throws SQLException
     */
    public static ObservableList<Appointments> filterAppointmentsViaWeek(LocalDate nowDate) throws SQLException {
        ObservableList<Appointments> appointmentsfWeeks = FXCollections.observableArrayList();
//        Timestamp filterDateWeeks = Timestamp.valueOf(date.atStartOfDay());
        LocalDateTime ldt = LocalDateTime.of(nowDate, LocalTime.of(0,0));
        String sqlW = "SELECT * FROM Appointments " +
                "WHERE Start Between ? AND ?"
                ;
        PreparedStatement ps = connection.prepareStatement(sqlW);
        ps.setTimestamp(1, Timestamp.valueOf(ldt));
        ps.setTimestamp(2,  Timestamp.valueOf(ldt.plusDays(7)));
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

            Appointments thisApptWeek = new Appointments(appointmentID, title, description, location, type,
                    start.toLocalDateTime(), end.toLocalDateTime(), createDate.toLocalDateTime(), createdBy,
                    lastUpdate.toLocalDateTime(), lastUpdatedBy, customerID, userID, contactID);
            appointmentsfWeeks.add(thisApptWeek);
        }
        return appointmentsfWeeks;
    }

}




package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static helper.JDBC.connection;

/**
 * Class created for the user login and connect with the DB
 * make methods static for being called 
 */
public class UserDAO {
    /**
     * Method retrieves all database users
     * @return list of user with no filter
     * @throws SQLException
     */
    public static ObservableList<Users> getAllUser() throws SQLException {
        ObservableList<Users> userALL = FXCollections.observableArrayList();
        //SQL Users not user

        String sqlALL = "SELECT * FROM Users";
        PreparedStatement ps = connection.prepareStatement(sqlALL);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            Users thisUser = new Users(userID, userName, password, createDate.toLocalDateTime(),
                    createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy);
            userALL.add(thisUser);
        }
        return userALL;
    }


    /**
     * This method retrieves a user based on the user's ID. Used in Mod Appointments when determining the unique user ID.
     * @param userID created from each added customer
     * @return list of users based on ID
     * @throws SQLException
     */
    public static Users getUserBasedOnID(int userID) throws SQLException {
        String sqlUID = "SELECT * FROM users " +
                "WHERE User_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sqlUID);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            Users thisUserID;
            thisUserID = new Users(userID, userName, password, createDate.toLocalDateTime(), createdBy,
                    lastUpdate.toLocalDateTime(), lastUpdatedBy);
            return thisUserID;
        }
        return null;
    }

    /**
     * Used in login page, this method validates the entered username and password based from the database.
     * @param userName validates if its in the DB
     * @param passWord validates if its in the DB
     * @return If successful the user will be able to login the scheduler.
     * @throws SQLException
     */
    public static Users userNamePassWordValidator(String userName, String passWord) throws SQLException {
        String sqlUPW = "SELECT * FROM Users " +
                "WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = connection.prepareStatement(sqlUPW);
        ps.setString(1, userName);
        ps.setString(2, passWord);
        //these Strings must match the variable in login page
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int userID = rs.getInt("User_ID");
            String password = rs.getString("Password");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            Users loginValidator;
            loginValidator = new Users(userID, userName, password, createDate.toLocalDateTime(), createdBy,
                    lastUpdate.toLocalDateTime(), lastUpdatedBy);


            return loginValidator;
        }
        return null;
    }


   
}




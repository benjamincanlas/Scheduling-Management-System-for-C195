package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

/**
 * This class implements the database methods for contacts with SQL statements.
 *  For the methods to be usable, make them into static
 */
public class ContactsDAO {


    /**
     * Used in Contact ID combo boxes, this method retrieves all 3 contacts
     * @return list of contacts, like all 3
     * @throws SQLException
     */
    public static ObservableList<Contacts> getAllContacts() throws SQLException {
        ObservableList<Contacts> contactsAll = FXCollections.observableArrayList();
        //sql words not case sensitive

        String sqlAll = "SELECT * FROM Contacts";
        PreparedStatement ps = connection.prepareStatement(sqlAll);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            //these must match the Models in syntax and spelling
            Contacts thisContact = new Contacts(contactID, contactName, email);
            contactsAll.add(thisContact);
        }
        return contactsAll;
    }

    /**
     * Used in retrieving contacts to display in appointment tables and reports
     * @param contactID filter by ID
     * @return list of appointments based on contact ID
     * @throws SQLException
     */
    public static Contacts getContact(int contactID) throws SQLException {
        String sqlCID = "SELECT * FROM Contacts " +
                "WHERE Contact_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sqlCID);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();
        //column labels are case sensitive
        if (rs.next()) {
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            Contacts thisContactID;
            thisContactID = new Contacts(contactID, contactName, email);
            return thisContactID;
        }
        return null;
    }




}

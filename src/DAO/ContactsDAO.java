package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactsDAO {



    public static ObservableList<Contacts> getAllContacts() throws SQLException {
        ObservableList<Contacts> contacts = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            contacts.add(new Contacts(contactID, contactName, email));
        }
        return contacts;
    }


    public Contacts getContact(int contactID) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            return new Contacts(contactID, contactName, email);
        }
        return null;
    }


    public void addContacts(Contacts contact) throws SQLException {
        String sql = "INSERT INTO contacts (Contact_Name, Email) VALUES (?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contact.getContactName());
        ps.setString(2, contact.getEmail());
        ps.executeUpdate();
    }




    public void updateContacts(Contacts contact) throws SQLException {
        String sql = "UPDATE contacts SET Contact_Name = ?, Email = ? WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contact.getContactName());
        ps.setString(2, contact.getEmail());
        ps.setInt(3, contact.getContactID());
        ps.executeUpdate();
    }



    public void deleteContacts(int contactID) throws SQLException {
        String sql = "DELETE FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        ps.executeUpdate();
    }

}

package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static helper.JDBC.connection;


/**
 * This class implements the database methods for customer with SQL statements.
 * For the methods to be usable, make them into static
 */
public class CustomerDAO {
    /**
     * This list contains the customers.
     */
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();


    /**
     * Method adds the customer into the table which gets it from the JBDC database server and displayed .
     * @param newCustomer to be added with new info
     * @throws SQLException
     */
    public static void addCustomer(Customer newCustomer) throws SQLException {

//        insert(String fruitName, int colorId) throws SQLException {
//            String sql = "INSERT INTO FRUITS (Fruit_Name, Color_ID) VALUES(?, ?)";
//            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//            ps.setString(1, fruitName);
//            ps.setInt(2, colorId);
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected;

        String sql = "INSERT INTO Customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        private static Statement st;
//        st=connection.createStatement();

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, newCustomer.getCustomerName());
        ps.setString(2, newCustomer.getAddress());
        ps.setString(3, newCustomer.getPostalCode());
        ps.setString(4, newCustomer.getPhone());
        ps.setTimestamp(5, Timestamp.valueOf(newCustomer.getCreateDate()));
        ps.setString(6, newCustomer.getCreatedBy());
        ps.setTimestamp(7, Timestamp.valueOf(newCustomer.getLastUpdate()));
        ps.setString(8, newCustomer.getLastUpdatedBy());
        ps.setInt(9, newCustomer.getDivisionID());
        ps.executeUpdate();

        }



/**====================*/
//String sql = "SELECT * FROM FRUITS WHERE Color_ID = ?";
//    PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, colorId);
//    ResultSet rs = ps.executeQuery();
//        while(rs.next()) {
//        int fruitId = rs.getInt("FRUIT_ID");
//        String fruitName = rs.getString("FRUIT_NAME");
//        int colorIdFK = rs.getInt("Color_ID");

    /**
     * This method retrieves custoemrs from the DB using their ID
     * @param customerID filter for retrieval
     * @return finds the customer based on ID
     * @throws SQLException
     */
    public static Customer getCustomer(int customerID) throws SQLException {
        String sql = "SELECT * FROM Customers " +
                "WHERE Customer_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionID = rs.getInt("Division_ID");

            return new Customer(customerID, customerName, address, postalCode, phone, createDate.toLocalDateTime(),
                    createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, divisionID);
        }
        return null;
    }


    /**
     * Method gets the selected customer and updates any based on any changed parameters.
     * @param selectedCustomer set to be modified
     * @throws SQLException
     */
    public static void updateCustomer(Customer selectedCustomer) throws SQLException {
        String sql = "UPDATE Customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, " +
                "Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, selectedCustomer.getCustomerName());
        ps.setString(2, selectedCustomer.getAddress());
        ps.setString(3, selectedCustomer.getPostalCode());
        ps.setString(4, selectedCustomer.getPhone());
        ps.setTimestamp(5, Timestamp.valueOf(selectedCustomer.getLastUpdate()));
        ps.setString(6, selectedCustomer.getLastUpdatedBy());
        ps.setInt(7, selectedCustomer.getDivisionID());
        ps.setInt(8, selectedCustomer.getCustomerID());
        ps.executeUpdate();

    }
//    public static boolean deleteCustomer(Customer selectedCustomer) {
//
//        return allCustomers.remove(selectedCustomer);
//    }
    /**=====THIS WORKS!!!!!!!!----------*/

    /**
     * THis method deletes the customerr based on ID
     * @param customerID set to be deleted
     * @throws SQLException
     */
    public static void deleteCustomer(int customerID) throws SQLException {

        String sql = "DELETE FROM Customers " +
                "WHERE Customer_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.executeUpdate();
    }

    /**======*/

//    String sql = "DELETE FROM FRUITS WHERE FRUIT_ID = ?";
//    PreparedStatement ps = JDBCold.connection.prepareStatement(sql);
//        ps.setInt(1, fruitId);
//    int rowsAffected = ps.executeUpdate();
//        return rowsAffected;


    /**
     * This method will display all customers in the DB.
     * @return list of customers retrieved without any filter
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
            ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

            String sql = "SELECT * FROM Customers";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divisionID = rs.getInt("Division_ID");

                Customer customerAdded = new Customer(customerID, customerName, address, postalCode,
                        phone, createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy,
                        divisionID);
                allCustomers.add(customerAdded);
            }
            return allCustomers;
        }

//    String sql = "SELECT * FROM FRUITS";
//    PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//    ResultSet rs = ps.executeQuery();
//        while(rs.next()) {
//        int fruitId = rs.getInt("FRUIT_ID");
//        String fruitName = rs.getString("FRUIT_NAME");



    //Todo: Is there a way to write a sql command where it selects based on country from custoemrs


    /**
     * This method retrieves customers based on the country and utilized in the country reports scene.
     * @param CountryID identifies which country customer is in
     * @return filtered list of customer using country ID
     * @throws SQLException
     */
    public static int getCustomersViaCountryID(int CountryID) throws SQLException {
        String sqlCountryID = "SELECT COUNT(*) AS customerCountry " +
                "FROM customers " +
                "WHERE Division_ID IN (SELECT Division_ID FROM First_Level_Divisions " +
                "WHERE Country_ID = " + CountryID + ")"
                ;
        PreparedStatement ps = connection.prepareStatement(sqlCountryID);
        int numberCustomersVariable = 0;
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            numberCustomersVariable = rs.getInt("customerCountry");
            return numberCustomersVariable;
        }
        return numberCustomersVariable;
    }





}

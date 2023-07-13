package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static helper.JDBC.connection;




public class CustomerDAO {
    /**
     * This list contains the customers.
     */
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();



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
     * @param newCustomer to be added
     */
    public static void addCustomer(Customer newCustomer) throws SQLException {


//        insert(String fruitName, int colorId) throws SQLException {
//            String sql = "INSERT INTO FRUITS (Fruit_Name, Color_ID) VALUES(?, ?)";
//            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//            ps.setString(1, fruitName);
//            ps.setInt(2, colorId);
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected;

        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, " +
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


//    public Customer getCustomer(int customerID) throws SQLException {
//        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
//        PreparedStatement ps = connection.prepareStatement(sql);
//        ps.setInt(1, customerID);
//        ResultSet rs = ps.executeQuery();
//
//        if (rs.next()) {
//            String customerName = rs.getString("Customer_Name");
//            String address = rs.getString("Address");
//            String postalCode = rs.getString("Postal_Code");
//            String phone = rs.getString("Phone");
//            Timestamp createDate = rs.getTimestamp("Create_Date");
//            String createdBy = rs.getString("Created_By");
//            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
//            String lastUpdatedBy = rs.getString("Last_Updated_By");
//            int divisionID = rs.getInt("Division_ID");
//
//            return new Customer(customerID, customerName, address, postalCode, phone, createDate.toLocalDateTime(),
//                    createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, divisionID);
//        }
//        return null;
//    }


    /**
     * @param selectedCustomer set to be modified
     */
    public static void updateCustomer(Customer selectedCustomer) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, " +
                "Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

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


//        allCustomers.set(index, selectedCustomer);
    }

    /**
     * @return The customer that was selected and deleted.
     */
//    public static boolean deleteCustomer(Customer selectedCustomer) {
//
//        return allCustomers.remove(selectedCustomer);
//    }

    /**=====THIS WORKS!!!!!!!!----------*/

    public static void deleteCustomer(int customerID) throws SQLException {

        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
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
     * @return customer list
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

                Customer customerAdded = new Customer(customerID, customerName, address, postalCode, phone, createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, divisionID);
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



}

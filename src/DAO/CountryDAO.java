package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static helper.JDBC.connection;


/**
 *
 *  This class implements the database methods for country with SQL statements.
 *   For the methods to be usable, make them into static
 *
 */
public class CountryDAO {
    private static ObservableList<Country> allCountry = FXCollections.observableArrayList();


    /**
     * Used in Country ID combo boxes, this method retrieves all 3 countries or if a table, displays the country.
     * @return list of all countries without a filter
     * @throws SQLException
     */
    public static ObservableList<Country> getAllCountry() throws SQLException {
        String sqlAll = "SELECT * FROM Countries";
        ObservableList<Country> allCountries = FXCollections.observableArrayList();


        PreparedStatement ps = connection.prepareStatement(sqlAll);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String country = rs.getString("country");

            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");


            Country country1 = new Country(countryID, country, createDate.toLocalDateTime(), createdBy,
                    lastUpdate.toLocalDateTime(), lastUpdatedBy);
            allCountries.add(country1);

        }
        return allCountries;
    }

    /**
     * Lambda expression to get a country based on the country ID as list in the customer combo boxes and ensure
     * it is selected before user can move on to the states/provinces. This in makes it so the 1st level divisions is dependant
     * on the country selected.
     * @param countryID filter by ID
     * @return list of countries filtered by ID
     */
    public static Country getCountry(int countryID){
        Country country1= new Country(0, "", LocalDateTime.now(), "", LocalDateTime.now(), "");
        ObservableList<Country> list = null;
        try {
            list=getAllCountry().stream()
                    .filter(c -> c.getCountryID()==countryID)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            if (!list.isEmpty()) {
                country1= list.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return country1;
    }




}

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

public class CountryDAO {
    private static ObservableList<Country> allCountry = FXCollections.observableArrayList();


    public static ObservableList<Country> getAllCountry() throws SQLException {
        String sql = "Select * from countries";
        ObservableList<Country> allCountries = FXCollections.observableArrayList();


        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String country = rs.getString("country");

            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");


            Country country1 = new Country(countryID, country, createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy);
            allCountries.add(country1);

        }
        return allCountries;
    }

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

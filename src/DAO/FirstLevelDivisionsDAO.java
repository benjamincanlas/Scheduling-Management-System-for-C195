package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class FirstLevelDivisionsDAO {
    private static ObservableList<FirstLevelDivisions> allDivisions = FXCollections.observableArrayList();


    public static ObservableList<FirstLevelDivisions> getAllDivisions() throws SQLException {
        String sql = "Select * from first_level_divisions";
        ObservableList<FirstLevelDivisions> allStates = FXCollections.observableArrayList();


        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("division");

            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryID = rs.getInt("Country_ID");


            FirstLevelDivisions state1 = new FirstLevelDivisions(divisionID, division, createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, countryID);
            allStates.add(state1);

        }
        return allStates;
        /**=== previously "return allDivisions"  ======*/
    }



/**===========================================================*/





/**================================*/


/**filter===*/
    public static ObservableList<FirstLevelDivisions> getCountryDivisions(int countryID) throws SQLException {
        return getAllDivisions().stream()
                .filter(d -> d.getCountryID() == countryID)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }


    public static FirstLevelDivisions getDivision(int divisionID)  {
        FirstLevelDivisions division1 = new FirstLevelDivisions(0,"unknown", LocalDateTime.now(),"", LocalDateTime.now(),"", 0);

        ObservableList<FirstLevelDivisions> list= null;
        try {
            list = getAllDivisions().stream()
                    .filter(d -> d.getDivisionID() == divisionID)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            if (!list.isEmpty()) {
                division1= list.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return division1;
    }







}
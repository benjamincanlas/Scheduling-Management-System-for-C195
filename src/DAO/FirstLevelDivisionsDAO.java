package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static helper.JDBC.connection;

/**
 *  This class implements the database methods for contacts with SQL statements.
 *  For the methods to be usable, make them into static
 */
public class FirstLevelDivisionsDAO {
    private static ObservableList<FirstLevelDivisions> allDivisions = FXCollections.observableArrayList();

    /**
     * Method will retrieve all the states and provinces
     * @return list of states and provinces without any filters
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivisions> getAllDivisions() throws SQLException {
        String sqlALL = "SELECT * FROM first_level_divisions";
        //sql is not case sensitve but spelling counts including spacing
        ObservableList<FirstLevelDivisions> allStates = FXCollections.observableArrayList();

        PreparedStatement ps = connection.prepareStatement(sqlALL);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("division");

            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryID = rs.getInt("Country_ID");


            FirstLevelDivisions state1 = new FirstLevelDivisions(divisionID, division, createDate.toLocalDateTime(),
                    createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, countryID);
            allStates.add(state1);

        }
        return allStates;
        /**=== previously "return allDivisions"  ======*/
    }




    /** Lambda expression
     * This method retrieves a list of countries based on its ID. These will be matched with the ID that every First level
     * division has to ensure country like US is matched with CAlifornia and UK with England.
     * @param countryID filter to be matched in First level divisions
     * @return States and Provinces based on country ID
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivisions> getCountryDivisions(int countryID) throws SQLException {
        return getAllDivisions().stream()
                .filter(d -> d.getCountryID() == countryID)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Lambda expression to get a state/ province based on the division ID as list in the customer combo boxes and ensure
     *  it is selected before user can move on to the states/provinces. This in makes it so the 1st level divisions is dependant
     * on the country selected.
     * @param divisionID filter to match with ID and country
     * @return filtered list of divisions matched with country
     */
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
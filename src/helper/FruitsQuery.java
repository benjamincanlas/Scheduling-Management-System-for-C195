package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**=================== JDBC WEBINAR PRACTICE - REFERENCE ONLY =======================*/

public abstract class FruitsQuery {
    public static int insert(String fruitName, int colorId) throws SQLException {
        String sql = "INSERT INTO FRUITS (Fruit_Name, Color_ID) VALUES(?, ?)";
        PreparedStatement ps = JDBCold.connection.prepareStatement(sql);
        ps.setString(1, fruitName);
        ps.setInt(2, colorId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }
/** ================== CODE review with Prof Sher DeCusatis on 5/16/23 @ 11:30====================
 * Changes I made/ added:
 * "int colorId" on line 23
 * "SET Color_ID = ?" on line 27
 * added line 31
 * wanted to set or update colorID from 1 to 3
 *
 *  ====Resolved by replacing SET with comma
 *  */
    public static int update(int fruitId, int colorId, String fruitName) throws SQLException {
        String sql = "UPDATE FRUITS SET Fruit_Name = ?, Color_ID = ?  WHERE Fruit_ID = ?";
        PreparedStatement ps = JDBCold.connection.prepareStatement(sql);
        ps.setString(1, fruitName);
        ps.setInt(2, colorId);
        ps.setInt(3, fruitId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    public static int delete(int fruitId) throws SQLException {
        String sql = "DELETE FROM FRUITS WHERE FRUIT_ID = ?";
        PreparedStatement ps = JDBCold.connection.prepareStatement(sql);
        ps.setInt(1, fruitId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;



    }


    public static void select() throws SQLException {
        String sql = "SELECT * FROM FRUITS";
        PreparedStatement ps = JDBCold.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int fruitId = rs.getInt("FRUIT_ID");
            String fruitName = rs.getString("FRUIT_NAME");




            System.out.print(fruitId + " | ");
            System.out.print(fruitName + "\n");


        }

    }


    public static void select(int colorId) throws SQLException {
        String sql = "SELECT * FROM FRUITS WHERE Color_ID = ?";
        PreparedStatement ps = JDBCold.connection.prepareStatement(sql);
        ps.setInt(1, colorId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int fruitId = rs.getInt("FRUIT_ID");
            String fruitName = rs.getString("FRUIT_NAME");
            int colorIdFK = rs.getInt("Color_ID");
            System.out.print(fruitId + " | ");
            System.out.print(fruitName + " | ");
            System.out.print(colorIdFK + "\n");


        }

    }
}

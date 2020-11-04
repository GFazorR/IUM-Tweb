package Dao;

import Model.Booking;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingDao {
    // TODO: 14/10/2020 add Bookings Queries
    public static ArrayList<Booking> getBookedSlots(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Booking> bookings = new ArrayList<>();
        try (Connection conn = DbManager.getConnection()){
            // TODO: 22/10/2020 insert teacher column in database
            statement = conn.prepareStatement("" +
                    "select id corso insegnante databooked " +
                    "from prenotazioni " +
                    "where corso = ?");
            statement.setInt(1,subjectId);
            set = statement.executeQuery();
            while (set.next()){
                bookings.add(new Booking(set.getInt("id"),
                        set.getString("corso"),
                        set.getString("insegnante"),
                        set.getString("databooked")));
            }
            return bookings;
        }finally {
            DbManager.close(statement,set);
        }
        // throw httpexception?
    }
}

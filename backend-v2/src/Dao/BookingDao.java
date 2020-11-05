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

    public static ArrayList<Booking> getUserBookings(int userId) throws SQLException, NamingException {
        ArrayList<Booking> bookings = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            // TODO: 05/11/2020 rivedere il db
            statement = conn.prepareStatement(
                    "select prenotazione.id, corso.titolo, docente.nome, " +
                            "docente.cognome, utente.account,prenotazione.stato " +
                            "from prenotazione, corso, docente, utente " +
                            "where prenotazione.utente=utente.id " +
                            "and prenotazione.corso=corso.id " +
                            "and utente.id=? " +
                            "order by prenotazione.id desc");
            statement.setInt(1, userId);
            set = statement.executeQuery();
            while (set.next()){
                Booking booking = new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + " " +
                        set.getString("cognome"),
                        set.getString("account"),
                        set.getInt("stato"),
                        set.getString("data"),
                        set.getString("ora_inizio"));
                bookings.add(booking);
            }
            return bookings;
        }finally {
            DbManager.close(statement,set);
        }
    }

    public static ArrayList<Booking> getAllBookings() throws SQLException, NamingException {
        ArrayList<Booking> bookings = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "select prenotazione.id, corso.titolo, docente.nome, docente.cognome, " +
                            "utente.account, prenotazione.stato, slot.data, slot.ora_inizio " +
                            "from prenotazione, corso, docente, utente, slot " +
                            "where prenotazione.utente = utente.id " +
                            "and prenotazione.corso = corso.id " +
                            "and prenotazione.slot = slot.id " +
                            "and slot.docente = docente.id " +
                            "order by prenotazione.id desc");
            set = statement.executeQuery();
            while (set.next()){
                Booking booking = new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + " " +
                                set.getString("cognome"),
                        set.getString("account"),
                        set.getInt("stato"),
                        set.getString("data"),
                        set.getString("ora_inizio"));
                bookings.add(booking);
            }
            return bookings;
        }finally {
            DbManager.close(statement,set);
        }
    }

    // TODO: 05/11/2020 create method add booking
}

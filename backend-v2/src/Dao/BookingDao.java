package Dao;

import Exceptions.HttpException;
import Model.Booking;
import Model.Flags;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static Time.TimeUtility.getFirstDayOfWeek;
import static Time.TimeUtility.getLastDayOfWeek;

public class BookingDao {



    public static ArrayList<Booking> getTeacherBookedSlots(int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Booking> bookings = new ArrayList<>();
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "select pr.id, cs.titolo, u.account, d.nome, d.cognome, pr.datetime, pr.stato " +
                        "from ((prenotazione as pr  " +
                        "join corso as cs on pr.corso = cs.id) " +
                        "join docente as d on pr.docente = d.id) " +
                        "join utente as u on pr.utente = u.id " +
                        "where docente = ? " +
                        "order by pr.id desc ");
            statement.setInt(1,teacherId);
            set = statement.executeQuery();
            while (set.next()){
                bookings.add(new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + ""+ set.getString("cognome"),
                        set.getString("account"),
                        set.getInt("stato"),
                        set.getTimestamp("date")));
            }
            return bookings;
        }finally {
            DbManager.close(statement,set);
        }
        // throw httpexception?
    }



    // TODO: 14/11/2020 metodo che crea gli slot
    public static ArrayList<Booking> getWeeklyBookings() throws SQLException, NamingException {
        Instant start = Instant.now();
        ArrayList<Booking> bookings = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("select pr.id, cs.titolo, d.id, d.nome, d.cognome, pr.datetime, pr.stato " +
                                                "from ((prenotazione as pr "+
                                                "join corso as cs on pr.corso = cs.id)" +
                                                "join docente as d on pr.docente = d.id) " +
                                                "join utente as u on pr.utente = u.id " +
                                                "where datetime >= ? and datetime <= ?" +
                                                "order by pr.id desc ");
            statement.setTimestamp(1, Timestamp.valueOf(getFirstDayOfWeek()));
            statement.setTimestamp(2, Timestamp.valueOf(getLastDayOfWeek()));
            set = statement.executeQuery();
            while (set.next()){
                bookings.add( new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + " " +
                                set.getString("cognome"),
                        set.getInt(3),
                        set.getTimestamp("datetime").toLocalDateTime()));
            }
            return bookings;
        }finally {
            DbManager.close(statement,set);
            Instant end = Instant.now();
            System.out.print("getWeeklyBooking: duration : ");
            System.out.println(Duration.between(start,end));
        }

    }

    public static ArrayList<Booking> getUserBookings(int userId) throws SQLException, NamingException {
        ArrayList<Booking> bookings = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "select pr.id, cs.titolo, u.account, d.nome, d.cognome, pr.datetime, pr.stato " +
                            "from ((prenotazione as pr  " +
                            "join corso as cs on pr.corso = cs.id) " +
                            "join docente as d on pr.docente = d.id) " +
                            "join utente as u on pr.utente = u.id " +
                            "where utente = ?" +
                            "order by pr.id desc ");
            statement.setInt(1, userId);
            set = statement.executeQuery();
            while (set.next()){
                bookings.add( new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + " " +
                        set.getString("cognome"),
                        set.getString("account"),
                        set.getInt("stato"),
                        set.getTimestamp("data")));
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
                    "select pr.id, cs.titolo, u.account, d.nome, d.cognome, pr.datetime, pr.stato " +
                            "from ((prenotazione as pr  " +
                            "join corso as cs on pr.corso = cs.id) " +
                            "join docente as d on pr.docente = d.id) " +
                            "join utente as u on pr.utente = u.id " +
                            "order by pr.id desc ");
            set = statement.executeQuery();
            while (set.next()){
                bookings.add(new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + ""+ set.getString("cognome"),
                        set.getString("account"),
                        set.getInt("stato"),
                        set.getTimestamp("datetime")));
            }
            return bookings;
        }
        finally {
            DbManager.close(statement,set);
        }
    }

    public static Booking addBooking(int teacherId, int subjectId, LocalDateTime date, int userId) throws SQLException, NamingException {
        PreparedStatement statement= null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "INSERT into prenotazione values (DEFAULT, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, subjectId);
            statement.setInt(3, teacherId);
            statement.setInt(4, Flags.INITIALIZED);
            statement.setTimestamp(5, Timestamp.valueOf(date));
            if (statement.executeUpdate()>0){
                set = statement.getGeneratedKeys();
                if (set.next())
                    return getBooking(set.getInt(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Prenotazione gia' esistente");
            if(e.getMessage().contains("violates foreign key constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Corso o Slot non trovato");
        }finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
    }

    public static Booking getBooking(int bookingId) throws SQLException, NamingException {
        PreparedStatement statement= null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("select pr.id, cs.titolo, u.account, d.nome, d.cognome, pr.datetime, pr.stato " +
                    "from ((prenotazione as pr  " +
                    "join corso as cs on pr.corso = cs.id) " +
                    "join docente as d on pr.docente = d.id) " +
                    "join utente as u on pr.utente = u.id " +
                    "where pr.id = ?");
            statement.setInt(1, bookingId);
            set = statement.executeQuery();
            if (set.next())
                return new Booking(set.getInt("id"),
                        set.getString("titolo"),
                        set.getString("nome") + ""+ set.getString("cognome"),
                        set.getString("account"),
                        set.getInt("stato"),
                        set.getTimestamp("datetime"));
        }finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Prenotazione non Trovata");
    }

    public static void setBookingStatus(int bookingId, int flagStatus) throws SQLException, NamingException {
        PreparedStatement statement= null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("update prenotazione set stato = ? where id=?");
            statement.setInt(1, flagStatus);
            statement.setInt(2, bookingId);
            if (statement.executeUpdate()>0)
                return;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Prenotazione non trovata");
    }

    public static Booking setUserBookingStatus(int bookingId, int userId, int flagStatus) throws SQLException, NamingException {
        PreparedStatement statement= null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("update prenotazione set stato = ? where id=? and utente=?");
            statement.setInt(1, flagStatus);
            statement.setInt(2, bookingId);
            statement.setInt(3, userId);
            if (statement.executeUpdate()>0)
                    return getBooking(bookingId);
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Prenotazione non trovata");
    }
}

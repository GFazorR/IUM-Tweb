package Dao;

import Exceptions.HttpException;
import Model.Booking;
import Model.Subject;
import Model.Teacher;
import Model.User;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// TODO: 14/10/2020 add queries

public class Dao {
    // TODO: 15/10/2020 Subject Queries
//    gets all courses from database
    public static ArrayList<Subject> getAllSubjects() throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Subject> cours = new ArrayList<>();
//        DbManager.registerDriver();
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("select corso.id, corso.titolo " +
                    "from corso " +
                    "where corso.deleted=FAlSE");
            set = statement.executeQuery();
            while(set.next()){
                ArrayList<Teacher> teachers = getAllSubjectTeachers(set.getInt("id"));
                Subject subject = new Subject(set.getInt("id"),
                        set.getString("titolo"), teachers);
                cours.add(subject);
            }
            return cours;
        }finally {
            DbManager.close(statement,set);
        }
    }

//    Inserts subject in database
    public static Subject getSubject(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("select corso.id, corso.titolo " +
                    "from corso " +
                    "where corso.id=? " +
                    "and corso.deleted=FALSE");
            statement.setInt(1,subjectId);
            set = statement.executeQuery();
            if (set.next())
                return new Subject(set.getInt("id"),
                        set.getString("titolo"),
                        Dao.getAllSubjectTeachers(set.getInt("id")));

        }finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Corso non trovato");
    }

//    Inserts Course into Database
    public static Subject setSubject(String subject) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("INSERT into corso " +
                    "values (DEFAULT , ?, DEFAULT )", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, subject);
            if(statement.executeUpdate() > 0){
                set = statement.getGeneratedKeys();
                if (set.next())
                    return getSubject(set.getInt(1));
            }
        }finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_CONFLICT,"Corso già esistente");
    }

//  sets Subject Delete to true
    public static void setSubjectDeleted (int subjectId) throws SQLException, NamingException {
        PreparedStatement statement=null;
        try(Connection connection = DbManager.getConnection()){
            statement= connection.prepareStatement("UPDATE corso " +
                    "set deleted=TRUE where id=? and deleted=FALSE");
            statement.setInt(1,subjectId);
            if(statement.executeUpdate() > 0){
                return;
            }
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso non trovato");
    }

//  deletes entry from database (for testing only)
    public static void deleteSubject(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("DELETE from corso where corso.id=?");
            statement.setInt(1,subjectId);
            if(statement.executeUpdate()>0)
                return;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Corso non Trovato");
    }

//  inserts the association teacher-subject into database
    // TODO: 15/10/2020 add Teaching Queries
    public static void addTeaching(int subjectId, int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("INSERT into corso_docente " +
                    "values (DEFAULT ,?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,subjectId);
            statement.setInt(2,teacherId);
            if (statement.executeUpdate()>0)
                return ;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Server Error");
    }

//  deletes teaching entry from database
    public static void deleteTeaching(int subjectId, int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("DELETE from corso_docente " +
                    "where corso=? and docente=?",Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,subjectId);
            statement.setInt(2,teacherId);
            if (statement.executeUpdate()>0)
                return;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Il corso "+
                getSubject(subjectId).getName()+ " non è insegnato da " +
                getTeacher(teacherId).getName());
    }


    // TODO: 14/10/2020 add Teachers Queries
    // TODO: 15/10/2020 add Test getAllTeachers
    public static ArrayList<Teacher> getAllTeachers() throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Teacher> teachers = new ArrayList<>();
//        DbManager.registerDriver();
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "select docente.id, docente.nome, docente.cognome\n" +
                    "from docente " +
                    "where docente.deleted=FALSE");
            set = statement.executeQuery();
            while (set.next()){
                Teacher teacher = new Teacher(set.getInt("id"),
                        set.getString("nome")+ " "+
                                set.getString("cognome"));
                teachers.add(teacher);
            }
            return teachers;
        }finally {
            DbManager.close(statement,set);
        }
    }
//  gets all teachers from database
    public static ArrayList<Teacher> getAllSubjectTeachers(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Teacher> teachers = new ArrayList<>();
//        DbManager.registerDriver();
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("select docente.id, docente.nome, docente.cognome\n" +
                    "from docente,corso_docente\n" +
                    "where docente.deleted=FALSE\n" +
                    "and docente.id=corso_docente.docente " +
                    "and corso_docente.corso=?");
            statement.setInt(1,subjectId);
            set = statement.executeQuery();
            while (set.next()){
                Teacher teacher = new Teacher(set.getInt("id"),
                        set.getString("nome")+ " "+
                        set.getString("cognome"));
                teachers.add(teacher);
            }
            return teachers;
        }finally {
            DbManager.close(statement,set);
        }
    }

    // TODO: 15/10/2020 add Test getTeacher
    private static Teacher getTeacher(int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("select docente.id, docente.nome, docente.cognome " +
                    "from docente " +
                    "where docente.id=? and docente.deleted=FALSE");
            statement.setInt(1,teacherId);
            set = statement.executeQuery();
            if (set.next())
                return new Teacher(set.getInt("id"),
                        set.getString("nome")+" "+set.getString("cognome"));
        }finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Professore non trovato");
    }

//    adds a new teacher in Database
    public static int addTeacher(String name, String surname) throws NamingException {
        PreparedStatement statement=null;
        ResultSet set=null;
        try(Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "INSERT into docente values (DEFAULT ,? , ?,DEFAULT )",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,name);
            statement.setString(2,surname);
            if(statement.executeUpdate() > 0){
                set = statement.getGeneratedKeys();
                if(set.next())
                    return set.getInt("id");
            }
        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint")){
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Professore gia' inserito");
            }
        } finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del Server");
    }

//    sets Teacher deleted field to false
    public static void setTeacherDeleted(int teacherID) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("UPDATE docente set deleted=TRUE where id=?");
            if (statement.executeUpdate()>0)
                return;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Professore non trovato");
    }

//  deletes teacher entry from database (for Testing only)
    public static void deleteTeacher(int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("DELETE from docente where id=?");
            statement.setInt(1,teacherId);
            if (statement.executeUpdate()>0)
                return;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Professore non trovato");
    }

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
    // TODO: 14/10/2020 add User Queries

    public static void removeUserToken(String token) throws SQLException,NamingException {
        PreparedStatement s = null;
        try (Connection c = DbManager.getConnection()) {
            int result = 0;
            s = c.prepareStatement("DELETE FROM \"public\".\"session_user\" WHERE \"token\" = ?");
            s.setString(1, token);
            result = s.executeUpdate();
            if (result > 0)
                return;

        } finally {
            DbManager.close(s, null);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del server");
    }

        public static User getUserFromToken(String token) throws SQLException, NamingException {
        PreparedStatement statement=null;
        ResultSet set =null;
        try(Connection c = DbManager.getConnection()){
            statement = c.prepareStatement("select utente.*,\"session_user\".*\n" +
                    "from utente,\"session_user\"\n" +
                    "where utente.id=\"session_user\".user\n" +
                    "and \"session_user\".token=? ");
            statement.setString(1,token);
            set = statement.executeQuery();
            if (set.next()) {
                java.util.Date now= Calendar.getInstance().getTime();
                Date genereted=new java.util.Date(set.getTimestamp("genereted").getTime());
                long from=(TimeUnit.MINUTES.convert(Math.abs(now.getTime()-genereted.getTime()),
                        TimeUnit.MILLISECONDS));

                int ttl=Integer.parseInt(DbManager.getTime());
                if(ttl>from)
                    return new User(set.getInt("id"),
                            set.getString("account"),
                            set.getBoolean("isAdmin"),
                            set.getString("token"));
                else
                    removeUserToken(token);
            }
        } finally {
            DbManager.close(statement,set);
        }
        return null;
    }

    public static User getUser(String username, String password) throws SQLException, NamingException {
        PreparedStatement statement= null;
        ResultSet set=null;
        try(Connection c = DbManager.getConnection()){
            statement = c.prepareStatement(
                    "select * from utente where account= ? and password = ? ");
            statement.setString(1, username);
            statement.setString(2, password);
            set = statement.executeQuery();
            if(set.next()) {
                return new User(set.getInt("id"),
                        set.getString("account"),
                        set.getBoolean("isAdmin"),
                        null);
            }
        } finally { DbManager.close(statement,set); }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Utente o password errati");
    }

    public static void insertUserToken(int id, String token) throws NamingException {
            PreparedStatement statement=null;
            try(Connection c = DbManager.getConnection()){
                statement = c.prepareStatement(
                        "INSERT into \"session_user\" values (? , ?,DEFAULT,DEFAULT )");
                statement.setInt(1,id);
                statement.setString(2,token);
                if(statement.executeUpdate()>0)
                    return;
            } catch (SQLException e) {
                if(e.getMessage()
                        .contains("ERROR: duplicate key value violates unique constraint"))
                    throw new HttpException(HttpServletResponse.SC_CONFLICT, "Token gia' esistente");
            }
            finally {
                DbManager.close(statement,null);
            }
            throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore da parte del server");
    }
}

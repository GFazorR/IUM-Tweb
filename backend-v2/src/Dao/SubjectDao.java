package Dao;

import Exceptions.HttpException;
import Model.Subject;
import Model.Teacher;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class SubjectDao {

    //    gets all courses from database
    public static ArrayList<Subject> getAllSubjects() throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Subject> cours = new ArrayList<Subject>();
//        DbManager.registerDriver();
        try (Connection conn = DbManager.getConnection()) {
            statement = conn.prepareStatement("select corso.id, corso.titolo " +
                    "from corso " +
                    "where corso.deleted=FAlSE");
            set = statement.executeQuery();
            while (set.next()) {
                ArrayList<Teacher> teachers = TeacherDao.getAllSubjectTeachers(set.getInt("id"));
                Subject subject = new Subject(set.getInt("id"),
                        set.getString("titolo"), teachers);
                cours.add(subject);
            }
            return cours;
        } finally {
            DbManager.close(statement, set);
        }
    }//    Inserts subject in database

    public static Subject getSubject(int subjectId) throws SQLException, NamingException {
        Instant start = Instant.now();
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()) {
            statement = conn.prepareStatement("select corso.id, corso.titolo " +
                    "from corso " +
                    "where corso.id=? " +
                    "and corso.deleted=FALSE");
            statement.setInt(1, subjectId);
            set = statement.executeQuery();
            if (set.next())
                return new Subject(set.getInt("id"),
                        set.getString("titolo"),
                        TeacherDao.getAllSubjectTeachers(set.getInt("id")));

        } finally {
            DbManager.close(statement, set);
            Instant end = Instant.now();
            System.out.print("getSubject: duration : ");
            System.out.println(Duration.between(start,end));
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso non trovato");
    }//    Inserts Course into Database

    public static Subject setSubject(String subject) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()) {
            statement = conn.prepareStatement("INSERT into corso " +
                    "values (DEFAULT , ?, DEFAULT )", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, subject);
            if (statement.executeUpdate() > 0) {
                set = statement.getGeneratedKeys();
                if (set.next())
                    return getSubject(set.getInt(1));
            }
        } finally {
            DbManager.close(statement, set);
        }
        throw new HttpException(HttpServletResponse.SC_CONFLICT, "Corso già esistente");
    }//  sets Subject Delete to true

    public static void setSubjectDeleted(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection connection = DbManager.getConnection()) {
            statement = connection.prepareStatement("UPDATE corso " +
                    "set deleted=TRUE where id=? and deleted=FALSE");
            statement.setInt(1, subjectId);
            if (statement.executeUpdate() > 0) {
                return;
            }
        } finally {
            DbManager.close(statement, null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso non trovato");
    }//  deletes entry from database (for testing only)

    public static void deleteSubject(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()) {
            statement = conn.prepareStatement("DELETE from corso where corso.id=?");
            statement.setInt(1, subjectId);
            if (statement.executeUpdate() > 0)
                return;
        } finally {
            DbManager.close(statement, null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso non Trovato");
    }
}
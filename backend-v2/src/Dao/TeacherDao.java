package Dao;

import Exceptions.HttpException;
import Model.Teacher;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class TeacherDao {
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
    public static Teacher getTeacher(int teacherId) throws SQLException, NamingException {
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
    public static Teacher addTeacher(String name, String surname) throws NamingException {
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
                    return getTeacher(set.getInt("id"));
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
}

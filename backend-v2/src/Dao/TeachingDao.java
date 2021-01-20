package Dao;

import Exceptions.HttpException;
import Model.Subject;
import Model.Teacher;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class TeachingDao {
    //  inserts the association teacher-subject into database

    public static ArrayList<Teacher> getAvailableTeachers(int subjectId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        ArrayList<Teacher> teachers = new ArrayList<>();
        try {
            Connection conn = DbManager.getConnection();
            statement = conn.prepareStatement("SELECT id, nome, cognome " +
                    "FROM docente " +
                    "WHERE deleted = false and id NOT IN (select te.id " +
                    "from docente as te join corso_docente as cd on te.id = cd.docente " +
                    "where cd.corso = ?)");
            statement.setInt(1,subjectId);
            set=statement.executeQuery();
            while (set.next()){
                teachers.add(new Teacher(set.getInt("id"), set.getString("nome")+ " " + set.getString("cognome")));
            }
            return teachers;
        }finally {
            DbManager.close(statement,set);
        }
    }

    public static Subject addTeaching(int subjectId, int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("INSERT into corso_docente " +
                    "values (DEFAULT ,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,subjectId);
            statement.setInt(2,teacherId);
            if (statement.executeUpdate()>0)
                return SubjectDao.getSubject(subjectId);
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Server Error");
    }

    //  deletes teaching entry from database
    public static void deleteTeaching(int subjectId, int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "DELETE from corso_docente " +
                    "where corso=? and docente=?",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,subjectId);
            statement.setInt(2,teacherId);
            if (statement.executeUpdate()>0)
                return;
        }finally {
            DbManager.close(statement,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND,"Il corso "+
                SubjectDao.getSubject(subjectId).getName()+ " non è insegnato da " +
                TeacherDao.getTeacher(teacherId).getName());
    }


    public static int getTeachingId(int subjectId, int teacherId) throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet set = null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement(
                    "select id " +
                        "from corso_docente " +
                        "where docente = ? and corso = ?");
            statement.setInt(1,teacherId);
            statement.setInt(2,subjectId);
            set = statement.executeQuery();
            if (set.next())
                return set.getInt("id");
        }finally {
            DbManager.close(statement,set);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Server Error");
    }
}

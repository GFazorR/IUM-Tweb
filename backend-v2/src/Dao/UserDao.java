package Dao;

import Exceptions.HttpException;
import Model.User;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class UserDao {
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

    public static User getUser(String userName, String password) throws SQLException, NamingException {
        PreparedStatement statement= null;
        ResultSet set=null;
        try(Connection c = DbManager.getConnection()){
            statement = c.prepareStatement(
                    "select * from utente where account= ? and password = ? ");
            statement.setString(1, userName);
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

    public static User getUser(int userId) throws SQLException, NamingException {
        PreparedStatement statement= null;
        ResultSet set= null;
        User user = null;
        try(Connection conn = DbManager.getConnection()) {
            statement = conn.prepareStatement("select  * from utente where id=?");
            statement.setInt(1,userId);
            set = statement.executeQuery();
            while(set.next()){
                user = new User(set.getInt("id"),
                        set.getString("account"),
                        set.getBoolean("isAdmin"),
                        null);
            }
        }finally{
            DbManager.close(statement,set);
        }
        return user;
    }

    public static User insertUser(String userName, String password) throws NamingException {
        PreparedStatement statement= null;
        ResultSet set= null;
        try (Connection conn = DbManager.getConnection()){
            statement = conn.prepareStatement("INSERT into utente " +
                    "values (DEFAULT , ? ,? ,DEFAULT )",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userName);
            statement.setString(2, password);

            if(statement.executeUpdate()>0){
                set = statement.getGeneratedKeys();
                if(set.next())
                    return getUser(set.getInt(1));
            }
            statement.close();
        }catch (SQLException e){
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "User already Exists");
        }finally {
            DbManager.close(statement, set);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
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

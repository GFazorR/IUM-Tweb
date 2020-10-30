package Auth;

import Exceptions.HttpException;
import Model.User;
import Dao.*;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class Auth {
    public static User authUser(String token) throws SQLException, NamingException {
        // TODO: 19/10/2020 creare metodi user in Dao
        User user= Dao.getUserFromToken(token);
        if(user == null)
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Non autorizzato");
        return  user;
    }

    public static User authAdmin(String token) throws SQLException, NamingException {
        User user = authUser(token);

        if(!user.isAdmin())
            throw new HttpException(HttpServletResponse.SC_FORBIDDEN, "Non permesso");
        return  user;
    }
}

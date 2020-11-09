package Servlets;

import Auth.Auth;
import Dao.DbManager;
import Dao.UserDao;
import Model.User;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

// TODO: 04/11/2020 add comments
@WebServlet(name = "Logout", urlPatterns = "/api/Logout")
public class Logout extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = req.getParameter("token");
        try {
            User user = Auth.authUser(token);
            UserDao.removeUserToken(user.getToken());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }
}

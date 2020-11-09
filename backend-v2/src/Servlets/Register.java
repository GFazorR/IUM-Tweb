package Servlets;

import Dao.DbManager;
import Dao.UserDao;
import Exceptions.HttpException;
import Model.User;
import com.google.gson.Gson;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// TODO: 04/11/2020 add comments
@WebServlet(name = "Register", urlPatterns = "/api/Register")
public class Register extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        PrintWriter out = resp.getWriter();
        try {
            if (username == null || password == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                        "Username or Password not provided");
            User user = UserDao.insertUser(username,password);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(new Gson().toJson(user));
            out.flush();
        } catch (NamingException throwables) {
            throw new ServletException(throwables);
        }
    }
}

package Servlets;

import Auth.Token;
import Dao.UserDao;
import Dao.DbManager;
import Exceptions.HttpException;
import Model.User;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


// TODO: 04/11/2020 add comments

@WebServlet(name = "Login", urlPatterns = "/api/Login")
public class Login extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    // takes username and password from request, crates new token,
    // return as response username object to Json
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        PrintWriter out = resp.getWriter();

        try {
            if(username == null|| password == null ||
                    username.isEmpty() || password.isEmpty())
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                        "Utente o Password non fornite");
            User user = UserDao.getUser(username,password);
            String token = Token.generateToken("" + user.getId(), user.getUsername());
            user.setToken(token);
            UserDao.insertUserToken(user.getId(),user.getToken());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.println(new Gson().toJson(user));
            out.flush();

        }catch (Exception e){
            throw new ServletException(e);
        }
    }
}

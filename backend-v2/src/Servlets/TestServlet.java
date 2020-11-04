package Servlets;

import Dao.UserDao;
import Dao.DbManager;
import Model.User;
import com.google.gson.Gson;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "TestServlet", urlPatterns = {"/api/TestServlet"})
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        try {
            out = response.getWriter();
            DbManager.registerDriver();
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyMSIsImp0aSI6IjIiLCJpYXQiOjE2MDA3MjM5MDZ9.BEu5NytU4lDK1MH1pc2BPRu_V6SpmujQrubYAXXOIm8";
            User user = UserDao.getUserFromToken(token);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            String output = new Gson().toJson(user);
            out.println(output);

        } catch (IOException | SQLException | ServletException | NamingException e) {
            e.printStackTrace();
        } finally {
            assert out != null;
            out.close();
        }
    }
}

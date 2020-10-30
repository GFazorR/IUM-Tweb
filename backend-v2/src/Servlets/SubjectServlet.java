package Servlets;

import Dao.Dao;
import Dao.DbManager;
import Model.Subject;
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
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "Subjects" , urlPatterns = {"/api/Subjects"})
public class SubjectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()){
            ArrayList<Subject> subjects = Dao.getAllSubjects();
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            out.println(new Gson().toJson(subjects));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }
}

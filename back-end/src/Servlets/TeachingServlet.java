package Servlets;

import Auth.Auth;
import Dao.DbManager;
import Dao.TeachingDao;
import Exceptions.HttpException;
import Model.Subject;
import Model.Teacher;
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

// TODO: 06/11/2020 add comments and refactor
@WebServlet(name = "Teaching", urlPatterns = "/api/Teaching")
public class TeachingServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String subjectId = req.getParameter("subject");
        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        try {
            Auth.authAdmin(token);
            if(subjectId==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                        "Subject not provided");
            ArrayList<Teacher> teachers = TeachingDao.getAvailableTeachers(Integer.parseInt(subjectId));
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.println(new Gson().toJson(teachers));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String subjectId = req.getParameter("subject");
        String teacherId = req.getParameter("teacher");
        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        try{
            Auth.authAdmin(token);
            if(subjectId==null || teacherId==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                        "Teacher or Subject not provided");

            Subject subject= TeachingDao.addTeaching(
                    Integer.parseInt(subjectId),
                    Integer.parseInt(teacherId));
            resp.setStatus(HttpServletResponse.SC_CREATED);

            out.println(new Gson().toJson(subject));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String subjectid = req.getParameter("subject");
        String teacherId = req.getParameter("teacher");
        String token = req.getParameter("token");
        try {
            Auth.authAdmin(token);
            if(subjectid == null || teacherId == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Theacher or Subject not provided");

            TeachingDao.deleteTeaching(Integer.parseInt(subjectid),Integer.parseInt(teacherId));
        } catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }
    }


}

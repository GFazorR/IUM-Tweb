package Servlets;

import Auth.Auth;
import Dao.DbManager;
import Dao.SubjectDao;
import Exceptions.HttpException;
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
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()){
            ArrayList<Subject> subjects = SubjectDao.getAllSubjects();
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            out.println(new Gson().toJson(subjects));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String token = req.getParameter("token");
        String subjectName = req.getParameter("subject");
        PrintWriter out = resp.getWriter();
        try {
            Auth.authAdmin(token);
            if (subjectName == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                        "Name of subject not provided");
            Subject subject = SubjectDao.setSubject(subjectName);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");

            out.println(new Gson().toJson(subject));
            out.flush();

        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = req.getParameter("token");
        String id = req.getParameter("id");
        try {
            Auth.authAdmin(token);
            if (id == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,"Id not provided");

            SubjectDao.setSubjectDeleted(Integer.parseInt(id));
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }


}

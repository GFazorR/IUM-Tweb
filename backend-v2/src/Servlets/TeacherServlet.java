package Servlets;

import Auth.Auth;
import Dao.DbManager;
import Dao.TeacherDao;
import Exceptions.HttpException;
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

// TODO: 04/11/2020 add comments and refactor
@WebServlet(name = "Teachers", urlPatterns = "/api/Teachers")
public class TeacherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        try {
            Auth.authAdmin(token);
            ArrayList<Teacher> teachers = TeacherDao.getAllTeachers();
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(new Gson().toJson(teachers));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        try {
            Auth.authAdmin(token);
            // TODO: 04/11/2020 replace with cases
            if (name == null || surname == null || token == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Name Surname or token not provided");
            Teacher teacher = TeacherDao.addTeacher(name, surname);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(new Gson().toJson(teacher));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String id = req.getParameter("id");
        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        System.out.println(id);
        try {
            Auth.authAdmin(token);
            if (id == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,"Id not provided");
            TeacherDao.setTeacherDeleted(Integer.parseInt(id));
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }
}

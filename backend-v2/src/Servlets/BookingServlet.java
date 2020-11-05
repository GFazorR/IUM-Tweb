package Servlets;

import Auth.Auth;
import Dao.BookingDao;
import Dao.DbManager;
import Model.Booking;
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
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: 05/11/2020 add comments and refactor

@WebServlet(name = "Bookings", urlPatterns = "api/Bookings")
public class BookingServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: 22/10/2020 create methods get booking slot from subject
        resp.setContentType("application/json");
        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        ArrayList<Booking> bookings;
        try {
            User user = Auth.authUser(token);
            if (user.isAdmin())
                bookings = BookingDao.getAllBookings();
            else
                bookings = BookingDao.getUserBookings(user.getId());

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.println(new Gson().toJson(bookings));
            out.flush();


        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = req.getParameter("token");
        String subject = req.getParameter("subject");
    }
}

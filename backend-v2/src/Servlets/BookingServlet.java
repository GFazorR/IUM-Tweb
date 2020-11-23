package Servlets;

import Auth.Auth;
import Dao.BookingDao;
import Dao.DbManager;
import Exceptions.HttpException;
import Model.Booking;
import Model.Flags;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

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
import java.time.LocalDateTime;
import java.util.ArrayList;

// TODO: 05/11/2020 add comments and refactor

@WebServlet(name = "Bookings", urlPatterns = "/api/Bookings")
public class BookingServlet extends HttpServlet {

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
        String teacher = req.getParameter("teacher");
        String date = req.getParameter("date");
        PrintWriter out = resp.getWriter();
        try {
            User user = Auth.authUser(token);
            if(subject == null || date == null || date == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                        "Subject, teacher or date not provided");
            LocalDateTime dateTime = LocalDateTime.parse(date);
            Booking booking = BookingDao.addBooking(Integer.parseInt(teacher),
                    Integer.parseInt(subject), dateTime, user.getId());

            Gson localDateTimeSerializer = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext)
                            -> new JsonPrimitive(localDateTime.toString())).create();

            String gson = localDateTimeSerializer.toJson(booking);

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);

            out.println(gson);
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = req.getParameter("token");
        String bookingId = req.getParameter("id");
        try {
            User user = Auth.authUser(token);
            if (bookingId == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Booking id Not Provided");
            if (user.isAdmin()){
                BookingDao.setBookingStatus(Integer.parseInt(bookingId), Flags.CANCELLED);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            }else {
                BookingDao.setUserBookingStatus(Integer.parseInt(bookingId), user.getId(), Flags.CANCELLED);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            }
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = req.getParameter("token");
        String bookingId = req.getParameter("id");
        PrintWriter out = resp.getWriter();

        try {
            User user = Auth.authUser(token);
            if (bookingId == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "BookingId not provided");
            Booking booking = BookingDao.setUserBookingStatus(Integer.parseInt(bookingId),user.getId(),Flags.CONFIRMED);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.println(new Gson().toJson(booking));
            out.flush();
        } catch (SQLException | NamingException throwables) {
            throw new ServletException(throwables);
        }
    }
}

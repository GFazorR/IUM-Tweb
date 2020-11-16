package Servlets;

import Dao.DbManager;
import Model.Slot;
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
import java.time.Duration;
import java.time.Instant;

@WebServlet(name = "Slot", urlPatterns = "/api/Slot")
public class SlotServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
//        int subjectId= Integer.parseInt(req.getParameter("subjectId"));
        PrintWriter out = resp.getWriter();

        try {
            Instant start = Instant.now();
            out.write(new Gson().toJson(Slot.Dao.weeklySubjectCalendar(35)));
            out.flush();
            Instant end = Instant.now();
            System.out.print("doGet: duration: ");
            System.out.println(Duration.between(start,end));
        } catch (SQLException | NamingException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DbManager.registerDriver();
    }
}

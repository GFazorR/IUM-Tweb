package Servlets;

import Dao.DbManager;
import Model.Slot;
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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@WebServlet(name = "Slot", urlPatterns = "/api/Slot")
public class SlotServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int subjectId= Integer.parseInt(req.getParameter("subjectId"));
        PrintWriter out = resp.getWriter();

        try {
            Instant start = Instant.now();
            Gson localDateTimeSerializer = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext)
                            -> new JsonPrimitive(localDateTime.toString())).create();
            String gson = localDateTimeSerializer.toJson(Slot.Dao.weeklySubjectCalendar(subjectId));
            out.write(gson);
            out.flush();
            Instant end = Instant.now();
            System.out.print("doGet: duration: ");
            System.out.println(Duration.between(start,end));
            System.out.println(gson);
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

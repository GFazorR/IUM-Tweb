package Servlets;

import Exceptions.HttpException;
import Model.ErrorMessage;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Error", urlPatterns = "/api/Error")
public class Error extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Exception e = (Exception) req.getAttribute("javax.servlet.error.exception");

        int exceptionCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = e.getMessage();

        if (e instanceof HttpException){
            HttpException m = (HttpException) e;
            exceptionCode = m.getCode();
        }

        resp.setStatus(exceptionCode);
        resp.getWriter().println(new Gson().toJson(new ErrorMessage(message)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}

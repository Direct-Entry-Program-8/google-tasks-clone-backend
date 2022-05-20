package lk.ijse.dep8.tasks.api;

import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "TaskListServlet")
public class TaskListServlet extends HttpServlet2 {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().startsWith("application/json")){
            throw new ResponseStatusException(415, "Invalid content type or content type is empty");
        }

        String pattern = "/users/[A-Fa-f0-9\\-]{36}/lists/?";
        if (!req.getPathInfo().matches(pattern)){
            throw new ResponseStatusException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid end point for POST request");
        }
    }
}

import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

//Takes a json object with user and pass keys as input
@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override // create account
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("BODY: " + body);
        Map<String,?> map = new Gson().fromJson(body, Map.class);

        Object o1,o2;

        o1 = map.get("user");
        o2 = map.get("pass");

        if(o1 ==null || o2 == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String user = o1.toString();
        String pass = o2.toString();

        try {
            if (!UserModel.post(user, pass))
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username taken");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        req.getSession().setAttribute("user", user);
    }

    @Override  //login
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String,?> map = new Gson().fromJson(body, Map.class);

        Object o1,o2;

        o1 = map.get("user");
        o2 = map.get("pass");

        if(o1 ==null || o2 == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String user = o1.toString();
        String pass = o2.toString();

        if(user==null || pass == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UserModel.put(user, pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        System.out.println("LOGIN USER " + user);
        req.getSession().setAttribute("user", user);
    }
}


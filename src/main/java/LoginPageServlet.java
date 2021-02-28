import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
//Can be trimmed down
//Will include better auth for viewing private/public sheets
// and editing owned sheets
@WebServlet("/loginPage")
public class LoginPageServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HERE");
        if(req.getSession().getAttribute("user")!= null) {
            System.out.println(req.getSession().getAttribute("user"));
            resp.sendRedirect("home");
        }
        var writer = resp.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html lang=\"en\"");
        writer.println("<head>");
        writer.println(" <meta charset=\"UTF - 8\">");
        writer.println("<title>Character Sheet Login</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h2>Login</h2>");
        writer.println("<form method=\"get\" action=\"login\">");
        writer.println("Username:");
        writer.println("<input type=\"text\" name=\"username\">");
        writer.println("<br>");
        writer.println("<br>");
        writer.println("Password:");
        writer.println("<input type=\"password\" name=\"password\">");
        writer.println("<input type=\"submit\" value=\"Submit\">");
        writer.println("</form>");
        if(req.getAttribute("valid")!=null && req.getAttribute("valid").equals("false")) {
            writer.println("<h2>Invalid username or password.</h2>");
        }
        writer.println("</body></html>");
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession().getAttribute("user")!= null)
            resp.sendRedirect("home");
        if(!Files.exists(Path.of("users.txt")))
            Files.createFile(Path.of("users.txt")); //temporary
        File file = new File("users.txt");
        Scanner s = new Scanner(file);
        while(s.hasNext()) {
            if(s.nextLine().equals(req.getParameter("username")))
                break;
        }
        if(s.hasNext() && s.nextLine().equals(req.getParameter("password"))) {
            req.getSession().setAttribute("user",req.getParameter("username"));
            resp.sendRedirect("home");
        }
        else {
            RequestDispatcher rd = req.getRequestDispatcher("loginPage");
            req.setAttribute("valid","false");
            rd.forward(req,resp);
        }
        s.close();
        System.out.println("VALID LOGIN");
    }
}


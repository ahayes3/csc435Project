import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession().getAttribute("user")==null)
            resp.sendRedirect("/loginPage");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Hello, World</title></head>");
        out.println("<body>");
        out.println("<center><h1>Character Sheets</h1></center>");
        out.println("<div id=\"characters\">");
        if(req.getSession().getAttribute("characters")!=null) {
            ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");
            characters.forEach(p -> out.println("<a href=\"character?id=" + p.id + "\">" + p.name + "</a>"));
        }
        out.println("</div>");
        out.println("<a href=\"create\"><button type=\"button\">Create New</button></a>");
        out.println("</body></html>");
        out.close();
    }
}
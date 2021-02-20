import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Hello, World</title></head>");
        out.println("<body>");
        out.println("<center><h1>Character Sheets</h1></center>");
        out.println("<a href=\"create\"><button type=\"button\">Create New</button></a>");
        //todo Character sheets will be linked here

        out.println("</body></html>");
        out.close();
    }
}
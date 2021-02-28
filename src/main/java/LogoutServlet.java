import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//NOT NEEDED
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var attrs = req.getSession().getAttributeNames();
        while(attrs.hasMoreElements()) {
            req.getSession().removeAttribute(attrs.nextElement());
        }
        resp.sendRedirect("loginPage");
    }
}

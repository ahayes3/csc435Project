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
import java.util.Scanner;
//Can be trimmed down
//Will include better auth for viewing private/public sheets
// and editing owned sheets
@WebServlet("/loginPage")
public class LoginPageServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        File users = new File("users.txt");  //plain text passwords :)
        if(users.exists()) {
            users.delete();
            try {
                users.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // create account
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File file = new File("users.txt");
        if(req.getSession().getAttribute("user")!= null) {
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
        }
        String user = req.getParameter("user");
        String pass = req.getParameter("pass");

        Scanner s = new Scanner(file);
        int i=0;
        while(s.hasNextLine()) {
            if(s.nextLine().equals(user) && i%2==0) {
                resp.sendError(HttpServletResponse.SC_CONFLICT,"Username "+ user+" already exists.");
                return;
            }
            i++;
        }
        FileWriter f = new FileWriter(file);
        f.append(user).append('\n');
        f.append(pass).append('\n');
        req.getSession().setAttribute("user",user);
        f.flush();
        f.close();

        s.close();
    }

    @Override  //login
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(req.getSession().getAttribute("user")!= null) {
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
        }
        String user = req.getParameter("user");
        String pass =req.getParameter("pass");
        Scanner s = new Scanner(new File("users.txt"));
        while(s.hasNextLine()) {
            String line = s.nextLine();
            if(line.equals(user) && s.nextLine().equals(pass)) {
                req.getSession().setAttribute("user",user);
                System.out.println("Valid login");
                return;
            }
            else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}


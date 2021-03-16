import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginView {
    public static void respond(boolean successful, HttpServletResponse resp) throws IOException {
        if(successful) {
            resp.setStatus(HttpServletResponse.SC_CONTINUE);
        }
        else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
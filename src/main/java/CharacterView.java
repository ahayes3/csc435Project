import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

//JSON view
public class CharacterView {
    static Gson gson = new Gson();
    public static void respond(Character c, HttpServletResponse resp) throws IOException {
        PrintWriter out = setup(resp);
        out.print(gson.toJson(c));
        out.flush();
        out.close();
    }
    public static void respond(HashMap<UUID,Character> c, HttpServletResponse resp) throws IOException {
        PrintWriter out = setup(resp);
        out.print(gson.toJson(c));
        out.flush();
        out.close();
    }
    public static void respond(Collection<UUID> coll, HttpServletResponse resp) throws IOException {
        PrintWriter out = setup(resp);
        out.print(gson.toJson(coll));
    }

    private static PrintWriter setup(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        return resp.getWriter();
    }
}

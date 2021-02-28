import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebFilter(urlPatterns = {"/characters/*"})
public class CharacterFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getMethod().equals("POST")) {
            chain.doFilter(request,response);
            return;
        }
        HttpServletResponse resp = (HttpServletResponse) response;
        ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");
        if(characters==null) {
            System.out.println("NO CHARACTERS FOUND");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        else {
            characters.forEach(p -> System.out.println(p.id));
        }


        var split = req.getRequestURI().split("/");
        int id = Integer.parseInt(split[split.length-1]);
        System.out.println("ID: "+id);
        if(characters.stream().anyMatch(p -> p.id==id)) {
            Character c = characters.stream().filter(p -> p.id == id).findFirst().get();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(c));
            out.flush();
            out.close();
        }
        //chain.doFilter(req,resp);

    }
}

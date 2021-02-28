import com.google.gson.Gson;

import javax.resource.spi.InvalidPropertyException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@WebFilter(urlPatterns = {"/characters/*"})
public class CharacterFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        switch (req.getMethod()) {
            case "POST" -> chain.doFilter(request, response);
            case "PUT" -> {
                int id = getId(req.getRequestURI());
                ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");

                if (characters == null || characters.stream().noneMatch(p -> p.id == id))
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character ID not found");
                else {
                    Character character = characters.stream().filter(p -> p.id == id).findFirst().get();

                    var iter = req.getParameterNames().asIterator();
                    boolean classChanged = false;
                    while (iter.hasNext()) {
                        String s = iter.next();
                        if (s.contains("class")) {
                            if (!classChanged) {
                                character.classes.clear();
                                classChanged = true;
                            }
                            var strs = s.split("[0-9]+");
                            int num;
                            try {
                                num = Integer.parseInt(strs[strs.length - 1]);
                            } catch (NumberFormatException e) {
                                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                                return;
                            }
                            try {
                                character.classes.add(new Clazz(req.getParameter(s), Integer.parseInt(req.getParameter("level" + num))));
                            } catch (NumberFormatException e) {
                                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Value for level" + num + " is not a number.");
                                return;
                            } catch (InvalidPropertyException e) {
                                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Class " + req.getParameter(s) + " not found.");
                            }


                        } else {
                            switch (s) {

                                case "str", "dex", "con", "int", "wis", "cha", "ac", "init", "speed", "maxHp", "hp" -> {
                                    try {
                                        int num = Integer.parseInt(req.getParameter(s));
                                        character.stats.replace(s, num);
                                    } catch (NumberFormatException e) {
                                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Value for " + s + "is not a number.");
                                    }
                                }
                                case "name" -> character.name = req.getParameter("name");
                                case "bg" -> character.background = req.getParameter("bg");
                                case "race" -> character.background = req.getParameter("race");
                                case "languages" -> character.languages = req.getParameter("languages");

                                default -> System.out.println("Invalid parameter given");
                            }
                        }
                    }
                    try {
                        int str, dex, con, intel, wis, cha, ac, init, speed, maxHP;

                        str = Integer.parseInt(req.getParameter("str"));
                        dex = Integer.parseInt(req.getParameter("dex"));
                        con = Integer.parseInt(req.getParameter("con"));
                        intel = Integer.parseInt(req.getParameter("int"));
                        wis = Integer.parseInt(req.getParameter("wis"));
                        cha = Integer.parseInt(req.getParameter("cha"));
                        ac = Integer.parseInt(req.getParameter("ac"));
                        init = Integer.parseInt(req.getParameter("init"));
                        speed = Integer.parseInt(req.getParameter("speed"));
                        maxHP = Integer.parseInt(req.getParameter("maxHP"));
                        character.name = req.getParameter("name");
                        character.background = req.getParameter("bg");
                        character.languages = req.getParameter("languages");
                        character.str = str;
                        character.dex = dex;
                        character.con = con;
                        character.intel = intel;
                        character.wis = wis;
                        character.cha = cha;
                        character.ac = ac;
                        character.init = init;
                        character.speed = speed;
                        character.maxHp = maxHP;
                        character.skillProfs = CharactersServlet.makeList(req, "skill");
                        character.toolProfs = CharactersServlet.makeList(req, "tool");
                        character.items = CharactersServlet.makeList(req, "item");
                        character.features = CharactersServlet.makeList(req, "feature");
                    }
                    catch(NumberFormatException e) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Values for stats must be numbers.");
                    }
                }
            }
            case "DELETE" -> {
                int id = getId(req.getRequestURI());
                ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");
                if (characters == null || characters.stream().noneMatch(p -> p.id == id))
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character ID not found");
            }
            case "GET" -> {
                ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");
                if (characters == null) {
                    System.out.println("NO CHARACTERS FOUND");
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                } else {
                    characters.forEach(p -> System.out.println(p.id));
                }

                int id = getId(req.getRequestURI());

                if (characters.stream().anyMatch(p -> p.id == id)) {
                    Character c = characters.stream().filter(p -> p.id == id).findFirst().get();
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter out = resp.getWriter();
                    out.print(gson.toJson(c));
                    out.flush();
                    out.close();
                }
            }


        }
    }

    public int getId(String uri) {
        var split = uri.split("/");
        return Integer.parseInt(split[split.length - 1]);
    }
}

import com.google.gson.Gson;

import javax.resource.spi.InvalidPropertyException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

@WebFilter(urlPatterns = {"/characters/*"})
public class CharacterFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        switch (req.getMethod()) {
            case "POST" -> {
                int str, dex, con, intel, wis, cha, ac, init, speed, maxHp;

                str = Integer.parseInt(req.getParameter("str"));
                dex = Integer.parseInt(req.getParameter("dex"));
                con = Integer.parseInt(req.getParameter("con"));
                intel = Integer.parseInt(req.getParameter("int"));
                wis = Integer.parseInt(req.getParameter("wis"));
                cha = Integer.parseInt(req.getParameter("cha"));
                ac = Integer.parseInt(req.getParameter("ac"));
                init = Integer.parseInt(req.getParameter("init"));
                speed = Integer.parseInt(req.getParameter("speed"));
                maxHp = Integer.parseInt(req.getParameter("maxHp"));
                Character c;
                try {
                    UUID id = UUID.randomUUID();
                    while (Characters.usedIds.contains(id)) {
                        id = UUID.randomUUID();
                    }
                    Characters.usedIds.add(id);
                    c = new Character(
                            (String) req.getSession().getAttribute("user"),
                            req.getParameter("name"),
                            req.getParameter("bg"),
                            req.getParameter("race"),
                            req.getParameter("languages"),
                            str,
                            dex,
                            con,
                            intel,
                            wis,
                            cha,
                            ac,
                            init,
                            speed,
                            maxHp,
                            makeList(req, "skill"),
                            makeList(req, "tool"),
                            makeList(req, "item"),
                            makeList(req, "feature"),
                            classesList(req),
                            id
                    );
                    Characters.put(id,c);
                } catch (InvalidPropertyException e) {
                    throw new ServletException();
                }
                var session = req.getSession();
                if (session.getAttribute("characters") == null) {
                    session.setAttribute("characters", new ArrayList<Character>());
                    ((ArrayList<Character>) session.getAttribute("characters")).add(c);
                    System.out.println("LSIT CRREATED");
                } else {
                    ((ArrayList<Character>) session.getAttribute("characters")).add(c);
                    System.out.println("ADDED TO LSIST");
                }
                //System.out.println(session.getAttribute("characters"));
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print(gson.toJson(c));
                out.flush();
                out.close();
            }
            case "PUT" -> {
                UUID id = getId(req.getRequestURI(), resp);
                ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");

                if (characters == null || characters.stream().noneMatch(p -> p.id.equals(id)))
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character ID not found");
                else {
                    Character character = characters.stream().filter(p -> p.id.equals(id)).findFirst().get();

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


                        } else if (s.contains("skill")) {
                            character.skillProfs.add(req.getParameter(s));
                        } else if (s.contains("tool")) {
                            character.toolProfs.add(req.getParameter(s));
                        } else if (s.contains("item")) {
                            character.items.add(req.getParameter(s));
                        } else if (s.contains("feature")) {
                            character.features.add(req.getParameter(s));
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
                }
            }
            case "DELETE" -> {
                UUID id = getId(req.getRequestURI(), resp);

                if (req.getSession().getAttribute("user") == null) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else if (Characters.contains(id) && Characters.get(id).owner.equals(req.getParameter("user"))) { //The one where it works
                    PrintWriter out = resp.getWriter();
                    Character c = Characters.remove(id);
                    Characters.usedIds.remove(id);
                    out.write(gson.toJson(c));
                    out.flush();
                    out.close();
                } else if (Characters.contains(id)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            case "GET" -> {
                PrintWriter out = resp.getWriter();
                String uri = req.getRequestURI();
                System.out.println(uri);
                if (uri.equals("/characters")) {
                    out.print(gson.toJson(Characters.list()));
                } else {
                    UUID id = getId(req.getRequestURI(), resp);
                    if (Characters.contains(id))
                        out.print(gson.toJson(Characters.get(id)));
                    else
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                out.flush();
                out.close();
            }
        }
    }

    private UUID getId(String uri, HttpServletResponse resp) throws IOException {
        var split = uri.split("/");
        if (split.length > 2)
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return UUID.fromString(split[split.length - 1]);
    }

    private static ArrayList<String> makeList(HttpServletRequest req, String name) {
        ArrayList<String> out = new ArrayList<>();
        var iter = req.getParameterNames();
        while (iter.hasMoreElements()) {
            String a = iter.nextElement();
            if (a.contains(name))
                out.add(req.getParameter(a));

        }
        return out;
    }

    private static ArrayList<Clazz> classesList(HttpServletRequest req) throws InvalidPropertyException {
        ArrayList<Clazz> out = new ArrayList<>();
        var iter = req.getParameterNames();
        while (iter.hasMoreElements()) {
            String a = iter.nextElement();
            int num = -1;
            if (a.contains("class")) {
                for (int i = 0; i < a.length(); i++) {
                    if (a.charAt(i) >= 48 || a.charAt(i) <= 57) {
                        num = i;
                        break;
                    }
                }
                if (num == -1) {
                    throw new InvalidPropertyException("");
                }
                out.add(new Clazz(req.getParameter(a), Integer.parseInt(req.getParameter("level"+num))));
            }
        }
        return out;
    }
}

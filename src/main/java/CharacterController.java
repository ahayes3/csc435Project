import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@WebFilter(urlPatterns = {"/characters/*"})
public class CharacterController implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        switch (req.getMethod()) {
            case "POST" -> {
                String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Map<String,?> map = gson.fromJson(body, Map.class);
                int str, dex, con, intel, wis, cha, ac, init, speed, maxHp;
                String name,bg,race,languages;
                Map<String,?> charObj;
                ArrayList<String> skills,tools,features,items;
                ArrayList<Clazz> classes;
                try {
                    charObj = (Map<String,?>) map.get("character");
                    charObj.keySet().forEach(System.out::println);
                    str = ((Double) charObj.get("str")).intValue();
                    dex = ((Double) charObj.get("dex")).intValue();
                    con = ((Double) charObj.get("con")).intValue();
                    intel = ((Double) charObj.get("int")).intValue();
                    wis = ((Double) charObj.get("wis")).intValue();
                    cha = ((Double) charObj.get("cha")).intValue();
                    ac = ((Double) charObj.get("ac")).intValue();
                    init = ((Double) charObj.get("init")).intValue();
                    speed = ((Double) charObj.get("speed")).intValue();
                    maxHp = ((Double) charObj.get("maxHp")).intValue();
                    name = (String) charObj.get("name");
                    bg = (String) charObj.get("background");
                    race = (String) charObj.get("race");
                    languages = (String) charObj.get("languages");
                    features = makeList(charObj,"features");
                    tools = makeList(charObj,"tools");
                    items = makeList(charObj,"items");
                    skills = makeList(charObj,"skills");
                    classes = classesList(charObj);
                }
                catch(NullPointerException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Character c;
                UUID id = UUID.randomUUID();
                while (Characters.usedIds.contains(id)) {
                    id = UUID.randomUUID();
                }
                Characters.usedIds.add(id);
                c = new Character(
                        (String) req.getSession().getAttribute("user"),
                        name,
                        bg,
                        race,
                        languages,
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
                        skills,
                        tools,
                        items,
                        features,
                        classes,
                        id
                );
                System.out.println("Character created with id "+id);
                Characters.put(id,c);
                var session = req.getSession();
                if (session.getAttribute("characters") == null) {
                    session.setAttribute("characters", new ArrayList<Character>());
                    ((ArrayList<Character>) session.getAttribute("characters")).add(c);
                    System.out.println("LSIT CRREATED");
                } else {
                    ((ArrayList<Character>) session.getAttribute("characters")).add(c);
                    System.out.println("ADDED TO LSIST");
                }
            }
            case "PUT" -> {
                UUID id = getId(req.getRequestURI(), resp);
                ArrayList<Character> characters = (ArrayList<Character>) req.getSession().getAttribute("characters");

                if (characters == null || characters.stream().noneMatch(p -> p.id.equals(id)))
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character ID not found");
                else {
                    Character character = characters.stream().filter(p -> p.id.equals(id)).findFirst().get();

                    String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    Map<String,?> map = gson.fromJson(body, Map.class);
                    int str, dex, con, intel, wis, cha, ac, init, speed, maxHp;
                    String name,bg,race,languages;
                    Map<String,?> charObj;
                    ArrayList<String> skills,tools,features,items;
                    ArrayList<Clazz> classes;
                    try {
                        charObj = (Map<String,?>) map.get("character");
                        charObj.keySet().forEach(System.out::println);
                        str = ((Double) charObj.get("str")).intValue();
                        dex = ((Double) charObj.get("dex")).intValue();
                        con = ((Double) charObj.get("con")).intValue();
                        intel = ((Double) charObj.get("int")).intValue();
                        wis = ((Double) charObj.get("wis")).intValue();
                        cha = ((Double) charObj.get("cha")).intValue();
                        ac = ((Double) charObj.get("ac")).intValue();
                        init = ((Double) charObj.get("init")).intValue();
                        speed = ((Double) charObj.get("speed")).intValue();
                        maxHp = ((Double) charObj.get("maxHp")).intValue();
                        name = (String) charObj.get("name");
                        bg = (String) charObj.get("background");
                        race = (String) charObj.get("race");
                        languages = (String) charObj.get("languages");
                        features = makeList(charObj,"features");
                        tools = makeList(charObj,"tools");
                        items = makeList(charObj,"items");
                        skills = makeList(charObj,"skills");
                        classes = classesList(charObj);
                    }
                    catch(NullPointerException e) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                    character.stats.replace("str",str);
                    character.stats.replace("dex",dex);
                    character.stats.replace("con",con);
                    character.stats.replace("int",intel);
                    character.stats.replace("wis",wis);
                    character.stats.replace("cha",cha);
                    character.stats.replace("ac",ac);
                    character.stats.replace("init",init);
                    character.stats.replace("speed",speed);
                    character.stats.replace("maxHp",maxHp);

                    character.name = name;
                    character.background = bg;
                    character.languages= languages;
                    character.race=race;
                    character.skillProfs= skills;
                    character.features=features;
                    character.toolProfs = tools;
                    character.items=items;
                    character.classes=classes;

                    CharacterView.respond(character,resp);
                }
            }
            case "DELETE" -> {
                UUID id = getId(req.getRequestURI(), resp);

                if (req.getSession().getAttribute("user") == null) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else if (Characters.contains(id) && Characters.get(id).owner.equals(req.getParameter("user"))) { //The one where it works
                    Character c = Characters.remove(id);
                    Characters.usedIds.remove(id);
                    CharacterView.respond(c,resp);
                } else if (Characters.contains(id)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            case "GET" -> {
                String uri = req.getRequestURI();
                if (uri.equals("/characters")) {
                    CharacterView.respond(Characters.list(),resp);
                } else {
                    UUID id = getId(req.getRequestURI(), resp);
                    if (Characters.contains(id))
                        CharacterView.respond(Characters.get(id),resp);
                    else
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    private UUID getId(String uri, HttpServletResponse resp) throws IOException {
        var split = uri.split("/");
        if (split.length > 2)
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return UUID.fromString(split[split.length - 1]);
    }

    private static ArrayList<String> makeList(Map<String,?> obj, String name) {
        return (ArrayList<String>) obj.get(name);
    }

    private static ArrayList<Clazz> classesList(Map<String,?> map) throws InvalidPropertiesFormatException {
        ArrayList<Clazz> out = new ArrayList<>();
        ArrayList<Map<String,?>> list = (ArrayList<Map<String,?>>) map.get("classes");
        for(Map<String,?> p : list) {
            out.add(new Clazz((String) p.get("name"),((Double) p.get("level")).intValue()));
        }
        return out;
    }
}

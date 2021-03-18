import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.Gson;

@WebFilter(urlPatterns = {"/characters/*"})
public class CharacterController implements Filter {
    private final Gson gson = new Gson();

    //todo UPDATE PUT

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        switch (req.getMethod()) {
            case "POST" -> {
                System.out.println("USER "+req.getSession().getAttribute("user"));

                String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Map<String,?> map = gson.fromJson(body, Map.class);
                int str, dex, con, intel, wis, cha, ac, init, speed, maxHp;
                String name,bg,race,languages;
                Map<String,?> charObj;
                ArrayList<String> skills,tools,features,items;
                ArrayList<Clazz> classes;
                try {
                    charObj = (Map<String, ?>) map.get("character");
                    str = ((Double) charObj.get("str")).intValue();
                    dex = ((Double) charObj.get("dex")).intValue();
                    con = ((Double) charObj.get("con")).intValue();
                    intel = ((Double) charObj.get("intel")).intValue();
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
                    System.out.println("INTEL " +intel );
                }
                catch(NullPointerException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Character c;

                UUID id = UUID.randomUUID();
                try {
                    Set<UUID> usedIds = CharacterModel.usedIds();
                    while(usedIds.contains(id)) {
                        id = UUID.randomUUID();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                c = new Character(
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
                System.out.println(c.skillProfs);
                //update to mysql
                try {
                    if(req.getSession().getAttribute("user")==null) {
                        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    CharacterModel.post(c,req.getSession().getAttribute("user").toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }

                CharacterView.respond(c,resp);
            }
            case "PUT" -> {
                UUID id = getId(req.getRequestURI(), resp);
                System.out.println("ENETERED ID "+id);
                if(id==null)
                    return;
                Set<UUID> ids=null;
                try {
                    ids = CharacterModel.getByUser(req.getSession().getAttribute("user").toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
                if (!ids.contains(id)) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character ID not found");
                    return;
                }
                else {

                    //Character character = characters.stream().filter(p -> p.id.equals(id)).findFirst().get();

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
                        intel = ((Double) charObj.get("intel")).intValue();
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

                    c = new Character(
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
                    try {
                        CharacterModel.put(id,c);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }
                    CharacterView.respond(c,resp);
                }
            }
            case "DELETE" -> {
                UUID id = getId(req.getRequestURI(), resp);

                if (req.getSession().getAttribute("user") == null) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    try {
                        Set<UUID> ids = CharacterModel.getByUser(req.getSession().getAttribute("user").toString());
                        if (ids.contains(id)) { //The one where it works
                            Character c = CharacterModel.remove(id);
                            CharacterModel.usedIds();
                            CharacterView.respond(c,resp);
                        } else if (CharacterModel.usedIds().contains(id)) {
                            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                        } else {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }
            }
            case "GET" -> {
                String uri = req.getRequestURI();
                if (uri.equals("/characters")) {
                    try {
                        CharacterView.respond(CharacterModel.getByUser(req.getSession().getAttribute("user").toString()),resp);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    //CharacterView.respond(Characters.list(),resp);
                } else {
                    UUID id = getId(req.getRequestURI(), resp);
                    Character character =null;
                    try {
                        character = CharacterModel.get(id);
                    } catch (SQLException throwables) {
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        throwables.printStackTrace();
                        return;
                    }
                    if (character != null)
                        CharacterView.respond(character,resp);
                    else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            }
        }
    }

    private UUID getId(String uri, HttpServletResponse resp) throws IOException {
        var split = uri.split("/");
        if (split.length > 3) {
            System.out.println("HERE");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
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

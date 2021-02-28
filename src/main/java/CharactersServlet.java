import com.google.gson.Gson;

import javax.resource.spi.InvalidPropertyException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/characters")
public class CharactersServlet extends HttpServlet {
    static int id = Integer.MIN_VALUE;
    private Gson gson = new Gson();
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //todo get list of all characters in json form
//
////        if(req.getSession().getAttribute("user")==null) {
////            resp.sendRedirect("/loginPage");
////            return;
////        }
////        RequestDispatcher rd = getServletContext().getRequestDispatcher("/template.html");
////        rd.forward(req, resp);
////        //Will show the creation page
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST");
        int str, dex, con, intel, wis, cha, ac, init, speed, maxHP;

//        var iter = req.getParameterNames();
//        while (iter.hasMoreElements()) {
//            System.out.println(iter.nextElement());
//        }
//        System.out.println("STR: "+ req.getParameter("str"));
//        System.out.println("DEX: "+req.getParameter("dex"));
//        System.out.println("CON: "+req.getParameter("con"));
//        System.out.println("INT: "+req.getParameter("int"));
//        System.out.println("WIS: "+req.getParameter("wis"));
//        System.out.println("CHA: "+req.getParameter("cha"));
//        System.out.println("AC: "+req.getParameter("ac"));
//        System.out.println("INIT: "+req.getParameter("init"));
//        System.out.println("SPEED: "+req.getParameter("speed"));
//        System.out.println("MAXHP: "+req.getParameter("maxHP"));
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
        Character c;
        try {
            c = new Character(req.getParameter("name"),
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
                    maxHP,
                    makeList(req, "skill"),
                    makeList(req, "tool"),
                    makeList(req, "item"),
                    makeList(req, "feature"),
                    classesList(req),
                    this.id
            );
            id++;
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

    private ArrayList<String> makeList(HttpServletRequest req, String name) {
        ArrayList<String> out = new ArrayList<>();
        var iter = req.getParameterNames();
        while (iter.hasMoreElements()) {
            String a = iter.nextElement();
            if (a.contains(name))
                out.add(req.getParameter(a));

        }
        return out;
    }

    private ArrayList<Clazz> classesList(HttpServletRequest req) throws InvalidPropertyException {
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

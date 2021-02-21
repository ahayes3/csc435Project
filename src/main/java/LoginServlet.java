//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Scanner;
//
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //if login exists do login
//        //else don't
//        if(req.getSession().getAttribute("user")!= null)
//            resp.sendRedirect("home");
//        if(!Files.exists(Path.of("users.txt")))
//            Files.createFile(Path.of("users.txt")); //temporary
//        File file = new File("users.txt");
//        Scanner s = new Scanner(file);
//        while(s.hasNext()) {
//            if(s.nextLine().equals(req.getParameter("username")))
//                break;
//        }
//        if(s.hasNext() && s.nextLine().equals(req.getParameter("password"))) {
//            req.getSession().setAttribute("user",req.getParameter("username"));
//            resp.sendRedirect("home");
//        }
//        else {
//            RequestDispatcher rd = req.getRequestDispatcher("loginPage");
//            req.setAttribute("valid","false");
//            rd.forward(req,resp);
//        }
//        s.close();
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//    }
//}

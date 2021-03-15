import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Users {
    public static boolean login(String user,String pass) {

        Scanner s = null;
        try {
            s = new Scanner(new File("users.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(s.hasNextLine()) {
            String line = s.nextLine();
            if(line.equals(user) && s.nextLine().equals(pass)) {
                System.out.println("Valid login");
                return true;
            }
        }
        return false;
    }
}

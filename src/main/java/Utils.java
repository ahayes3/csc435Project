import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
    public static Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/characterdb?allowPublicKeyRetrieval=true&useSSL=false"; //DO NOT CHANGE - STRING TO CONNECT TO DB
        String user = "root";
        String password = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = DriverManager.getConnection(url, user, password);

        return con;
    }
}

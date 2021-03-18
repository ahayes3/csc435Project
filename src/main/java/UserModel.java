import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserModel {
    public static boolean put(String user,String pass) throws SQLException {
        Connection con = Utils.connect();
        Statement st = con.createStatement();
        String query = "SELECT *" +
                " FROM users"+
                " WHERE name='"+user+"' AND pass=MD5("+pass+")";
        st.executeQuery(query);
        st.close();
        con.close();
        return true;
    }
    public static boolean post(String user,String pass) throws SQLException {
        Connection con = Utils.connect();
        Statement st = con.createStatement();

        String q0 = "SELECT * FROM users WHERE name='"+user+"'";
        ResultSet rs = st.executeQuery(q0);
        if(rs.next()) {
            return false;
        }

        String query = "INSERT INTO users(name,pass) VALUES ("+
                "'"+user+"',MD5("+pass+"))";
        st.executeUpdate(query);
        st.close();
        con.close();
        return true;
    }
}

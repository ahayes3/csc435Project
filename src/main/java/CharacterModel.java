import java.sql.*;
import java.util.Set;
import java.util.UUID;

public class CharacterModel {
    public static Character get(UUID id) throws SQLException{ //todo check to make sure id is proper format before putting into statement
        Connection con = connect();
        Statement st = con.createStatement();
        String query = "SELECT c.*,cl.*,f.*,i.*,s.*,t.*" +     //Gets a character and all the parts owned bycharacter
                " FROM characters c" +
                " WHERE c.id='"+id.toString()+"'" +
                " INNER JOIN character_classes cl" +
                " ON c.id=cl.id" +
                " INNER JOIN character_features f" +
                " ON c.id=f.id" +
                " INNER JOIN character_items" +
                " ON c.id=i.id" +
                " INNER JOIN character_skills" +
                " ON c.id=s.id" +
                " INNER JOIN character_tools" +
                " ON c.id=t.id";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {

        }

    }
    public static Set<Character> getByUser(String user) throws SQLException{
        Connection con = connect();
        Statement st = con.createStatement();
        String query = "SELECT c.*,cl.*,f.*,i.*,s.*,t.*" +
                " FROM users u" +
                " WHERE u.name='"+user+"'" +
                " INNER JOIN user_characters uc" +
                " ON u.name=uc.name" +
                " INNER JOIN characters c" +
                " ON uc.id=c.id" +
                " INNER JOIN character_classes cl" +
                " ON c.id=cl.id" +
                " INNER JOIN character_features f" +
                " ON c.id=f.id" +
                " INNER JOIN character_items" +
                " ON c.id=i.id" +
                " INNER JOIN character_skills" +
                " ON c.id=s.id" +
                " INNER JOIN character_tools" +
                " ON c.id=t.id";
        //todo build character from stats + skills + items + tools + features + classes

        ResultSet rs = st.executeQuery(query);
    }

    private static Connection connect() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/java?useSSL=false"; //DO NOT CHANGE - STRING TO CONNECT TO DB
        String user = "java";
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

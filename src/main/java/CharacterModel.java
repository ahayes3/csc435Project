import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CharacterModel {
    public static Character get(UUID id) throws SQLException,IllegalArgumentException{
        Connection con = connect();
        Statement st = con.createStatement();
        Character cr = getCharacter(id,st);
        st.close();
        con.close();
        return cr;
    }
    public static Set<UUID> getByUser(String user) throws SQLException{
        Connection con = connect();
        Statement st = con.createStatement();
        String query = "SELECT uc.id" +
                " FROM users u" +
                " WHERE u.name='"+user+"'" +
                " INNER JOIN user_characters uc" +
                " ON u.name=uc.name";

        ResultSet rs = st.executeQuery(query);

        Set<UUID> ids = new HashSet<>();
        while(rs.next()) {
            ids.add(UUID.fromString(rs.getString("id")));
        }
        rs.close();
        st.close();
        con.close();
        return ids;
    }

    public static Character remove(UUID id) throws SQLException {
        Connection con = connect();
        Statement st = con.createStatement();
        Character c = getCharacter(id,st);
        String query = "DELETE FROM characters"+
                " WHERE id'="+id.toString()+"'";
        ResultSet rs = st.executeQuery(query);
        System.out.println("DELETE RS has next: "+rs.next());
        return c;
    }
    public static void post(Character c,String user) throws SQLException {
        Connection con = connect();
        Statement st = con.createStatement();
        String query1 = "INSERT INTO characters(id,name,background,race,languages,str,dex,con,wis,intel,cha,ac,init,speed,maxHp VALUES ("+
                "'"+c.id.toString()+"',"+"'"+c.name+"',"+"'"+c.background+"',"+"'"+c.race+"',"+"'"+c.languages+"',"+"'"+c.stats.get("str")+"',"
                +"'"+c.stats.get("str")+"',"+"'"+c.stats.get("dex")+"',"+"'"+c.stats.get("con")+"',"+"'"+c.stats.get("wis")+"',"
                +"'"+c.stats.get("intel")+"',"+"'"+c.stats.get("cha")+"',"+"'"+c.stats.get("ac")+"',"
                +"'"+c.stats.get("init")+"',"+"'"+c.stats.get("speed")+"',"+"'"+c.stats.get("maxHp")+"')";
        String query2 = "INSERT INTO user_characters(name,id) VALUES (" +
                "'"+user+"',"+"'"+c.id+"')";
        st.executeQuery(query1);
        st.executeQuery(query2);


        for(String f:c.features) {
            String query3 = "INSERT IGNORE INTO character_features(id,feature) VALUES(" +
                    "'" + c.id + "','" +f+"')";
            st.executeQuery(query3).close();
        }
        for(String i:c.items) {
            String q4 = "INSERT IGNORE INTO character_items(id,item) VALUES (" +
                    "'"+c.id+"','"+i+"')";
            st.executeQuery(q4).close();
        }
        for(String s:c.skillProfs) {
            String q5 = "INSERT IGNORE INTO character_features(id,skill) VALUES (" +
                    "'"+c.id+"','"+s+"')";
            st.executeQuery(q5).close();
        }
        for(String t:c.toolProfs) {
            String q6 = "INSERT IGNORE INTO character_tools(id,tool) VALUES (" +
                    "'"+c.id+"','"+t+"')";
            st.executeQuery(q6).close();
        }
        for(Clazz cl:c.classes) {
            String q7 = "INSERT IGNORE INTO character_classes(id,classe_name,level) VALUES (" +
                    "'"+c.id+"','"+cl.name+"','"+cl.level+"')";
            st.executeQuery(q7).close();
        }
        st.close();
        con.close();

    }

    public static void put(UUID id, Character c) throws SQLException{
        Connection con = connect();
        Statement st = con.createStatement();
        String query1 = "DELETE FROM character_classes" +
                " WHERE id='"+id+"'";
        String query2 = "DELETE FROM character_features"+
                " WHERE id='"+id+"'";
        String query3 = "DELETE FROM character_items"+
                " WHERE id='"+id+"'";
        String query4 = "DELETE FROM character_skills"+
                " WHERE id='"+id+"'";
        String query5 = "DELETE FROM character_tools"+
                " WHERE id='"+id+"'";
        st.executeQuery(query1);
        st.executeQuery(query2);
        st.executeQuery(query3);
        st.executeQuery(query4);
        st.executeQuery(query5);

        String query6 = "UPDATE characters" +
                " SET name='"+c.name+"',"+
                " SET background='"+c.background+"',"+
                " SET race='"+c.race+"',"+
                " SET languages='"+c.languages+"',"+
                " SET str='"+c.stats.get("str")+"',"+
                " SET dex='"+c.stats.get("dex")+"',"+
                " SET con='"+c.stats.get("con")+"',"+
                " SET wis='"+c.stats.get("wis")+"',"+
                " SET intel='"+c.stats.get("int")+"',"+
                " SET cha='"+c.stats.get("ac")+"',"+
                " SET ac='"+c.stats.get("init")+"',"+
                " SET init='"+c.stats.get("speed")+"',"+
                " SET str='"+c.stats.get("maxHp")+"')" +
                " WHERE id='"+c.id+"'";
        st.executeQuery(query6);

        for(String f:c.features) {
            String q1 = "INSERT IGNORE INTO character_features(id,feature) VALUES(" +
                    "'" + c.id + "','" +f+"')";
            st.executeQuery(q1).close();
        }
        for(String i:c.items) {
            String q2 = "INSERT IGNORE INTO character_items(id,item) VALUES (" +
                    "'"+c.id+"','"+i+"')";
            st.executeQuery(q2).close();
        }
        for(String s:c.skillProfs) {
            String q3 = "INSERT IGNORE INTO character_features(id,skill) VALUES (" +
                    "'"+c.id+"','"+s+"')";
            st.executeQuery(q3).close();
        }
        for(String t:c.toolProfs) {
            String q4 = "INSERT IGNORE INTO character_tools(id,tool) VALUES (" +
                    "'"+c.id+"','"+t+"')";
            st.executeQuery(q4).close();
        }
        for(Clazz cl:c.classes) {
            String q5 = "INSERT IGNORE INTO character_classes(id,classe_name,level) VALUES (" +
                    "'"+c.id+"','"+cl.name+"','"+cl.level+"')";
            st.executeQuery(q5).close();
        }
    }
    public static Set<UUID> usedIds() throws SQLException{
        Connection con  = connect();
        Statement st = con.createStatement();
        Set<UUID> ids = new HashSet<>();
        String query = "SELECT uc.id" +
                " FROM user_characters uc";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            ids.add(UUID.fromString(rs.getString("id")));
        }
        return ids;
    }

    private static Character getCharacter(UUID id, Statement st) throws SQLException {
        String query1 = "SELECT c.*" +     //Gets a character and all the parts owned bycharacter
                " FROM characters c" +
                " WHERE c.id='"+id.toString()+"'";

        String query2 = "SELECT cl.*" +
                " FROM character_classes cl" +
                " ON cl.id='" + id.toString() + "'";
        String query3 = "SELECT f.*" +
                " FROM character_features f" +
                " ON f.id='" + id.toString() + "'";
        String query4= "SELECT i.*" +
                " FROM character_items i" +
                " ON i.id='" + id.toString() + "'";
        String query5 = "SELECT s.*" +
                " FROM character_skills s" +
                " ON s.id='" + id.toString() + "'";
        String query6 = "SELECT t.*" +
                " FROM character_tools t" +
                " ON t.id='" + id.toString() + "'";

        ResultSet c = st.executeQuery(query1);
        ResultSet cl = st.executeQuery(query2);
        ResultSet f = st.executeQuery(query3);
        ResultSet i = st.executeQuery(query4);
        ResultSet s = st.executeQuery(query5);
        ResultSet t = st.executeQuery(query6);
        if(c.wasNull()) {
            return null;
        }
        c.next();
        Character cr = new Character(c.getString("name"),c.getString("background"),c.getString("race"),
                c.getString("languages"),c.getInt("str"),c.getInt("dex"),c.getInt("con"),c.getInt("intel"),c.getInt("wis"),
                c.getInt("cha"),c.getInt("ac"),c.getInt("init"),c.getInt("speed"),c.getInt("maxHp"),UUID.fromString(c.getString("id")));
        c.close();
        ArrayList<Clazz> classes = new ArrayList<>();
        ArrayList<String> features = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        ArrayList<String> skills = new ArrayList<>();
        ArrayList<String> tools = new ArrayList<>();
        while(cl.next()) {
            classes.add(new Clazz(cl.getString("class_name"),cl.getInt("level")));
        }
        cl.close();
        while(f.next()) {
            features.add(f.getString("feature"));
        }
        f.close();
        while(i.next()) {
            items.add(f.getString("item"));
        }
        i.close();
        while(s.next()) {
            skills.add(s.getString("skill"));
        }
        s.close();
        while(t.next()) {
            tools.add(t.getString("tool"));
        }
        t.close();
        cr.setClasses(classes);
        cr.setFeatures(features);
        cr.setItems(items);
        cr.setSkills(skills);
        cr.setToolProfs(tools);
        return cr;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CharacterModel {
    public static Character get(UUID id) throws SQLException, IllegalArgumentException {
        Connection con = Utils.connect();
        Character cr = getCharacter(id, con);
        con.close();
        return cr;
    }

    public static Set<UUID> getByUser(String user) throws SQLException {
        Connection con = Utils.connect();
        PreparedStatement st1 = con.prepareStatement("SELECT uc.id FROM users u INNER JOIN user_characters uc ON u.name=uc.name AND u.name=?");
        st1.setString(1,user);
        ResultSet rs = st1.executeQuery();

        Set<UUID> ids = new HashSet<>();
        while (rs.next()) {
            ids.add(UUID.fromString(rs.getString("id")));
        }
        rs.close();
        st1.close();
        con.close();
        return ids;
    }

    public static Character remove(UUID id) throws SQLException {
        Connection con = Utils.connect();
        PreparedStatement st1 = con.prepareStatement("DELETE FROM characters" +
                " WHERE id=?");
        PreparedStatement st2 = con.prepareStatement("DELETE FROM user_characters WHERE id=?");
        Character c = getCharacter(id, con);
        st1.setString(1,id.toString());
        st2.setString(1,id.toString());

        st1.executeUpdate();
        st2.executeUpdate();
        return c;
    }

    public static void post(Character c, String user) throws SQLException {
        System.out.println("INTEL2: " + c.stats.get("intel"));
        Connection con = Utils.connect();
        PreparedStatement st1 = con.prepareStatement("INSERT INTO characters(id,name,background,race,languages,str,dex,con,wis,intel,cha,ac,init,speed,maxHp) VALUES (" +
                "'" + c.id.toString() + "','" + c.name + "','" + c.background + "','" + c.race + "','" + c.languages + "','" + c.stats.get("str") + "','"
                + c.stats.get("dex") + "','" + c.stats.get("con") + "','" + c.stats.get("wis") + "','"
                + c.stats.get("intel") + "','" + c.stats.get("cha") + "','" + c.stats.get("ac") + "','"
                + c.stats.get("init") + "','" + c.stats.get("speed") + "','" + c.stats.get("maxHp") + "')");
        String query2 = "INSERT INTO user_characters(name,id) VALUES (" +
                "'" + user + "'," + "'" + c.id + "')";
        PreparedStatement st2 = con.prepareStatement(query2);
        st1.executeUpdate();
        st2.executeUpdate();


        for (String t : c.features) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_features(id,feature) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }
        for (String t : c.items) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_items(id,item) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }
        for (String t : c.skillProfs) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_skills(id,skill) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }

        for (String t : c.toolProfs) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_tools(id,tool) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }
        for (Clazz t : c.classes) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_classes(id,class_name,level) VALUES(?,?,?)");
            pst.setString(1, c.id.toString());
            pst.setString(2,t.name);
            pst.setInt(3,t.level);
            pst.executeUpdate();
            pst.close();
        }
        con.close();

    }

    public static void put(UUID id, Character c) throws SQLException {
        System.out.println("ID 2 : "+ id);
        Connection con = Utils.connect();

        String query1 = "DELETE FROM character_classes" +
                " WHERE id='" + id + "'";
        String query2 = "DELETE FROM character_features" +
                " WHERE id='" + id + "'";
        String query3 = "DELETE FROM character_items" +
                " WHERE id='" + id + "'";
        String query4 = "DELETE FROM character_skills" +
                " WHERE id='" + id + "'";
        String query5 = "DELETE FROM character_tools" +
                " WHERE id='" + id + "'";
        PreparedStatement st1 = con.prepareStatement(query1);
        PreparedStatement st2 = con.prepareStatement(query2);
        PreparedStatement st3 = con.prepareStatement(query3);
        PreparedStatement st4 = con.prepareStatement(query4);
        PreparedStatement st5 = con.prepareStatement(query5);

        st1.executeUpdate(query1);
        st2.executeUpdate(query2);
        st3.executeUpdate(query3);
        st4.executeUpdate(query4);
        st5.executeUpdate(query5);

        String query6 = "REPLACE characters(id,name,background,race,languages,str,dex,con,wis,intel,cha,ac,init,speed,maxHp)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement st6 = con.prepareStatement(query6);
        st6.setString(1,id.toString());
        st6.setString(2,c.name);
        st6.setString(3,c.background);
        st6.setString(4,c.race);
        st6.setString(5,c.languages);
        st6.setInt(6,c.stats.get("str"));
        st6.setInt(7,c.stats.get("dex"));
        st6.setInt(8,c.stats.get("con"));
        st6.setInt(9,c.stats.get("wis"));
        st6.setInt(10,c.stats.get("intel"));
        st6.setInt(11,c.stats.get("cha"));
        st6.setInt(12,c.stats.get("ac"));
        st6.setInt(13,c.stats.get("init"));
        st6.setInt(14,c.stats.get("speed"));
        st6.setInt(15,c.stats.get("maxHp"));
        st6.executeUpdate();

        for (String t : c.features) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_features(id,feature) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }
        for (String t : c.items) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_items(id,item) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }
        for (String t : c.skillProfs) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_skills(id,skill) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }

        for (String t : c.toolProfs) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_tools(id,tool) VALUES(" +
                    "'" + c.id + "',?)");
            pst.setString(1, t);
            pst.executeUpdate();
            pst.close();
        }
        for (Clazz t : c.classes) {
            PreparedStatement pst = con.prepareStatement("INSERT IGNORE INTO character_classes(id,class_name,level) VALUES(?,?,?)");
            pst.setString(1, c.id.toString());
            pst.setString(2,t.name);
            pst.setInt(3,t.level);
            pst.executeUpdate();
            pst.close();
        }
    }

    public static Set<UUID> usedIds() throws SQLException {
        Connection con = Utils.connect();
        Statement st = con.createStatement();
        Set<UUID> ids = new HashSet<>();
        String query = "SELECT uc.id" +
                " FROM user_characters uc";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            ids.add(UUID.fromString(rs.getString("id")));
        }
        return ids;
    }

    private static Character getCharacter(UUID id, Connection con) throws SQLException {

        PreparedStatement st1= con.prepareStatement("SELECT c.* FROM characters c WHERE c.id=?");
        PreparedStatement st2= con.prepareStatement("SELECT c.* FROM character_classes c WHERE c.id=?");
        PreparedStatement st3= con.prepareStatement("SELECT c.* FROM character_features c WHERE c.id=?");
        PreparedStatement st4= con.prepareStatement("SELECT c.* FROM character_items c WHERE c.id=?");
        PreparedStatement st5= con.prepareStatement("SELECT c.* FROM character_skills c WHERE c.id=?");
        PreparedStatement st6= con.prepareStatement("SELECT c.* FROM character_tools c WHERE c.id=?");

        st1.setString(1,id.toString());
        st2.setString(1,id.toString());
        st3.setString(1,id.toString());
        st4.setString(1,id.toString());
        st5.setString(1,id.toString());
        st6.setString(1,id.toString());

        ResultSet c = st1.executeQuery();
        ResultSet cl = st2.executeQuery();
        ResultSet f = st3.executeQuery();
        ResultSet i = st4.executeQuery();
        ResultSet s = st5.executeQuery();
        ResultSet t = st6.executeQuery();
        if (c.isClosed() || !c.next()) {
            return null;
        }
        Character cr = new Character(c.getString("name"), c.getString("background"), c.getString("race"),
                c.getString("languages"), c.getInt("str"), c.getInt("dex"), c.getInt("con"), c.getInt("intel"), c.getInt("wis"),
                c.getInt("cha"), c.getInt("ac"), c.getInt("init"), c.getInt("speed"), c.getInt("maxHp"), UUID.fromString(c.getString("id")));
        c.close();
        ArrayList<Clazz> classes = new ArrayList<>();
        ArrayList<String> features = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        ArrayList<String> skills = new ArrayList<>();
        ArrayList<String> tools = new ArrayList<>();
        while (cl.next()) {
            classes.add(new Clazz(cl.getString("class_name"), cl.getInt("level")));
        }
        cl.close();
        while (f.next()) {
            features.add(f.getString("feature"));
        }
        f.close();
        while (i.next()) {
            items.add(i.getString("item"));
        }
        i.close();
        while (s.next()) {
            skills.add(s.getString("skill"));
        }
        s.close();
        while (t.next()) {
            tools.add(t.getString("tool"));
        }
        st1.close();
        st2.close();
        st3.close();
        st4.close();
        st5.close();
        st6.close();
        t.close();
        cr.setClasses(classes);
        cr.setFeatures(features);
        cr.setItems(items);
        cr.setSkills(skills);
        cr.setToolProfs(tools);
        return cr;
    }
}

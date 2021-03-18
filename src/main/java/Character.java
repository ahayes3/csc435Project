import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Character {
    String name,background,race,languages;
    HashMap<String,Integer> stats;
    UUID id;
    //int str,dex,con,intel,wis,cha,ac,init,speed,maxHp,hp;
    ArrayList<Clazz> classes;
    ArrayList<String> skillProfs;
    static HashMap<String,Ability> skills = new HashMap<>(){{
        put("Acrobatics",Ability.DEX);
        put("Animal Handling",Ability.WIS);
        put("Arcana",Ability.INT);
        put("Athletics",Ability.STR);
        put("Deception",Ability.CHA);
        put("History",Ability.INT);
        put("Insight",Ability.WIS);
        put("Intimidation",Ability.CHA);
        put("Investigation",Ability.INT);
        put("Medicine",Ability.WIS);
        put("Nature",Ability.INT);
        put("Perception",Ability.WIS);
        put("Performance",Ability.CHA);
        put("Persuasion",Ability.CHA);
        put("Religion",Ability.INT);
        put("Sleight of Hand",Ability.DEX);
        put("Stealth",Ability.DEX);
        put("Survival",Ability.WIS);
    }};
    ArrayList<String> toolProfs,items,features;

    public Character(String name,String background,String race,String languages,int str,int dex,int con,int intel,int wis,int cha, int ac,int init,int speed, int maxHp,UUID id) {
        stats = new HashMap<>();
        this.name=name;
        this.background=background;
        this.race=race;
        this.languages=languages;
        stats.put("str",str);
        stats.put("dex",dex);
        stats.put("con",con);
        stats.put("int",intel);
        stats.put("wis",wis);
        stats.put("cha",cha);
        stats.put("ac",ac);
        stats.put("init",init);
        stats.put("speed",speed);
        stats.put("maxHp",maxHp);
        stats.put("hp",maxHp);
        this.id=id;

        classes=new ArrayList<>();
        skillProfs=new ArrayList<>();
        toolProfs=new ArrayList<>();
        items=new ArrayList<>();
        features=new ArrayList<>();
    }
    public Character(String name,String background,String race,String languages,int str,int dex,int con,int intel,int wis,int cha, int ac,int init,int speed, int maxHp,ArrayList<String> skillProfs,ArrayList<String> toolProfs,ArrayList<String> items,ArrayList<String> features,ArrayList<Clazz> classes,UUID id) {
        stats = new HashMap<>();
        this.name=name;
        this.background=background;
        this.race=race;
        this.languages=languages;
        stats.put("str",str);
        stats.put("dex",dex);
        stats.put("con",con);
        stats.put("intel",intel);
        stats.put("wis",wis);
        stats.put("cha",cha);
        stats.put("ac",ac);
        stats.put("init",init);
        stats.put("speed",speed);
        stats.put("maxHp",maxHp);
        stats.put("hp",maxHp);

        this.skillProfs = skillProfs;
        this.toolProfs=toolProfs;
        this.items=items;
        this.features=features;
        this.classes=classes;
        this.id=id;
    }
    public int skillAbility(String skill) {
        Ability a = skills.get(skill);
        switch(a) {
            case STR:
                return strMod();
            case DEX:
                return dexMod();
            case CON:
                return conMod();
            case INT:
                return intMod();
            case WIS:
                return wisMod();
            case CHA:
                return chaMod();
        }
        return Integer.MIN_VALUE;
    }
    public void setSkills(ArrayList<String> s) {
        if(s!=null)
            this.skillProfs=s;
    }
    public void setItems(ArrayList<String> i) {
        this.items=i;
    }
    public void setToolProfs(ArrayList<String> t) {
        this.toolProfs = t;
    }
    public void setFeatures(ArrayList<String> f) {
        this.features = f;
    }
    public void setClasses(ArrayList<Clazz> c) {
        this.classes= c;
    }
    public int strMod(){
        return (stats.get("str")-10)/2;
    }
    public int dexMod(){
        return (stats.get("dex")-10)/2;
    }
    public int conMod(){
        return (stats.get("con")-10)/2;
    }
    public int intMod(){
        return (stats.get("int")-10)/2;
    }
    public int wisMod(){
        return (stats.get("wis")-10)/2;
    }
    public int chaMod() {
        return (stats.get("cha")-10)/2;
    }
    public int totalLevel() {
        return classes.stream().map(p -> p.level).reduce(0,(a,b) -> a+b);
    }
    public int proficiency() {
        int l = totalLevel();
        if(l < 5)
            return 2;
        else if(l <9)
            return 3;
        else if(l < 13)
            return 4;
        else if(l < 16)
            return 5;
        else
            return 6;

    }
    public int skillMod(String skill) {
        int mod = skillAbility(skill);
        if(skillProfs.contains(skill))
            mod+= proficiency();
        return mod;

    }
}

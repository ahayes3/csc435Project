import java.util.ArrayList;
import java.util.HashMap;

public class Character {
    class Resource {
        public Resource(String name,int max) {
            this.max=max;
            this.current=max;
        }
        int max,current;
    }
    String name,background,race,languages;
    int id;
    int str,dex,con,intel,wis,cha,ac,init,speed,maxHp,hp;
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
    ArrayList<Resource> resources;


    public Character(String name,String background,String race,String languages,int str,int dex,int con,int intel,int wis,int cha, int ac,int init,int speed, int maxHp,ArrayList<String> skillProfs,ArrayList<String> toolProfs,ArrayList<String> items,ArrayList<String> features,ArrayList<Clazz> classes,int id) {
        this.name=name;
        this.background=background;
        this.race=race;
        this.languages=languages;
        this.str = str;
        this.dex=dex;
        this.con=con;
        this.intel = intel;
        this.wis=wis;
        this.cha=cha;
        this.ac=ac;
        this.init=init;
        this.speed=speed;
        this.maxHp=maxHp;
        this.hp=maxHp;
        this.skillProfs = skillProfs;
        this.toolProfs=toolProfs;
        this.items=items;
        this.features=features;
        resources= new ArrayList<>();
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
    public int strMod(){
        return (str-10)/2;
    }
    public int dexMod(){
        return (dex-10)/2;
    }
    public int conMod(){
        return (con-10)/2;
    }
    public int intMod(){
        return (intel-10)/2;
    }
    public int wisMod(){
        return (wis-10)/2;
    }
    public int chaMod() {
        return (cha-10)/2;
    }
    public void addResource(String name, int amt) {
        resources.add(new Resource(name,amt));
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

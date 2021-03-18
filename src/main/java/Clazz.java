import javax.resource.spi.InvalidPropertyException;
import java.util.InvalidPropertiesFormatException;

public class Clazz {
    String name;
    int hitDie;
    int level;
    boolean[] savingThrows;
    public Clazz(String name,int level) {
        switch(name.toLowerCase()) {
            case "artificer":
                this.name = "Artificer";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, true, true, false, false};
                break;
            case "barbarian":
                this.name = "Barbarian";
                this.hitDie = 12;
                this.savingThrows = new boolean[]{true, false, true, false, false, false};
                break;
            case "bard":
                this.name = "Bard";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, true, false, false, false, true};
                break;
            case "cleric":
                this.name = "Cleric";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, false, false, true, true};
                break;
            case "druid":
                this.name = "Druid";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, false, true, true, false};
                break;
            case "fighter":
                this.name = "Fighter";
                this.hitDie = 10;
                this.savingThrows = new boolean[]{true, false, true, false, false, false};
                break;
            case "monk":
                this.name = "Monk";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{true, true, false, false, false, false};
                break;
            case "paladin":
                this.name = "Paladin";
                this.hitDie = 10;
                this.savingThrows = new boolean[]{false, false, false, false, true, true};
                break;
            case "ranger":
                this.name = "Ranger";
                this.hitDie = 10;
                this.savingThrows = new boolean[]{true, true, false, false, false, false};
                break;
            case "rogue":
                this.name = "Rogue";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, true, false, true, false, false};
                break;
            case "sorcerer":
                this.name = "Sorcerer";
                this.hitDie = 6;
                this.savingThrows = new boolean[]{false, false, true, false, false, true};
                break;
            case "warlock":
                this.name = "Warlock";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, false, false, true, true};
                break;
            case "wizard":
                this.name = "Wizard";
                this.hitDie = 6;
                this.savingThrows = new boolean[]{false, false, false, true, true, false};
                break;
            default:
                System.out.println("INVALID CLASS");
                throw new IllegalArgumentException("Class " + name + " not found");

        }
        this.level=level;
    }
}

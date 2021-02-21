import javax.resource.spi.InvalidPropertyException;

public class Clazz {
    String name;
    int hitDie;
    int level;
    boolean[] savingThrows;
    public Clazz(String name,int level) throws InvalidPropertyException {
        switch(name) {
            case "Artificer":
                this.name = "Artificer";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, true, true, false, false};
                break;
            case "Barbarian":
                this.name = "Barbarian";
                this.hitDie = 12;
                this.savingThrows = new boolean[]{true, false, true, false, false, false};
                break;
            case "Bard":
                this.name = "Bard";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, true, false, false, false, true};
                break;
            case "Cleric":
                this.name = "Cleric";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, false, false, true, true};
                break;
            case "Druid":
                this.name = "Druid";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, false, true, true, false};
                break;
            case "Fighter":
                this.name = "Fighter";
                this.hitDie = 10;
                this.savingThrows = new boolean[]{true, false, true, false, false, false};
                break;
            case "Monk":
                this.name = "Monk";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{true, true, false, false, false, false};
                break;
            case "Paladin":
                this.name = "Paladin";
                this.hitDie = 10;
                this.savingThrows = new boolean[]{false, false, false, false, true, true};
                break;
            case "Ranger":
                this.name = "Ranger";
                this.hitDie = 10;
                this.savingThrows = new boolean[]{true, true, false, false, false, false};
                break;
            case "Rogue":
                this.name = "Rogue";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, true, false, true, false, false};
                break;
            case "Sorcerer":
                this.name = "Sorcerer";
                this.hitDie = 6;
                this.savingThrows = new boolean[]{false, false, true, false, false, true};
                break;
            case "Warlock":
                this.name = "Warlock";
                this.hitDie = 8;
                this.savingThrows = new boolean[]{false, false, false, false, true, true};
                break;
            case "Wizard":
                this.name = "Wizard";
                this.hitDie = 6;
                this.savingThrows = new boolean[]{false, false, false, true, true, false};
                break;
            default:
                System.out.println("INVALID CLASS");
                throw new InvalidPropertyException("Class " + name + " not found");

        }
        this.level=level;
    }
}

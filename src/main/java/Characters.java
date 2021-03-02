import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class Characters {
    private static HashMap<UUID,Character> characters=new HashMap<>();
    public static HashSet<UUID> usedIds=new HashSet<>();

    public static Character get(UUID id) {
        return characters.get(id);
    }
    public static void put(UUID id,Character c) {
        characters.put(id,c);
    }
    public static Character remove(UUID id) {
        return characters.remove(id);
    }
    public static HashMap<UUID,Character> list() {
        return characters;
    }
    public static boolean isEmpty() {
        return characters.isEmpty();
    }
    public static boolean contains(UUID id) {
        return characters.containsKey(id);
    }
}

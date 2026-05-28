package esporthub.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDatabase {
    private static final Map<String, User> database = new HashMap<>();

    static {
        // Admin account
        database.put("admin", new User("admin", "admin123", "Admin", "", false));

        // Mock player data with popular esports team names
        database.put("luminaire", new User("Luminaire", "lumi123", "Player", "RRQ Hoshi", false));
        database.put("kairi", new User("Kairi", "kairi123", "Player", "ONIC Esports", false));
        database.put("alberttt", new User("Alberttt", "albert123", "Player", "RRQ Hoshi", false));
        database.put("vynnn", new User("Vynnn", "vynn123", "Player", "EVOS Legends", false));
        database.put("skylar", new User("Skylar", "sky123", "Player", "Alter Ego", false));
        database.put("celiboy", new User("Celiboy", "celi123", "Player", "ONIC Esports", false));
        database.put("antimage", new User("Antimage", "anti123", "Player", "EVOS Legends", false));
        database.put("pai", new User("Pai", "pai123", "Player", "Bigetron Alpha", false));
        database.put("sanz", new User("Sanz", "sanz123", "Player", "Alter Ego", false));
        database.put("psychoo", new User("Psychoo", "psy123", "Player", "Bigetron Alpha", false));
    }

    public static boolean exists(String username) {
        return database.containsKey(username.toLowerCase());
    }

    public static User getUser(String username) {
        return database.get(username.toLowerCase());
    }

    public static void register(String username, String password, String role) {
        database.put(username.toLowerCase(), new User(username, password, role, "", false));
    }

    public static void deleteUser(String username) {
        database.remove(username.toLowerCase());
    }

    public static List<User> getAllPlayers() {
        List<User> players = new ArrayList<>();
        for (User user : database.values()) {
            if ("Player".equalsIgnoreCase(user.getRole())) {
                players.add(user);
            }
        }
        return players;
    }

    public static List<String> getAllTeamNames() {
        List<String> teams = new ArrayList<>();
        for (User user : database.values()) {
            if ("Player".equalsIgnoreCase(user.getRole()) && user.getTeamName() != null
                    && !user.getTeamName().isEmpty() && !teams.contains(user.getTeamName())) {
                teams.add(user.getTeamName());
            }
        }
        return teams;
    }
}

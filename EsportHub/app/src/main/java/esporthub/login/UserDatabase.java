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

        // Mock player data with popular esports team names and statistics
        addMockPlayer("Luminaire", "lumi123", "RRQ Hoshi", "Mythical Glory", "Midlaner", "luminaire@rrqhoshi.com", "829304812",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-10 19:30", "Faramis", "Win", 4, 1, 12),
                new esporthub.Player.PlayerMatch("2026-05-12 21:00", "Yve", "Win", 2, 2, 10),
                new esporthub.Player.PlayerMatch("2026-05-14 18:15", "Valentina", "Lose", 3, 4, 5),
                new esporthub.Player.PlayerMatch("2026-05-16 20:45", "Pharsa", "Win", 6, 0, 8),
                new esporthub.Player.PlayerMatch("2026-05-18 19:00", "Lylia", "Win", 5, 2, 11)
            )
        );

        addMockPlayer("Kairi", "kairi123", "ONIC Esports", "Mythical Immortal", "Jungler", "kairi@onicesports.com", "204859102",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-11 20:00", "Ling", "Win", 12, 1, 6),
                new esporthub.Player.PlayerMatch("2026-05-13 19:30", "Fanny", "Win", 9, 0, 5),
                new esporthub.Player.PlayerMatch("2026-05-15 21:15", "Lancelot", "Lose", 4, 3, 2),
                new esporthub.Player.PlayerMatch("2026-05-17 18:00", "Nolan", "Win", 8, 2, 7),
                new esporthub.Player.PlayerMatch("2026-05-19 20:30", "Baxia", "Win", 3, 1, 14),
                new esporthub.Player.PlayerMatch("2026-05-21 19:00", "Hayabusa", "Win", 11, 2, 4)
            )
        );

        addMockPlayer("Alberttt", "albert123", "RRQ Hoshi", "Mythical Glory", "Jungler", "alberttt@rrqhoshi.com", "748392019",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-10 19:30", "Lancelot", "Win", 8, 2, 6),
                new esporthub.Player.PlayerMatch("2026-05-12 21:00", "Ling", "Lose", 3, 4, 4),
                new esporthub.Player.PlayerMatch("2026-05-14 18:15", "Fredrinn", "Win", 2, 1, 10)
            )
        );

        addMockPlayer("Vynnn", "vynn123", "EVOS Legends", "Mythic", "Roamer", "vynnn@evoslegends.com", "102938475",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-11 20:00", "Tigreal", "Win", 1, 3, 15),
                new esporthub.Player.PlayerMatch("2026-05-13 19:30", "Minotaur", "Lose", 0, 5, 6),
                new esporthub.Player.PlayerMatch("2026-05-15 21:15", "Angela", "Win", 2, 2, 18)
            )
        );

        addMockPlayer("Skylar", "sky123", "Alter Ego", "Mythical Honor", "Gold Laner", "skylar@alterego.com", "637482910",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-12 19:00", "Beatrix", "Win", 9, 2, 4),
                new esporthub.Player.PlayerMatch("2026-05-14 20:30", "Claude", "Lose", 4, 4, 3),
                new esporthub.Player.PlayerMatch("2026-05-16 18:45", "Bruno", "Win", 7, 1, 5)
            )
        );

        addMockPlayer("Celiboy", "celi123", "ONIC Esports", "Mythical Glory", "Jungler", "celiboy@onicesports.com", "987654321",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-11 20:00", "Nolan", "Lose", 3, 5, 2),
                new esporthub.Player.PlayerMatch("2026-05-13 19:30", "Martis", "Win", 8, 2, 6)
            )
        );

        addMockPlayer("Antimage", "anti123", "EVOS Legends", "Mythic", "Exp Laner", "antimage@evoslegends.com", "123456789",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-10 19:30", "Terizla", "Win", 4, 2, 8),
                new esporthub.Player.PlayerMatch("2026-05-12 21:00", "Yu Zhong", "Win", 6, 3, 5)
            )
        );

        addMockPlayer("Pai", "pai123", "Bigetron Alpha", "Mythic", "Exp Laner", "pai@bigetron.com", "456789123",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-11 20:00", "X.Borg", "Win", 5, 1, 9),
                new esporthub.Player.PlayerMatch("2026-05-13 19:30", "Arlott", "Lose", 2, 4, 3)
            )
        );

        addMockPlayer("Sanz", "sanz123", "Alter Ego", "Mythical Honor", "Midlaner", "sanz@alterego.com", "789123456",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-12 19:00", "Novaria", "Win", 3, 1, 12),
                new esporthub.Player.PlayerMatch("2026-05-14 20:30", "Kagura", "Win", 5, 0, 8)
            )
        );

        addMockPlayer("Psychoo", "psy123", "Bigetron Alpha", "Mythic", "Roamer", "psychoo@bigetron.com", "321654987",
            java.util.List.of(
                new esporthub.Player.PlayerMatch("2026-05-10 19:30", "Chou", "Win", 2, 3, 11),
                new esporthub.Player.PlayerMatch("2026-05-12 21:00", "Franco", "Lose", 1, 4, 5)
            )
        );
    }

    private static void addMockPlayer(String username, String password, String team, String rank, String gameRole, String email, String playerId, java.util.List<esporthub.Player.PlayerMatch> matches) {
        User u = new User(username, password, "Player", team, false);
        u.setRank(rank);
        u.setGameRole(gameRole);
        u.setEmail(email);
        u.setPlayerId(playerId);
        u.setMatchHistory(new java.util.ArrayList<>(matches));
        database.put(username.toLowerCase(), u);
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

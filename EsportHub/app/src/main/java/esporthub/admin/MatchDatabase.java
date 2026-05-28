package esporthub.admin;

import java.util.ArrayList;
import java.util.List;

public class MatchDatabase {
    private static final List<Match> matches = new ArrayList<>();
    private static int nextId = 1;

    static {
        // Mock data pertandingan awal
        matches.add(new Match(generateId(), "RRQ Hoshi", "ONIC Esports", "2026-06-01 19:00", "Scheduled", 0, 0, "-"));
        matches.add(new Match(generateId(), "EVOS Legends", "Alter Ego", "2026-06-02 20:00", "Scheduled", 0, 0, "-"));
        matches.add(new Match(generateId(), "Bigetron Alpha", "RRQ Hoshi", "2026-06-03 19:00", "Scheduled", 0, 0, "-"));
        matches.add(new Match(generateId(), "ONIC Esports", "EVOS Legends", "2026-05-20 19:00", "Completed", 3, 1, "ONIC Esports"));
        matches.add(new Match(generateId(), "Alter Ego", "Bigetron Alpha", "2026-05-22 20:00", "Completed", 2, 3, "Bigetron Alpha"));
    }

    private static String generateId() {
        return "M" + String.format("%03d", nextId++);
    }

    public static List<Match> getAllMatches() {
        return new ArrayList<>(matches);
    }

    public static void addMatch(Match match) {
        match.setId(generateId());
        matches.add(match);
    }

    public static void updateMatch(Match updated) {
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getId().equals(updated.getId())) {
                matches.set(i, updated);
                return;
            }
        }
    }

    public static void deleteMatch(String id) {
        matches.removeIf(m -> m.getId().equals(id));
    }

    public static Match getMatch(String id) {
        for (Match m : matches) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public static long countByStatus(String status) {
        return matches.stream().filter(m -> m.getStatus().equals(status)).count();
    }
}

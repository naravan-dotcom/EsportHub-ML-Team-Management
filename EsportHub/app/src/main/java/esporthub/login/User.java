package esporthub.login;

public class User {
    private String username;
    private String password;
    private String role; // "Admin" atau "Player"
    private String teamName;
    private boolean isBanned;

    // Player Profile fields
    private String playerId;
    private String email;
    private String rank;
    private String gameRole;
    private java.util.List<esporthub.Player.PlayerMatch> matchHistory;

    public User(String username, String password, String role) {
        this(username, password, role, "", false);
    }

    public User(String username, String password, String role, String teamName, boolean isBanned) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.teamName = teamName;
        this.isBanned = isBanned;
        
        // Default player profile values
        this.playerId = String.valueOf((int) (Math.random() * 900000000) + 100000000);
        this.email = username.toLowerCase().replaceAll("\\s+", "") + "@esporthub.com";
        this.rank = "Grandmaster";
        this.gameRole = "All Rounder";
        this.matchHistory = new java.util.ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        this.isBanned = banned;
    }

    // Player Profile Getters & Setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public java.util.List<esporthub.Player.PlayerMatch> getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistory(java.util.List<esporthub.Player.PlayerMatch> matchHistory) {
        this.matchHistory = matchHistory;
    }

    // Helper statistics
    public double getOverallKda() {
        if (matchHistory == null || matchHistory.isEmpty()) return 0.0;
        int kills = 0, deaths = 0, assists = 0;
        for (esporthub.Player.PlayerMatch m : matchHistory) {
            kills += m.getKills();
            deaths += m.getDeaths();
            assists += m.getAssists();
        }
        return (kills + assists) / (double) Math.max(1, deaths);
    }

    public double getOverallWinrate() {
        if (matchHistory == null || matchHistory.isEmpty()) return 0.0;
        long wins = matchHistory.stream().filter(m -> "Win".equalsIgnoreCase(m.getResult())).count();
        return (wins * 100.0) / matchHistory.size();
    }
}

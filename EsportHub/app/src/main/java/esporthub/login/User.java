package esporthub.login;

public class User {
    private String username;
    private String password;
    private String role; // "Admin" atau "Player"
    private String teamName;
    private boolean isBanned;

    public User(String username, String password, String role) {
        this(username, password, role, "", false);
    }

    public User(String username, String password, String role, String teamName, boolean isBanned) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.teamName = teamName;
        this.isBanned = isBanned;
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
}

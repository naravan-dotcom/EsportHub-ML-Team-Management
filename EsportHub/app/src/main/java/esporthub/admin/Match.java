package esporthub.admin;

public class Match {
    private String id;
    private String teamA;
    private String teamB;
    private String matchDate;
    private String status; // "Scheduled" or "Completed"
    private int scoreA;
    private int scoreB;
    private String winner;

    public Match(String id, String teamA, String teamB, String matchDate) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.matchDate = matchDate;
        this.status = "Scheduled";
        this.scoreA = 0;
        this.scoreB = 0;
        this.winner = "-";
    }

    public Match(String id, String teamA, String teamB, String matchDate, String status, int scoreA, int scoreB, String winner) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.matchDate = matchDate;
        this.status = status;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.winner = winner;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTeamA() { return teamA; }
    public void setTeamA(String teamA) { this.teamA = teamA; }

    public String getTeamB() { return teamB; }
    public void setTeamB(String teamB) { this.teamB = teamB; }

    public String getMatchDate() { return matchDate; }
    public void setMatchDate(String matchDate) { this.matchDate = matchDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getScoreA() { return scoreA; }
    public void setScoreA(int scoreA) { this.scoreA = scoreA; }

    public int getScoreB() { return scoreB; }
    public void setScoreB(int scoreB) { this.scoreB = scoreB; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }

    public String getResult() {
        if ("Completed".equals(status)) {
            return scoreA + " - " + scoreB;
        }
        return "Belum dimainkan";
    }
}

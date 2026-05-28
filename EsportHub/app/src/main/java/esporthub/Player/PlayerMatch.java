package esporthub.Player;

public class PlayerMatch {
    private String matchDate;
    private String hero;
    private String result; // "Win" or "Lose"
    private int kills;
    private int deaths;
    private int assists;

    public PlayerMatch(String matchDate, String hero, String result, int kills, int deaths, int assists) {
        this.matchDate = matchDate;
        this.hero = hero;
        this.result = result;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public double getKda() {
        return (kills + assists) / (double) Math.max(1, deaths);
    }
}

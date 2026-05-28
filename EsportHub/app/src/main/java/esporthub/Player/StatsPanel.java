package esporthub.Player;

import esporthub.login.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Handles the Statistics & KDA Chart panel.
 */
public class StatsPanel {

    private final User playerUser;
    private final StackPane contentArea;

    public StatsPanel(User playerUser, StackPane contentArea) {
        this.playerUser = playerUser;
        this.contentArea = contentArea;
    }

    public void show() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));

        Label title = new Label("📈 Statistik & Grafik KDA");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        // Metrik Overview Cards
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        double winrate = playerUser.getOverallWinrate();
        double kda = playerUser.getOverallKda();
        int matches = playerUser.getMatchHistory().size();

        // Calculate Kills, Deaths, Assists total for description
        int kills = 0, deaths = 0, assists = 0;
        for (PlayerMatch m : playerUser.getMatchHistory()) {
            kills += m.getKills();
            deaths += m.getDeaths();
            assists += m.getAssists();
        }

        statsRow.getChildren().addAll(
            createStatCard("🏆", "Winrate Keseluruhan", String.format("%.1f%%", winrate), "Rasio kemenangan match history", "#6c5ce7"),
            createStatCard("⚔", "KDA Rata-rata", String.format("%.2f", kda), String.format("K: %d | D: %d | A: %d", kills, deaths, assists), "#00b894"),
            createStatCard("🎮", "Total Pertandingan", String.valueOf(matches), "Matches yang tercatat", "#fdcb6e")
        );

        // Chart Area
        VBox chartBox = new VBox(10);
        chartBox.setPadding(new Insets(20));
        chartBox.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #eeeef5; -fx-border-radius: 12px;");
        DropShadow shadow = new DropShadow(8, Color.web("#00000006"));
        chartBox.setEffect(shadow);
        VBox.setVgrow(chartBox, Priority.ALWAYS);

        Label chartTitle = new Label("Grafik Perkembangan KDA");
        chartTitle.setFont(Font.font("System", FontWeight.BOLD, 15));
        chartTitle.setTextFill(Color.web("#1a1a2e"));

        if (playerUser.getMatchHistory().isEmpty()) {
            Label noChart = new Label("Belum ada riwayat pertandingan untuk digambar.");
            noChart.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            chartBox.getChildren().addAll(chartTitle, noChart);
        } else {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Pertandingan (Hero)");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("KDA");

            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setLegendVisible(false);
            lineChart.setStyle("-fx-background-color: transparent;");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            
            List<PlayerMatch> history = playerUser.getMatchHistory();
            for (int i = 0; i < history.size(); i++) {
                PlayerMatch m = history.get(i);
                series.getData().add(new XYChart.Data<>("M" + (i + 1) + " (" + m.getHero() + ")", m.getKda()));
            }

            lineChart.getData().add(series);
            chartBox.getChildren().addAll(chartTitle, lineChart);
        }

        panel.getChildren().addAll(title, statsRow, chartBox);
        contentArea.getChildren().add(panel);
    }

    private VBox createStatCard(String icon, String label, String value, String description, String accentColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setPrefWidth(240);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #eeeef5; -fx-border-radius: 12px;");
        DropShadow shadow = new DropShadow(8, Color.web("#00000006"));
        card.setEffect(shadow);

        Text iconText = new Text(icon);
        iconText.setFont(Font.font("System", 24));

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font("System", FontWeight.BOLD, 26));
        valLbl.setTextFill(Color.web(accentColor));

        Label nameLbl = new Label(label);
        nameLbl.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px; -fx-font-weight: bold;");

        Label descLbl = new Label(description);
        descLbl.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        card.getChildren().addAll(iconText, valLbl, nameLbl, descLbl);
        return card;
    }
}

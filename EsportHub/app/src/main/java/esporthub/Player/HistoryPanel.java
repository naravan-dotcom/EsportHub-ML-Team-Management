package esporthub.Player;

import esporthub.login.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Handles the Match History panel with table and winrate bar.
 */
public class HistoryPanel {

    private final User playerUser;
    private final StackPane contentArea;

    public HistoryPanel(User playerUser, StackPane contentArea) {
        this.playerUser = playerUser;
        this.contentArea = contentArea;
    }

    @SuppressWarnings("unchecked")
    public void show() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(15);
        panel.setPadding(new Insets(30));

        Label title = new Label("📜 Riwayat Pertandingan");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        // Table
        TableView<PlayerMatch> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<PlayerMatch, String> dateCol = new TableColumn<>("Tanggal");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));

        TableColumn<PlayerMatch, String> heroCol = new TableColumn<>("Hero");
        heroCol.setCellValueFactory(new PropertyValueFactory<>("hero"));

        TableColumn<PlayerMatch, String> resultCol = new TableColumn<>("Hasil");
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        resultCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Win".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<PlayerMatch, Integer> killsCol = new TableColumn<>("Kills");
        killsCol.setCellValueFactory(new PropertyValueFactory<>("kills"));

        TableColumn<PlayerMatch, Integer> deathsCol = new TableColumn<>("Deaths");
        deathsCol.setCellValueFactory(new PropertyValueFactory<>("deaths"));

        TableColumn<PlayerMatch, Integer> assistsCol = new TableColumn<>("Assists");
        assistsCol.setCellValueFactory(new PropertyValueFactory<>("assists"));

        TableColumn<PlayerMatch, Double> kdaCol = new TableColumn<>("KDA");
        kdaCol.setCellValueFactory(new PropertyValueFactory<>("kda"));
        kdaCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });

        table.getColumns().addAll(dateCol, heroCol, resultCol, killsCol, deathsCol, assistsCol, kdaCol);

        ObservableList<PlayerMatch> data = FXCollections.observableArrayList(playerUser.getMatchHistory());
        table.setItems(data);

        // Winrate display bar
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(12, 20, 12, 20));
        bottomBar.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #eeeef5; -fx-border-radius: 8px;");
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        double winrate = playerUser.getOverallWinrate();
        Label wrLabel = new Label("📊 Winrate di History: ");
        wrLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");

        Label wrValue = new Label(String.format("%.1f%%", winrate));
        wrValue.setStyle("-fx-font-weight: bold; -fx-text-fill: #6c5ce7; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label totalLabel = new Label("Total Matches: " + playerUser.getMatchHistory().size());
        totalLabel.setStyle("-fx-text-fill: #777777; -fx-font-size: 12px;");

        bottomBar.getChildren().addAll(wrLabel, wrValue, spacer, totalLabel);

        panel.getChildren().addAll(title, table, bottomBar);
        contentArea.getChildren().add(panel);
    }
}

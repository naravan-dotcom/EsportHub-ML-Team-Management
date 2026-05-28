package esporthub.Player;

import esporthub.login.User;
import esporthub.login.UserDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the Team panel — shows teammates or a prompt to join a team.
 */
public class TeamPanel {

    private final User playerUser;
    private final StackPane contentArea;
    private final Runnable showProfileCallback;

    public TeamPanel(User playerUser, StackPane contentArea, Runnable showProfileCallback) {
        this.playerUser = playerUser;
        this.contentArea = contentArea;
        this.showProfileCallback = showProfileCallback;
    }

    @SuppressWarnings("unchecked")
    public void show() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));

        String teamName = playerUser.getTeamName();
        boolean hasTeam = teamName != null && !teamName.isEmpty();

        Label title = new Label("👥 Tim Saya" + (hasTeam ? ": " + teamName : ""));
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        if (!hasTeam) {
            // Friendly warning card
            VBox warnCard = new VBox(15);
            warnCard.setPadding(new Insets(25));
            warnCard.setAlignment(Pos.CENTER);
            warnCard.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #fdcb6e; -fx-border-width: 1px; -fx-border-radius: 12px;");
            DropShadow shadow = new DropShadow(8, Color.web("#00000005"));
            warnCard.setEffect(shadow);

            Text icon = new Text("⚠️");
            icon.setFont(Font.font("System", 40));

            Label msg1 = new Label("Anda belum bergabung dengan tim manapun.");
            msg1.setStyle("-fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-font-size: 15px;");

            Label msg2 = new Label("Edit profil Anda untuk menentukan nama tim Anda agar bisa melihat daftar rekan tim.");
            msg2.setStyle("-fx-text-fill: #777777; -fx-font-size: 12px;");

            Button toProfileBtn = DashboardUtils.createStyledButton("Buka Profil Saya", "#6c5ce7");
            toProfileBtn.setPrefWidth(180);
            toProfileBtn.setOnAction(e -> {
                if (showProfileCallback != null) showProfileCallback.run();
            });

            warnCard.getChildren().addAll(icon, msg1, msg2, toProfileBtn);
            panel.getChildren().addAll(title, warnCard);
        } else {
            // Find teammates
            List<User> teammates = UserDatabase.getAllPlayers().stream()
                    .filter(u -> teamName.equalsIgnoreCase(u.getTeamName()) && !playerUser.getUsername().equalsIgnoreCase(u.getUsername()))
                    .collect(Collectors.toList());

            // Teammates Count Card
            VBox countCard = new VBox(5);
            countCard.setPadding(new Insets(15, 20, 15, 20));
            countCard.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #eeeef5; -fx-border-radius: 10px;");
            Label countLabel = new Label("Total Anggota Tim Lainnya");
            countLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-weight: bold;");
            Label countVal = new Label((teammates.size()) + " Pemain");
            countVal.setFont(Font.font("System", FontWeight.BOLD, 22));
            countVal.setTextFill(Color.web("#6c5ce7"));
            countCard.getChildren().addAll(countLabel, countVal);

            // Table of Teammates
            VBox tableBox = new VBox(10);
            VBox.setVgrow(tableBox, Priority.ALWAYS);

            Label listTitle = new Label("Daftar Rekan Satu Tim");
            listTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
            listTitle.setTextFill(Color.web("#1a1a2e"));

            TableView<User> table = new TableView<>();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            table.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
            VBox.setVgrow(table, Priority.ALWAYS);

            TableColumn<User, String> nameCol = new TableColumn<>("Username");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

            TableColumn<User, String> idCol = new TableColumn<>("Player ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("playerId"));

            TableColumn<User, String> emailCol = new TableColumn<>("Email");
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<User, String> rankCol = new TableColumn<>("Rank Game");
            rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));

            TableColumn<User, String> roleCol = new TableColumn<>("Role Game");
            roleCol.setCellValueFactory(new PropertyValueFactory<>("gameRole"));

            table.getColumns().addAll(nameCol, idCol, emailCol, rankCol, roleCol);

            ObservableList<User> data = FXCollections.observableArrayList(teammates);
            table.setItems(data);

            if (teammates.isEmpty()) {
                Label noTeammates = new Label("Tidak ada rekan tim lain yang terdaftar dalam tim ini.");
                noTeammates.setStyle("-fx-text-fill: #888888; -fx-font-style: italic; -fx-padding: 20;");
                tableBox.getChildren().addAll(listTitle, noTeammates);
            } else {
                tableBox.getChildren().addAll(listTitle, table);
            }

            panel.getChildren().addAll(title, countCard, tableBox);
        }

        contentArea.getChildren().add(panel);
    }
}

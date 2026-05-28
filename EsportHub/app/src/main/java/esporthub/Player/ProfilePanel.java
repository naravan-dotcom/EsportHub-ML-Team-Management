package esporthub.Player;

import esporthub.login.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Handles the Profile panel — both view mode and edit mode.
 */
public class ProfilePanel {

    private final User playerUser;
    private final StackPane contentArea;
    private Label sidebarUserLabel;

    public ProfilePanel(User playerUser, StackPane contentArea, Label sidebarUserLabel) {
        this.playerUser = playerUser;
        this.contentArea = contentArea;
        this.sidebarUserLabel = sidebarUserLabel;
    }

    /**
     * Sets the sidebar user label reference (used when the label is created after this panel).
     */
    public void setSidebarUserLabel(Label sidebarUserLabel) {
        this.sidebarUserLabel = sidebarUserLabel;
    }

    public void show() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));

        Label title = new Label("👤 Profil Saya");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        // Content Wrapper that switches between View and Edit Mode
        StackPane modeContainer = new StackPane();
        VBox.setVgrow(modeContainer, Priority.ALWAYS);

        // Populate View Mode
        VBox viewModeLayout = createViewMode(modeContainer);
        modeContainer.getChildren().add(viewModeLayout);

        panel.getChildren().addAll(title, modeContainer);
        contentArea.getChildren().add(panel);
    }

    private VBox createViewMode(StackPane container) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #eeeef5; -fx-border-radius: 12px;");
        
        DropShadow shadow = new DropShadow(10, Color.web("#00000008"));
        box.setEffect(shadow);

        // Header Avatar
        HBox avatarRow = new HBox(20);
        avatarRow.setAlignment(Pos.CENTER_LEFT);

        VBox avatarCircle = new VBox();
        avatarCircle.setAlignment(Pos.CENTER);
        avatarCircle.setPrefSize(70, 70);
        avatarCircle.setStyle("-fx-background-color: linear-gradient(to right, #6c5ce7, #a855f7); -fx-background-radius: 35px;");
        Text avatarText = new Text(playerUser.getUsername().substring(0, 1).toUpperCase());
        avatarText.setFont(Font.font("System", FontWeight.BOLD, 30));
        avatarText.setFill(Color.WHITE);
        avatarCircle.getChildren().add(avatarText);

        VBox nameDetails = new VBox(5);
        Label nameLbl = new Label(playerUser.getUsername());
        nameLbl.setFont(Font.font("System", FontWeight.BOLD, 20));
        nameLbl.setTextFill(Color.web("#1a1a2e"));

        Label idBadge = new Label("ID: " + playerUser.getPlayerId());
        idBadge.setStyle("-fx-background-color: #f0eeff; -fx-text-fill: #6c5ce7; -fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 3 10; -fx-background-radius: 12px;");

        nameDetails.getChildren().addAll(nameLbl, idBadge);
        avatarRow.getChildren().addAll(avatarCircle, nameDetails);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #eeeef5;");

        // Profile details grid
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(15);

        // Column Constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(250);
        grid.getColumnConstraints().addAll(col1, col2);

        DashboardUtils.addGridRow(grid, 0, "📧 Email", playerUser.getEmail());
        DashboardUtils.addGridRow(grid, 1, "🏆 Rank Game", playerUser.getRank());
        DashboardUtils.addGridRow(grid, 2, "⚔ Role Game", playerUser.getGameRole());
        
        String teamNameText = playerUser.getTeamName() == null || playerUser.getTeamName().isEmpty() 
                ? "Belum bergabung dengan tim" : playerUser.getTeamName();
        DashboardUtils.addGridRow(grid, 3, "👥 Tim", teamNameText);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button editBtn = DashboardUtils.createStyledButton("✏ Edit Detail Profil", "#6c5ce7");
        editBtn.setPrefWidth(220);
        editBtn.setOnAction(e -> {
            container.getChildren().clear();
            container.getChildren().add(createEditMode(container));
        });

        box.getChildren().addAll(avatarRow, sep, grid, spacer, editBtn);
        return box;
    }

    private VBox createEditMode(StackPane container) {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #eeeef5; -fx-border-radius: 12px;");
        
        DropShadow shadow = new DropShadow(10, Color.web("#00000008"));
        box.setEffect(shadow);

        Label subtitle = new Label("✏ Edit Informasi Profil");
        subtitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        subtitle.setTextFill(Color.web("#1a1a2e"));

        // Fields
        Label userLabel = new Label("Username");
        userLabel.setStyle("-fx-text-fill: #555555; -fx-font-weight: bold; -fx-font-size: 12px;");
        TextField userField = new TextField(playerUser.getUsername());
        userField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8 12;");

        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-text-fill: #555555; -fx-font-weight: bold; -fx-font-size: 12px;");
        TextField emailField = new TextField(playerUser.getEmail());
        emailField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8 12;");

        Label rankLabel = new Label("Rank Mobile Legends");
        rankLabel.setStyle("-fx-text-fill: #555555; -fx-font-weight: bold; -fx-font-size: 12px;");
        ComboBox<String> rankBox = new ComboBox<>(FXCollections.observableArrayList(
            "Warrior", "Elite", "Master", "Grandmaster", "Epic", "Legend", "Mythic", "Mythical Honor", "Mythical Glory", "Mythical Immortal"
        ));
        rankBox.setValue(playerUser.getRank());
        rankBox.setMaxWidth(Double.MAX_VALUE);
        rankBox.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px;");

        Label roleLabel = new Label("Role Game");
        roleLabel.setStyle("-fx-text-fill: #555555; -fx-font-weight: bold; -fx-font-size: 12px;");
        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList(
            "Jungler", "Midlaner", "Gold Laner", "Exp Laner", "Roamer", "All Rounder"
        ));
        roleBox.setValue(playerUser.getGameRole());
        roleBox.setMaxWidth(Double.MAX_VALUE);
        roleBox.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px;");

        Label teamLabel = new Label("Nama Tim");
        teamLabel.setStyle("-fx-text-fill: #555555; -fx-font-weight: bold; -fx-font-size: 12px;");
        TextField teamField = new TextField(playerUser.getTeamName());
        teamField.setPromptText("Masukkan Nama Tim (opsional)");
        teamField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8 12;");

        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 12px;");

        HBox btnRow = new HBox(10);
        Button saveBtn = DashboardUtils.createStyledButton("Simpan", "#00b894");
        saveBtn.setPrefWidth(120);
        Button cancelBtn = DashboardUtils.createStyledButton("Batal", "#636e72");
        cancelBtn.setPrefWidth(120);
        btnRow.getChildren().addAll(saveBtn, cancelBtn);

        cancelBtn.setOnAction(e -> {
            container.getChildren().clear();
            container.getChildren().add(createViewMode(container));
        });

        saveBtn.setOnAction(e -> {
            String userVal = userField.getText().trim();
            String emailVal = emailField.getText().trim();
            String rankVal = rankBox.getValue();
            String roleVal = roleBox.getValue();
            String teamVal = teamField.getText().trim();

            if (userVal.isEmpty() || emailVal.isEmpty()) {
                feedbackLabel.setText("Username dan email tidak boleh kosong!");
                feedbackLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                return;
            }

            // Save to database/user
            playerUser.setUsername(userVal);
            playerUser.setEmail(emailVal);
            playerUser.setRank(rankVal);
            playerUser.setGameRole(roleVal);
            playerUser.setTeamName(teamVal);

            // Trigger UI update on sidebar label
            sidebarUserLabel.setText("👤 " + playerUser.getUsername() + " (ID: " + playerUser.getPlayerId() + ")");

            container.getChildren().clear();
            VBox viewMode = createViewMode(container);
            
            // Inject temporary success message inside view mode
            Label successLbl = new Label("Profil berhasil diperbarui!");
            successLbl.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-background-color: #eef9f2; -fx-padding: 8 15; -fx-background-radius: 6px;");
            successLbl.setMaxWidth(Double.MAX_VALUE);
            viewMode.getChildren().add(1, successLbl);

            container.getChildren().add(viewMode);
        });

        box.getChildren().addAll(
            subtitle, 
            userLabel, userField, 
            emailLabel, emailField, 
            rankLabel, rankBox, 
            roleLabel, roleBox, 
            teamLabel, teamField, 
            feedbackLabel, btnRow
        );
        
        return box;
    }
}

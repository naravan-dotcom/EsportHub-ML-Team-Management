package esporthub.Player;

import esporthub.login.User;
import esporthub.login.UserDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerDashboard extends HBox {

    private final StackPane contentArea;
    private final Runnable onLogout;
    private final User playerUser;
    private VBox lastActiveNavButton = null;
    private Label sidebarUserLabel;

    // Styling constants matching AdminDashboard
    private static final String SIDEBAR_BG = "-fx-background-color: linear-gradient(to bottom, #0f0c29, #1b1642);";
    private static final String NAV_NORMAL = "-fx-background-color: transparent; -fx-background-radius: 8px; -fx-cursor: hand;";
    private static final String NAV_ACTIVE = "-fx-background-color: rgba(168, 85, 247, 0.25); -fx-background-radius: 8px; -fx-cursor: hand;";
    private static final String CONTENT_BG = "-fx-background-color: #f0f0f5;";

    public PlayerDashboard(User playerUser, Runnable onLogout) {
        super(0);
        this.playerUser = playerUser;
        this.onLogout = onLogout;

        setPrefSize(1020, 640);
        setMinSize(1020, 640);

        // ===== LEFT SIDEBAR =====
        VBox sidebar = createSidebar();

        // ===== MAIN CONTENT AREA =====
        contentArea = new StackPane();
        contentArea.setStyle(CONTENT_BG);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        getChildren().addAll(sidebar, contentArea);

        // Show Profile panel by default
        showProfilePanel();
    }

    // ======================== SIDEBAR ========================

    private VBox createSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setStyle(SIDEBAR_BG);

        // Logo
        Text logo = new Text("🎮 EsportHub");
        logo.setFont(Font.font("System", FontWeight.BOLD, 20));
        logo.setFill(Color.WHITE);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#a855f7", 0.5));
        glow.setRadius(10);
        logo.setEffect(glow);

        Label hubLabel = new Label("Player Dashboard");
        hubLabel.setStyle("-fx-text-fill: #a855f7; -fx-font-size: 11px; -fx-font-weight: bold;");

        sidebarUserLabel = new Label("👤 " + playerUser.getUsername() + " (ID: " + playerUser.getPlayerId() + ")");
        sidebarUserLabel.setStyle("-fx-text-fill: #9999bb; -fx-font-size: 11px;");

        Region spacer1 = new Region();
        spacer1.setPrefHeight(20);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333366;");

        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);

        // Navigation Buttons
        VBox navProfile = createNavButton("👤", "Profil Saya", this::showProfilePanel);
        VBox navStats = createNavButton("📈", "Statistik & Grafik", this::showStatsPanel);
        VBox navHistory = createNavButton("📜", "Riwayat Match", this::showHistoryPanel);
        VBox navTeam = createNavButton("👥", "Tim Saya", this::showTeamPanel);

        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox navLogout = createNavButton("🚪", "Keluar", () -> {
            if (onLogout != null) onLogout.run();
        });

        sidebar.getChildren().addAll(
            logo, hubLabel, sidebarUserLabel,
            spacer1, sep, spacer2,
            navProfile, navStats, navHistory, navTeam,
            bottomSpacer, navLogout
        );

        lastActiveNavButton = navProfile;
        navProfile.setStyle(NAV_ACTIVE);

        return sidebar;
    }

    private VBox createNavButton(String icon, String text, Runnable action) {
        VBox btn = new VBox();
        btn.setPadding(new Insets(10, 15, 10, 15));
        btn.setStyle(NAV_NORMAL);

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Text iconText = new Text(icon);
        iconText.setFont(Font.font("System", 16));

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #ccccee; -fx-font-size: 13px;");

        row.getChildren().addAll(iconText, label);
        btn.getChildren().add(row);

        btn.setOnMouseEntered(e -> {
            if (btn != lastActiveNavButton) {
                btn.setStyle("-fx-background-color: rgba(168, 85, 247, 0.12); -fx-background-radius: 8px; -fx-cursor: hand;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != lastActiveNavButton) {
                btn.setStyle(NAV_NORMAL);
            }
        });
        btn.setOnMouseClicked(e -> {
            if (lastActiveNavButton != null) lastActiveNavButton.setStyle(NAV_NORMAL);
            btn.setStyle(NAV_ACTIVE);
            lastActiveNavButton = btn;
            action.run();
        });

        return btn;
    }

    // ======================== PROFILE PANEL ========================

    private void showProfilePanel() {
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
        VBox viewModeLayout = createProfileViewMode(modeContainer);
        modeContainer.getChildren().add(viewModeLayout);

        panel.getChildren().addAll(title, modeContainer);
        contentArea.getChildren().add(panel);
    }

    private VBox createProfileViewMode(StackPane container) {
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

        addGridRow(grid, 0, "📧 Email", playerUser.getEmail());
        addGridRow(grid, 1, "🏆 Rank Game", playerUser.getRank());
        addGridRow(grid, 2, "⚔ Role Game", playerUser.getGameRole());
        
        String teamNameText = playerUser.getTeamName() == null || playerUser.getTeamName().isEmpty() 
                ? "Belum bergabung dengan tim" : playerUser.getTeamName();
        addGridRow(grid, 3, "👥 Tim", teamNameText);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button editBtn = createStyledButton("✏ Edit Detail Profil", "#6c5ce7");
        editBtn.setPrefWidth(220);
        editBtn.setOnAction(e -> {
            container.getChildren().clear();
            container.getChildren().add(createProfileEditMode(container));
        });

        box.getChildren().addAll(avatarRow, sep, grid, spacer, editBtn);
        return box;
    }

    private void addGridRow(GridPane grid, int row, String label, String value) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label val = new Label(value);
        val.setStyle("-fx-text-fill: #1a1a2e; -fx-font-size: 14px; -fx-font-weight: bold;");
        grid.add(lbl, 0, row);
        grid.add(val, 1, row);
    }

    private VBox createProfileEditMode(StackPane container) {
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
        Button saveBtn = createStyledButton("Simpan", "#00b894");
        saveBtn.setPrefWidth(120);
        Button cancelBtn = createStyledButton("Batal", "#636e72");
        cancelBtn.setPrefWidth(120);
        btnRow.getChildren().addAll(saveBtn, cancelBtn);

        cancelBtn.setOnAction(e -> {
            container.getChildren().clear();
            container.getChildren().add(createProfileViewMode(container));
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
            VBox viewMode = createProfileViewMode(container);
            
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

    // ======================== STATS & CHART PANEL ========================

    private void showStatsPanel() {
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

    // ======================== MATCH HISTORY PANEL ========================

    @SuppressWarnings("unchecked")
    private void showHistoryPanel() {
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

    // ======================== TEAM PANEL ========================

    @SuppressWarnings("unchecked")
    private void showTeamPanel() {
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

            Button toProfileBtn = createStyledButton("Buka Profil Saya", "#6c5ce7");
            toProfileBtn.setPrefWidth(180);
            toProfileBtn.setOnAction(e -> {
                // Trigger profile nav click manually
                showProfilePanel();
                // Find and reset nav state
                // This is a shortcut: sidebar will adjust on next click.
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

    // ======================== UTILITIES ========================

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        String baseStyle = "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 6px; -fx-cursor: hand;";
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(baseStyle + "-fx-opacity: 0.85;"));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }
}

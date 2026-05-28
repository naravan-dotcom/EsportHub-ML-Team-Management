package esporthub.admin;

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

public class AdminDashboard extends HBox {

    private final StackPane contentArea;
    private final Runnable onLogout;
    private VBox lastActiveNavButton = null;

    // Sidebar style constants
    private static final String SIDEBAR_BG = "-fx-background-color: linear-gradient(to bottom, #0f0c29, #1a1a3e);";
    private static final String NAV_NORMAL = "-fx-background-color: transparent; -fx-background-radius: 8px; -fx-cursor: hand;";
    private static final String NAV_ACTIVE = "-fx-background-color: rgba(168, 85, 247, 0.25); -fx-background-radius: 8px; -fx-cursor: hand;";
    private static final String CONTENT_BG = "-fx-background-color: #f0f0f5;";

    public AdminDashboard(User adminUser, Runnable onLogout) {
        super(0);
        this.onLogout = onLogout;
        setPrefSize(1020, 640);
        setMinSize(1020, 640);

        // ===== LEFT SIDEBAR =====
        VBox sidebar = createSidebar(adminUser);

        // ===== MAIN CONTENT =====
        contentArea = new StackPane();
        contentArea.setStyle(CONTENT_BG);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        getChildren().addAll(sidebar, contentArea);

        // Show dashboard by default
        showDashboardPanel();
    }

    // ======================== SIDEBAR ========================

    private VBox createSidebar(User adminUser) {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setStyle(SIDEBAR_BG);

        // Logo area
        Text logo = new Text("🎮 EsportHub");
        logo.setFont(Font.font("System", FontWeight.BOLD, 20));
        logo.setFill(Color.WHITE);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#a855f7", 0.5));
        glow.setRadius(10);
        logo.setEffect(glow);

        Label roleLabel = new Label("Admin Console");
        roleLabel.setStyle("-fx-text-fill: #a855f7; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label userLabel = new Label("👤 " + adminUser.getUsername());
        userLabel.setStyle("-fx-text-fill: #9999bb; -fx-font-size: 11px;");

        Region spacer1 = new Region();
        spacer1.setPrefHeight(20);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333366;");

        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);

        // Nav buttons
        VBox navDashboard = createNavButton("📊", "Dashboard", () -> showDashboardPanel());
        VBox navSchedule = createNavButton("📅", "Jadwal Pertandingan", () -> showSchedulePanel());
        VBox navTeams = createNavButton("👥", "Tim & Pemain", () -> showTeamsPanel());

        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox navLogout = createNavButton("🚪", "Keluar", () -> {
            if (onLogout != null) onLogout.run();
        });

        sidebar.getChildren().addAll(
            logo, roleLabel, userLabel,
            spacer1, sep, spacer2,
            navDashboard, navSchedule, navTeams,
            bottomSpacer, navLogout
        );

        // Set dashboard as active initially
        lastActiveNavButton = navDashboard;
        navDashboard.setStyle(NAV_ACTIVE);

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

    // ======================== DASHBOARD PANEL ========================

    private void showDashboardPanel() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));

        Label title = new Label("📊 Dashboard Overview");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        // Stats cards
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        List<String> teams = UserDatabase.getAllTeamNames();
        List<User> players = UserDatabase.getAllPlayers();
        long scheduled = MatchDatabase.countByStatus("Scheduled");
        long completed = MatchDatabase.countByStatus("Completed");

        statsRow.getChildren().addAll(
            createDashCard("👥", "Total Tim", String.valueOf(teams.size()), "#6c5ce7"),
            createDashCard("🎮", "Total Pemain", String.valueOf(players.size()), "#00b894"),
            createDashCard("📅", "Jadwal Aktif", String.valueOf(scheduled), "#fdcb6e"),
            createDashCard("✅", "Selesai", String.valueOf(completed), "#e17055")
        );

        // Upcoming matches preview
        Label upcomingTitle = new Label("📅 Pertandingan Mendatang");
        upcomingTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        upcomingTitle.setTextFill(Color.web("#1a1a2e"));
        upcomingTitle.setPadding(new Insets(10, 0, 0, 0));

        VBox matchList = new VBox(8);
        List<Match> upcomingMatches = MatchDatabase.getAllMatches().stream()
                .filter(m -> "Scheduled".equals(m.getStatus()))
                .collect(Collectors.toList());

        if (upcomingMatches.isEmpty()) {
            Label noMatch = new Label("Tidak ada pertandingan terjadwal.");
            noMatch.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            matchList.getChildren().add(noMatch);
        } else {
            for (Match m : upcomingMatches) {
                HBox matchCard = new HBox(15);
                matchCard.setPadding(new Insets(12, 18, 12, 18));
                matchCard.setAlignment(Pos.CENTER_LEFT);
                matchCard.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 10px;" +
                    "-fx-border-color: #e8e8f0; -fx-border-radius: 10px;"
                );
                DropShadow shadow = new DropShadow(5, Color.web("#00000010"));
                matchCard.setEffect(shadow);

                Label teams2 = new Label(m.getTeamA() + "  ⚔  " + m.getTeamB());
                teams2.setFont(Font.font("System", FontWeight.BOLD, 14));
                teams2.setTextFill(Color.web("#1a1a2e"));

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label date = new Label("📆 " + m.getMatchDate());
                date.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

                Label status = new Label("⏳ Scheduled");
                status.setStyle("-fx-text-fill: #6c5ce7; -fx-font-weight: bold; -fx-font-size: 11px; " +
                    "-fx-background-color: #f0eeff; -fx-padding: 3 10; -fx-background-radius: 12px;");

                matchCard.getChildren().addAll(teams2, spacer, date, status);
                matchList.getChildren().add(matchCard);
            }
        }

        ScrollPane scrollPane = new ScrollPane(matchList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        panel.getChildren().addAll(title, statsRow, upcomingTitle, scrollPane);
        contentArea.getChildren().add(panel);
    }

    private VBox createDashCard(String icon, String label, String value, String accentColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setPrefWidth(170);
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 14px;" +
            "-fx-border-color: #eeeef5; -fx-border-radius: 14px;"
        );
        DropShadow shadow = new DropShadow(8, Color.web("#00000008"));
        card.setEffect(shadow);

        Text iconText = new Text(icon);
        iconText.setFont(Font.font("System", 24));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(accentColor));

        Label nameLabel = new Label(label);
        nameLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        card.getChildren().addAll(iconText, valueLabel, nameLabel);
        return card;
    }

    // ======================== SCHEDULE PANEL ========================

    @SuppressWarnings("unchecked")
    private void showSchedulePanel() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(15);
        panel.setPadding(new Insets(30));

        Label title = new Label("📅 Kelola Jadwal Pertandingan");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        // Table
        TableView<Match> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");

        TableColumn<Match, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Match, String> teamACol = new TableColumn<>("Tim A");
        teamACol.setCellValueFactory(new PropertyValueFactory<>("teamA"));

        TableColumn<Match, String> teamBCol = new TableColumn<>("Tim B");
        teamBCol.setCellValueFactory(new PropertyValueFactory<>("teamB"));

        TableColumn<Match, String> dateCol = new TableColumn<>("Tanggal");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));

        TableColumn<Match, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Completed".equals(item)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<Match, String> resultCol = new TableColumn<>("Hasil");
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        TableColumn<Match, String> winnerCol = new TableColumn<>("Pemenang");
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winner"));

        table.getColumns().addAll(idCol, teamACol, teamBCol, dateCol, statusCol, resultCol, winnerCol);
        refreshScheduleTable(table);

        VBox.setVgrow(table, Priority.ALWAYS);

        // Form area
        HBox formArea = new HBox(15);
        formArea.setAlignment(Pos.TOP_LEFT);

        // --- Add / Edit Match Form ---
        VBox addForm = new VBox(8);
        addForm.setPadding(new Insets(15));
        addForm.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #eeeef5; -fx-border-radius: 10px;");
        addForm.setPrefWidth(350);

        Label addTitle = new Label("➕ Tambah / Edit Pertandingan");
        addTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
        addTitle.setTextFill(Color.web("#1a1a2e"));

        TextField teamAField = new TextField();
        teamAField.setPromptText("Nama Tim A");
        teamAField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        TextField teamBField = new TextField();
        teamBField.setPromptText("Nama Tim B");
        teamBField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        TextField dateField = new TextField();
        dateField.setPromptText("Tanggal (YYYY-MM-DD HH:mm)");
        dateField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label addFeedback = new Label();
        addFeedback.setStyle("-fx-font-size: 11px;");

        Button addBtn = createStyledButton("Tambah Pertandingan", "#6c5ce7");
        Button editBtn = createStyledButton("Simpan Perubahan", "#00b894");
        editBtn.setDisable(true);

        HBox addBtnRow = new HBox(8, addBtn, editBtn);

        final String[] editingId = {null};

        // Fill form when selecting row
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                teamAField.setText(newVal.getTeamA());
                teamBField.setText(newVal.getTeamB());
                dateField.setText(newVal.getMatchDate());
                editingId[0] = newVal.getId();
                editBtn.setDisable(false);
            }
        });

        addBtn.setOnAction(e -> {
            String tA = teamAField.getText().trim();
            String tB = teamBField.getText().trim();
            String dt = dateField.getText().trim();
            if (tA.isEmpty() || tB.isEmpty() || dt.isEmpty()) {
                addFeedback.setText("Semua field harus diisi!");
                addFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
                return;
            }
            MatchDatabase.addMatch(new Match("", tA, tB, dt));
            refreshScheduleTable(table);
            teamAField.clear(); teamBField.clear(); dateField.clear();
            editingId[0] = null;
            editBtn.setDisable(true);
            addFeedback.setText("Pertandingan berhasil ditambahkan!");
            addFeedback.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");
        });

        editBtn.setOnAction(e -> {
            if (editingId[0] == null) return;
            Match m = MatchDatabase.getMatch(editingId[0]);
            if (m != null) {
                m.setTeamA(teamAField.getText().trim());
                m.setTeamB(teamBField.getText().trim());
                m.setMatchDate(dateField.getText().trim());
                MatchDatabase.updateMatch(m);
                refreshScheduleTable(table);
                addFeedback.setText("Pertandingan berhasil diperbarui!");
                addFeedback.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");
            }
        });

        Button deleteBtn = createStyledButton("🗑 Hapus Pertandingan", "#e74c3c");
        deleteBtn.setOnAction(e -> {
            Match sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                MatchDatabase.deleteMatch(sel.getId());
                refreshScheduleTable(table);
                teamAField.clear(); teamBField.clear(); dateField.clear();
                editingId[0] = null;
                editBtn.setDisable(true);
                addFeedback.setText("Pertandingan dihapus.");
                addFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
            }
        });

        addForm.getChildren().addAll(addTitle, teamAField, teamBField, dateField, addBtnRow, deleteBtn, addFeedback);

        // --- Set Result Form ---
        VBox resultForm = new VBox(8);
        resultForm.setPadding(new Insets(15));
        resultForm.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #eeeef5; -fx-border-radius: 10px;");
        resultForm.setPrefWidth(300);

        Label resultTitle = new Label("🏆 Tentukan Hasil Pertandingan");
        resultTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
        resultTitle.setTextFill(Color.web("#1a1a2e"));

        Label selMatchLabel = new Label("Pilih pertandingan dari tabel.");
        selMatchLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-style: italic;");

        TextField scoreAField = new TextField();
        scoreAField.setPromptText("Skor Tim A");
        scoreAField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        TextField scoreBField = new TextField();
        scoreBField.setPromptText("Skor Tim B");
        scoreBField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label resultFeedback = new Label();
        resultFeedback.setStyle("-fx-font-size: 11px;");

        Button saveResultBtn = createStyledButton("Simpan Hasil", "#e67e22");

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selMatchLabel.setText("Terpilih: " + newVal.getTeamA() + " vs " + newVal.getTeamB());
                scoreAField.setText(String.valueOf(newVal.getScoreA()));
                scoreBField.setText(String.valueOf(newVal.getScoreB()));
            }
        });

        saveResultBtn.setOnAction(e -> {
            Match sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                resultFeedback.setText("Pilih pertandingan terlebih dahulu!");
                resultFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
                return;
            }
            try {
                int sA = Integer.parseInt(scoreAField.getText().trim());
                int sB = Integer.parseInt(scoreBField.getText().trim());
                sel.setScoreA(sA);
                sel.setScoreB(sB);
                sel.setStatus("Completed");
                if (sA > sB) {
                    sel.setWinner(sel.getTeamA());
                } else if (sB > sA) {
                    sel.setWinner(sel.getTeamB());
                } else {
                    sel.setWinner("Seri");
                }
                MatchDatabase.updateMatch(sel);
                refreshScheduleTable(table);
                resultFeedback.setText("Hasil pertandingan disimpan!");
                resultFeedback.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");
            } catch (NumberFormatException ex) {
                resultFeedback.setText("Skor harus berupa angka!");
                resultFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
            }
        });

        resultForm.getChildren().addAll(resultTitle, selMatchLabel, scoreAField, scoreBField, saveResultBtn, resultFeedback);

        formArea.getChildren().addAll(addForm, resultForm);

        panel.getChildren().addAll(title, table, formArea);
        contentArea.getChildren().add(panel);
    }

    private void refreshScheduleTable(TableView<Match> table) {
        ObservableList<Match> data = FXCollections.observableArrayList(MatchDatabase.getAllMatches());
        table.setItems(data);
        table.refresh();
    }

    // ======================== TEAMS & PLAYERS PANEL ========================

    @SuppressWarnings("unchecked")
    private void showTeamsPanel() {
        contentArea.getChildren().clear();

        VBox panel = new VBox(15);
        panel.setPadding(new Insets(30));

        Label title = new Label("👥 Kelola Tim & Pemain");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        // Search bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari pemain atau tim...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(35);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #dcdce6; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 5 12;");

        searchBar.getChildren().add(searchField);

        // Player table
        TableView<User> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> teamCol = new TableColumn<>("Nama Tim");
        teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, Boolean> bannedCol = new TableColumn<>("Status");
        bannedCol.setCellValueFactory(new PropertyValueFactory<>("banned"));
        bannedCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item) {
                        setText("🚫 Banned");
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setText("✅ Aktif");
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });

        table.getColumns().addAll(usernameCol, teamCol, roleCol, bannedCol);
        refreshPlayersTable(table, "");

        VBox.setVgrow(table, Priority.ALWAYS);

        // Search listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            refreshPlayersTable(table, newVal.trim().toLowerCase());
        });

        // Action panel
        HBox actionPanel = new HBox(12);
        actionPanel.setAlignment(Pos.TOP_LEFT);

        // Edit team name
        VBox editBox = new VBox(8);
        editBox.setPadding(new Insets(15));
        editBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #eeeef5; -fx-border-radius: 10px;");
        editBox.setPrefWidth(320);

        Label editTitle = new Label("✏ Edit Data Pemain");
        editTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
        editTitle.setTextFill(Color.web("#1a1a2e"));

        Label selectedPlayerLabel = new Label("Pilih pemain dari tabel.");
        selectedPlayerLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-style: italic;");

        TextField editTeamField = new TextField();
        editTeamField.setPromptText("Nama Tim Baru");
        editTeamField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label editFeedback = new Label();
        editFeedback.setStyle("-fx-font-size: 11px;");
        editFeedback.setWrapText(true);
        editFeedback.setMaxWidth(290);

        Button saveTeamBtn = createStyledButton("Simpan Tim", "#6c5ce7");

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedPlayerLabel.setText("Terpilih: " + newVal.getUsername());
                editTeamField.setText(newVal.getTeamName());
            }
        });

        saveTeamBtn.setOnAction(e -> {
            User sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sel.setTeamName(editTeamField.getText().trim());
                refreshPlayersTable(table, searchField.getText().trim().toLowerCase());
                editFeedback.setText("Nama tim berhasil diperbarui!");
                editFeedback.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");
            }
        });

        editBox.getChildren().addAll(editTitle, selectedPlayerLabel, editTeamField, saveTeamBtn, editFeedback);

        // Ban / Unban / Delete actions
        VBox actionBox = new VBox(8);
        actionBox.setPadding(new Insets(15));
        actionBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #eeeef5; -fx-border-radius: 10px;");
        actionBox.setPrefWidth(320);

        Label actionTitle = new Label("⚡ Aksi Pemain");
        actionTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
        actionTitle.setTextFill(Color.web("#1a1a2e"));

        Label actionFeedback = new Label();
        actionFeedback.setStyle("-fx-font-size: 11px;");
        actionFeedback.setWrapText(true);
        actionFeedback.setMaxWidth(290);

        Button banBtn = createStyledButton("🚫 Ban Akun", "#e74c3c");
        Button unbanBtn = createStyledButton("✅ Lepas Ban", "#27ae60");
        Button deleteBtn = createStyledButton("🗑 Hapus Akun", "#636e72");

        banBtn.setOnAction(e -> {
            User sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                if ("Admin".equalsIgnoreCase(sel.getRole())) {
                    actionFeedback.setText("Tidak bisa ban akun Admin!");
                    actionFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
                    return;
                }
                sel.setBanned(true);
                refreshPlayersTable(table, searchField.getText().trim().toLowerCase());
                actionFeedback.setText(sel.getUsername() + " telah di-banned.");
                actionFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
            }
        });

        unbanBtn.setOnAction(e -> {
            User sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sel.setBanned(false);
                refreshPlayersTable(table, searchField.getText().trim().toLowerCase());
                actionFeedback.setText(sel.getUsername() + " ban telah dicabut.");
                actionFeedback.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");
            }
        });

        deleteBtn.setOnAction(e -> {
            User sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                if ("Admin".equalsIgnoreCase(sel.getRole())) {
                    actionFeedback.setText("Tidak bisa hapus akun Admin!");
                    actionFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
                    return;
                }
                UserDatabase.deleteUser(sel.getUsername());
                refreshPlayersTable(table, searchField.getText().trim().toLowerCase());
                actionFeedback.setText(sel.getUsername() + " telah dihapus.");
                actionFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
            }
        });

        actionBox.getChildren().addAll(actionTitle, banBtn, unbanBtn, deleteBtn, actionFeedback);

        actionPanel.getChildren().addAll(editBox, actionBox);

        panel.getChildren().addAll(title, searchBar, table, actionPanel);
        contentArea.getChildren().add(panel);
    }

    private void refreshPlayersTable(TableView<User> table, String filter) {
        List<User> allPlayers = UserDatabase.getAllPlayers();
        List<User> filtered;
        if (filter == null || filter.isEmpty()) {
            filtered = allPlayers;
        } else {
            filtered = allPlayers.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(filter)
                    || (u.getTeamName() != null && u.getTeamName().toLowerCase().contains(filter)))
                .collect(Collectors.toList());
        }
        ObservableList<User> data = FXCollections.observableArrayList(filtered);
        table.setItems(data);
        table.refresh();
    }

    // ======================== UTILITIES ========================

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        btn.setMaxWidth(Double.MAX_VALUE);
        String baseStyle = "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 6px; -fx-cursor: hand;";
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(baseStyle + "-fx-opacity: 0.85;"));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }
}

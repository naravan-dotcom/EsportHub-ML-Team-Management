package esporthub.Player;

import esporthub.login.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Main Player Dashboard — contains the sidebar navigation and delegates
 * content rendering to individual panel classes:
 * {@link ProfilePanel}, {@link StatsPanel}, {@link HistoryPanel},
 * {@link TeamPanel}.
 */
public class PlayerDashboard extends HBox {

    private final StackPane contentArea;
    private final Runnable onLogout;
    private final User playerUser;
    private VBox lastActiveNavButton = null;
    private Label sidebarUserLabel;

    // Panel delegates
    private final ProfilePanel profilePanel;
    private final StatsPanel statsPanel;
    private final HistoryPanel historyPanel;
    private final TeamPanel teamPanel;

    public PlayerDashboard(User playerUser, Runnable onLogout) {
        super(0);
        this.playerUser = playerUser;
        this.onLogout = onLogout;

        setPrefSize(1020, 640);
        setMinSize(1020, 640);

        // ===== MAIN CONTENT AREA =====
        contentArea = new StackPane();
        contentArea.setStyle(DashboardUtils.CONTENT_BG);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Initialize panel delegates
        profilePanel = new ProfilePanel(playerUser, contentArea, null); // sidebarUserLabel set after sidebar creation
        statsPanel = new StatsPanel(playerUser, contentArea);
        historyPanel = new HistoryPanel(playerUser, contentArea);
        teamPanel = new TeamPanel(playerUser, contentArea, this::showProfilePanel);

        // ===== LEFT SIDEBAR =====
        VBox sidebar = createSidebar();

        // Now that sidebarUserLabel is created, re-initialize profilePanel with the
        // label
        profilePanel.setSidebarUserLabel(sidebarUserLabel);

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
        sidebar.setStyle(DashboardUtils.SIDEBAR_BG);

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
            if (onLogout != null)
                onLogout.run();
        });

        sidebar.getChildren().addAll(
                logo, hubLabel, sidebarUserLabel,
                spacer1, sep, spacer2,
                navProfile, navStats, navHistory, navTeam,
                bottomSpacer, navLogout);

        lastActiveNavButton = navProfile;
        navProfile.setStyle(DashboardUtils.NAV_ACTIVE);

        return sidebar;
    }

    private VBox createNavButton(String icon, String text, Runnable action) {
        VBox btn = new VBox();
        btn.setPadding(new Insets(10, 15, 10, 15));
        btn.setStyle(DashboardUtils.NAV_NORMAL);

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
                btn.setStyle(
                        "-fx-background-color: rgba(168, 85, 247, 0.12); -fx-background-radius: 8px; -fx-cursor: hand;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != lastActiveNavButton) {
                btn.setStyle(DashboardUtils.NAV_NORMAL);
            }
        });
        btn.setOnMouseClicked(e -> {
            if (lastActiveNavButton != null)
                lastActiveNavButton.setStyle(DashboardUtils.NAV_NORMAL);
            btn.setStyle(DashboardUtils.NAV_ACTIVE);
            lastActiveNavButton = btn;
            action.run();
        });

        return btn;
    }

    // ======================== PANEL NAVIGATION ========================

    private void showProfilePanel() {
        profilePanel.show();
    }

    private void showStatsPanel() {
        statsPanel.show();
    }

    private void showHistoryPanel() {
        historyPanel.show();
    }

    private void showTeamPanel() {
        teamPanel.show();
    }
}

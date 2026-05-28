package esporthub.Player;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Shared styling constants and utility methods for the Player Dashboard panels.
 */
public final class DashboardUtils {

    // Styling constants matching AdminDashboard
    public static final String SIDEBAR_BG = "-fx-background-color: linear-gradient(to bottom, #0f0c29, #1b1642);";
    public static final String NAV_NORMAL = "-fx-background-color: transparent; -fx-background-radius: 8px; -fx-cursor: hand;";
    public static final String NAV_ACTIVE = "-fx-background-color: rgba(168, 85, 247, 0.25); -fx-background-radius: 8px; -fx-cursor: hand;";
    public static final String CONTENT_BG = "-fx-background-color: #f0f0f5;";

    private DashboardUtils() {
        // Utility class — no instantiation
    }

    /**
     * Creates a styled button with the given text and accent color.
     */
    public static Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        String baseStyle = "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 6px; -fx-cursor: hand;";
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(baseStyle + "-fx-opacity: 0.85;"));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    /**
     * Adds a label-value row to a GridPane.
     */
    public static void addGridRow(GridPane grid, int row, String label, String value) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label val = new Label(value);
        val.setStyle("-fx-text-fill: #1a1a2e; -fx-font-size: 14px; -fx-font-weight: bold;");
        grid.add(lbl, 0, row);
        grid.add(val, 1, row);
    }
}

package esporthub.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.effect.DropShadow;

import java.util.function.Consumer;

public class LoginForm extends HBox {

    private final TextField userField;
    private final PasswordField passField;
    private final TextField visiblePassField;
    private final Label feedbackLabel;
    private final CheckBox showPasswordCheckBox;
    private final Button loginButton;
    private final Button toRegisterButton;

    public LoginForm(Runnable onRegisterRequested, Consumer<User> onLoginSuccess) {
        super(0);
        setPrefSize(850, 500);
        setMinSize(850, 500);
        setMaxSize(850, 500);

        // ====== LEFT HERO BANNER ======
        VBox heroBanner = createHeroBanner();

        // ====== RIGHT FORM PANEL ======
        VBox formPanel = new VBox(14);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(40, 45, 40, 45));
        formPanel.setPrefWidth(420);
        formPanel.setMinWidth(420);
        formPanel.setStyle("-fx-background-color: #ffffff;");

        Label titleLabel = new Label("Masuk ke Akun");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#1a1a2e"));

        Label subtitleLabel = new Label("Silakan masuk dengan akun Anda");
        subtitleLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        // Form fields
        VBox formFields = new VBox(10);
        formFields.setAlignment(Pos.CENTER_LEFT);
        formFields.setMaxWidth(300);

        Label userLabel = new Label("Username / ID");
        userLabel.setStyle("-fx-text-fill: #444444; -fx-font-weight: bold; -fx-font-size: 12px;");
        userField = new TextField();
        userField.setPromptText("Masukkan Username / ID");
        userField.setPrefHeight(38);
        userField.setStyle("-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8 12;");

        Label passLabel = new Label("Password");
        passLabel.setStyle("-fx-text-fill: #444444; -fx-font-weight: bold; -fx-font-size: 12px;");
        passField = new PasswordField();
        visiblePassField = new TextField();
        passField.setPrefHeight(38);
        visiblePassField.setPrefHeight(38);
        String fieldStyle = "-fx-background-color: #f5f5f9; -fx-border-color: #dcdce6; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8 12;";
        passField.setStyle(fieldStyle);
        visiblePassField.setStyle(fieldStyle);

        StackPane passwordPane = createPasswordPane(passField, visiblePassField, "Masukkan Password");
        showPasswordCheckBox = createShowPasswordToggle(passField, visiblePassField);
        showPasswordCheckBox.setStyle("-fx-text-fill: #777777; -fx-font-size: 11px;");

        formFields.getChildren().addAll(userLabel, userField, passLabel, passwordPane, showPasswordCheckBox);

        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 12px;");
        feedbackLabel.setWrapText(true);
        feedbackLabel.setMaxWidth(300);

        loginButton = new Button("Login");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(40);
        loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #6c5ce7, #a855f7);" +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;" +
            "-fx-background-radius: 8px; -fx-cursor: hand;"
        );
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #5a4bd1, #9333ea);" +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;" +
            "-fx-background-radius: 8px; -fx-cursor: hand;"
        ));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #6c5ce7, #a855f7);" +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;" +
            "-fx-background-radius: 8px; -fx-cursor: hand;"
        ));

        HBox registerRow = new HBox(5);
        registerRow.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Belum punya akun?");
        noAccountLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");
        toRegisterButton = new Button("Daftar Sekarang");
        toRegisterButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #6c5ce7; -fx-font-weight: bold; -fx-font-size: 12px; -fx-underline: true; -fx-border-color: transparent; -fx-cursor: hand;");
        registerRow.getChildren().addAll(noAccountLabel, toRegisterButton);

        loginButton.setOnAction(e -> handleLogin(onLoginSuccess));
        toRegisterButton.setOnAction(e -> {
            clearFields();
            onRegisterRequested.run();
        });

        Region spacer1 = new Region();
        spacer1.setPrefHeight(5);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(5);

        formPanel.getChildren().addAll(titleLabel, subtitleLabel, spacer1, formFields, feedbackLabel, spacer2, loginButton, registerRow);

        getChildren().addAll(heroBanner, formPanel);
    }

    private VBox createHeroBanner() {
        VBox banner = new VBox(15);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(40));
        banner.setPrefWidth(430);
        banner.setMinWidth(430);
        banner.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #0f0c29, #302b63, #24243e);"
        );

        Text logoText = new Text("🎮");
        logoText.setFont(Font.font("System", 50));

        Text brandName = new Text("EsportHub");
        brandName.setFont(Font.font("System", FontWeight.BOLD, 32));
        brandName.setFill(Color.WHITE);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#a855f7", 0.6));
        glow.setRadius(15);
        brandName.setEffect(glow);

        Text tagline = new Text("Platform Manajemen\nTurnamen Esports #1");
        tagline.setFont(Font.font("System", FontWeight.NORMAL, 14));
        tagline.setFill(Color.web("#b8b8d4"));
        tagline.setTextAlignment(TextAlignment.CENTER);

        Region spacer = new Region();
        spacer.setPrefHeight(20);

        // Decorative stats
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
            createStatBox("500+", "Pemain"),
            createStatBox("50+", "Tim"),
            createStatBox("120+", "Turnamen")
        );

        Text footerText = new Text("© 2026 EsportHub. All rights reserved.");
        footerText.setFont(Font.font("System", 10));
        footerText.setFill(Color.web("#555577"));

        banner.getChildren().addAll(logoText, brandName, tagline, spacer, statsRow, footerText);
        return banner;
    }

    private VBox createStatBox(String number, String label) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 15, 10, 15));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 10px;");

        Text numText = new Text(number);
        numText.setFont(Font.font("System", FontWeight.BOLD, 18));
        numText.setFill(Color.web("#a855f7"));

        Text lblText = new Text(label);
        lblText.setFont(Font.font("System", 11));
        lblText.setFill(Color.web("#9999bb"));

        box.getChildren().addAll(numText, lblText);
        return box;
    }

    private void handleLogin(Consumer<User> onLoginSuccess) {
        String username = userField.getText().trim();
        String password = passField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText("Username dan password tidak boleh kosong!");
            feedbackLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 12px;");
            return;
        }

        User user = UserDatabase.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            if (user.isBanned()) {
                feedbackLabel.setText("Akun Anda telah di-banned! Hubungi admin.");
                feedbackLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 12px;");
                return;
            }
            feedbackLabel.setText("Login Berhasil!");
            feedbackLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 12px;");
            userField.clear();
            passField.clear();
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(user);
            }
        } else {
            feedbackLabel.setText("Username atau Password salah!");
            feedbackLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 12px;");
        }
    }

    private StackPane createPasswordPane(PasswordField passwordField, TextField visiblePasswordField, String promptText) {
        passwordField.setPromptText(promptText);
        visiblePasswordField.setPromptText(promptText);
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        StackPane passwordPane = new StackPane(passwordField, visiblePasswordField);
        return passwordPane;
    }

    private CheckBox createShowPasswordToggle(PasswordField passwordField, TextField visiblePasswordField) {
        CheckBox showPasswordCheckBox = new CheckBox("Tampilkan password");
        showPasswordCheckBox.setStyle("-fx-text-fill: #555555;");
        showPasswordCheckBox.setOnAction(e -> {
            boolean showPassword = showPasswordCheckBox.isSelected();
            visiblePasswordField.setVisible(showPassword);
            visiblePasswordField.setManaged(showPassword);
            passwordField.setVisible(!showPassword);
            passwordField.setManaged(!showPassword);

            if (showPassword) {
                visiblePasswordField.requestFocus();
                visiblePasswordField.positionCaret(visiblePasswordField.getText().length());
            } else {
                passwordField.requestFocus();
                passwordField.positionCaret(passwordField.getText().length());
            }
        });

        return showPasswordCheckBox;
    }

    public void clearFields() {
        userField.clear();
        passField.clear();
        showPasswordCheckBox.setSelected(false);
        visiblePassField.setVisible(false);
        visiblePassField.setManaged(false);
        passField.setVisible(true);
        passField.setManaged(true);
        feedbackLabel.setText("");
    }

    public void setSuccessMessage(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 12px;");
    }
}

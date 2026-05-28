package esporthub;

import esporthub.admin.AdminDashboard;
import esporthub.login.LoginForm;
import esporthub.login.RegisterForm;
import esporthub.login.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene registerScene;

    private LoginForm loginForm;
    private RegisterForm registerForm;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        
        // Build Forms
        loginForm = new LoginForm(
            // When registration is requested
            () -> switchScene(registerScene, "EsportHub - Daftar Akun"),
            // When login succeeds
            (User user) -> {
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    showAdminDashboard(user);
                } else {
                    // Player login — for now show success message on login form
                    // Future: transition to player dashboard
                }
            }
        );

        registerForm = new RegisterForm(
            // When back to login is requested
            () -> switchScene(loginScene, "EsportHub - Login"),
            // When registration succeeds
            (successMsg) -> {
                switchScene(loginScene, "EsportHub - Login");
                loginForm.setSuccessMessage(successMsg);
            }
        );

        // Build Scenes — Landscape ratio (850x500)
        loginScene = new Scene(loginForm, 850, 500);
        registerScene = new Scene(registerForm, 850, 500);

        // Show main login screen
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("EsportHub - Login");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showAdminDashboard(User adminUser) {
        AdminDashboard dashboard = new AdminDashboard(adminUser, () -> {
            // Logout callback — return to login
            loginForm.clearFields();
            stage.setScene(loginScene);
            stage.setTitle("EsportHub - Login");
            stage.setWidth(850);
            stage.setHeight(540); // account for title bar
            stage.centerOnScreen();
        });

        Scene adminScene = new Scene(dashboard, 1020, 640);
        stage.setScene(adminScene);
        stage.setTitle("EsportHub - Admin Dashboard");
        stage.setWidth(1020);
        stage.setHeight(680); // account for title bar
        stage.centerOnScreen();
    }

    private void switchScene(Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

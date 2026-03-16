package controller;

import database.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    // On instancie directement pour éviter le NullPointerException au logout/login
    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleLogin() {
        String login = loginField.getText();
        String pass = passwordField.getText();

        // Plus de risque de null ici
        if (userRepository.checkLogin(login, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent dashboardRoot = loader.load();

                Scene dashboardScene = new Scene(dashboardRoot);
                Stage stage = (Stage) loginField.getScene().getWindow();

                stage.setScene(dashboardScene);
                stage.setTitle("Aéroclub - Tableau de bord");
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                messageLabel.setText("Erreur de chargement du dashboard");
                e.printStackTrace();
            }
        } else {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Identifiant ou mot de passe incorrect.");
        }
    }
}
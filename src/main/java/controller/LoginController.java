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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleLogin() {
        String login = loginField.getText();
        String pass = passwordField.getText();

        // Récupération du rôle
        String userRights = userRepository.getUserRights(login, pass);

        if (userRights != null) {

            // --- CONDITION DE RESTRICTION ---
            if ("utilisateur".equalsIgnoreCase(userRights)) {
                redirectToWebsite("https://francoisl.fr/projects/aeroclub/");
                messageLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                messageLabel.setText("Accès gestion réservé. Redirection web...");
                return;
            }

            // --- ACCÈS AUTORISÉ (Administrateur ou Consulteur) ---
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent dashboardRoot = loader.load();

                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(new Scene(dashboardRoot));
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

    /**
     * Ouvre le navigateur par défaut vers l'URL spécifiée
     */
    private void redirectToWebsite(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            System.err.println("Erreur redirection : " + e.getMessage());
        }
    }
}
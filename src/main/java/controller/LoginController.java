package controller;

import database.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button btnCreerCompte;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleLogin() {
        String login = loginField.getText();
        String pass = passwordField.getText();

        Map<String, String> userData = userRepository.getUserInfo(login, pass);

        if (userData != null) {
            String userRights = userData.get("role");
            String prenom = userData.get("prenom");
            String etat_val_compte = userData.get("etat_val_compte");

            if ("a_valider".equalsIgnoreCase(etat_val_compte)) {
                messageLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                messageLabel.setText("Compte en attente de validation, veuillez patienter.");
                return;
            }

            if ("utilisateur".equalsIgnoreCase(userRights)) {
                redirectToWebsite("https://francoisl.fr/projects/aeroclub/");
                messageLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                messageLabel.setText("Accès gestion réservé. Redirection web...");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent dashboardRoot = loader.load();
                DashboardController dashCtrl = loader.getController();
                dashCtrl.setUserInfo(prenom, userRights);

                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(new Scene(dashboardRoot));
                stage.setTitle("Aéroclub - Tableau de bord");
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
                messageLabel.setText("Erreur de chargement du dashboard");
                e.printStackTrace();
            }
        } else {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Identifiant ou mot de passe incorrect.");
        }
    }

    @FXML
    private void handleCreerCompte() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inscription.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCreerCompte.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription - Aéroclub");
            stage.show();
        } catch (IOException e) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Erreur : Impossible d'ouvrir la page d'inscription.");
            e.printStackTrace();
        }
    }

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
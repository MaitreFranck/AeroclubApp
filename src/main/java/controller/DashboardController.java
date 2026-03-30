package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {

    @FXML private Label welcomeLabel; // C'est le label gris en bas
    @FXML private Label userDisplayLabel; // Nouveau Label pour le nom (à ajouter dans le FXML)
    @FXML private BorderPane mainPane;

    /**
     * Cette méthode sera appelée par le LoginController pour passer les infos
     */
    public void setUserInfo(String prenom, String role) {
        // On transforme "administrateur" en "ADMIN" pour le style
        String roleDisplay = role.toUpperCase();
        userDisplayLabel.setText("Bienvenue, " + prenom + " [" + roleDisplay + "]");

        // Optionnel : Changer la couleur selon le rôle
        if (role.equalsIgnoreCase("administrateur")) {
            userDisplayLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
        }
    }

    // Tes méthodes de navigation restent les mêmes...
    @FXML private void showAvionsPage() { loadPage("/view/avions.fxml"); }
    @FXML private void showMembresPage() { loadPage("/view/membres.fxml"); }
    @FXML private void showVolsPage() { loadPage("/view/vols.fxml"); }
    @FXML private void showReservationsPage() { loadPage("/view/reservations.fxml"); }
    @FXML private void showOperationsPage() { loadPage("/view/operations.fxml"); }
    @FXML private void showCotisationsPage() { loadPage("/view/cotisations.fxml"); }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Aéroclub - Connexion");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
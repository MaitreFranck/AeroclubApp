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

    @FXML private Label welcomeLabel;
    @FXML private BorderPane mainPane;

    // Méthodes de navigation
    @FXML private void showAvionsPage() { loadPage("/view/avions.fxml"); }
    @FXML private void showMembresPage() { loadPage("/view/membres.fxml"); }
    @FXML private void showVolsPage() { loadPage("/view/vols.fxml"); }
    @FXML private void showReservationsPage() { loadPage("/view/reservations.fxml"); }
    @FXML private void showOperationsPage() { loadPage("/view/operations.fxml"); }
    @FXML private void showCotisationsPage() { loadPage("/view/cotisations.fxml"); }

    /**
     * Méthode générique pour charger une page FXML au centre du BorderPane
     */
    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            System.err.println("Erreur de chargement de la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            // Utilisation du mainPane pour récupérer la fenêtre (plus fiable)
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
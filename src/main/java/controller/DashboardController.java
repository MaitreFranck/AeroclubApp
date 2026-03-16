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

    @FXML
    private Label welcomeLabel;

    @FXML
    private BorderPane mainPane; // Assure-toi d'avoir mis fx:id="mainPane" sur ton BorderPane dans dashboard.fxml

    @FXML
    private void showMembresPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/membres.fxml"));
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Recharger la page de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Aéroclub - Connexion");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
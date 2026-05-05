package controller;

import database.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class InscriptionController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker dateNaisPicker;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleInscription() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        LocalDate dateNais = dateNaisPicker.getValue();
        String pass = passwordField.getText();

        // Vérification des champs vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pass.isEmpty() || dateNais == null) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        // Vérification du format de l'e-mail
        if (!isValidEmail(email)) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Format d'e-mail invalide.");
            return;
        }

        boolean success = userRepository.saveUser(nom, prenom, email, phone, dateNais, pass);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Inscription réussie ! Votre compte est en attente de validation.");
            alert.showAndWait();
            handleRetour();
        } else {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Erreur : L'email est déjà utilisé.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    @FXML
    private void handleRetour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Aéroclub - Authentification");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
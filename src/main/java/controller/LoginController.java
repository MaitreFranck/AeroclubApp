package controller;

import database.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UserRepository userRepository;

    public void setUserRepository(UserRepository repo) {
        this.userRepository = repo;
    }

    @FXML
    private void handleLogin() {
        String login = loginField.getText();
        String pass = passwordField.getText();

        if (login.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (userRepository != null && userRepository.checkLogin(login, pass)) {
            messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            messageLabel.setText("Connexion réussie !");

            System.out.println("Accès autorisé pour : " + login);
        } else {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Identifiant ou mot de passe incorrect.");
        }
    }
}

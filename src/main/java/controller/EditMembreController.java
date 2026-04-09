package controller;

import database.MembreRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Membre;

public class EditMembreController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telField;
    @FXML private TextField licenceField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<String> droitsCombo;
    @FXML private Spinner<Double> soldeSpinner;

    private Membre membre;
    private boolean saveClicked = false;
    private final MembreRepository repo = new MembreRepository();

    @FXML
    public void initialize() {
        typeCombo.getItems().addAll("pilote", "eleve", "instructeur", "admin");
        statutCombo.getItems().addAll("actif", "inactif");
        droitsCombo.getItems().addAll("utilisateur", "consulteur", "administrateur");

        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-10000.0, 10000.0, 0.0, 10.0);
        soldeSpinner.setValueFactory(valueFactory);
        soldeSpinner.setEditable(true);
    }

    public void setMembre(Membre membre) {
        this.membre = membre;

        nomField.setText(membre.getNom());
        prenomField.setText(membre.getPrenom());
        emailField.setText(membre.getEmail());
        telField.setText(membre.getTelephone());
        licenceField.setText(membre.getNumeroLicence());
        dateNaissancePicker.setValue(membre.getDateNaissance());
        typeCombo.setValue(membre.getTypeMembre());
        statutCombo.setValue(membre.getStatut());
        droitsCombo.setValue(membre.getDroitsUtilisateurs());
        soldeSpinner.getValueFactory().setValue(membre.getSoldeCompte());
    }

    public boolean isSaveClicked() { return saveClicked; }

    @FXML
    private void handleSave() {
        if (nomField.getText().isEmpty() || emailField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Le nom et l'email sont obligatoires.").show();
            return;
        }

        membre.setNom(nomField.getText());
        membre.setPrenom(prenomField.getText());
        membre.setEmail(emailField.getText());
        membre.setTelephone(telField.getText());
        membre.setNumeroLicence(licenceField.getText());
        membre.setDateNaissance(dateNaissancePicker.getValue());
        membre.setTypeMembre(typeCombo.getValue());
        membre.setStatut(statutCombo.getValue());
        membre.setDroitsUtilisateurs(droitsCombo.getValue());
        membre.setSoldeCompte(soldeSpinner.getValue());

        if (!passwordField.getText().isEmpty()) {
            membre.setMotDePasse(passwordField.getText());
        }

        boolean success = (membre.getId() == 0) ? repo.addMembre(membre) : repo.updateMembre(membre);

        if (success) {
            saveClicked = true;
            closeWindow();
        } else {
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la sauvegarde.").show();
        }
    }

    @FXML private void handleCancel() { closeWindow(); }

    private void closeWindow() {
        ((Stage) nomField.getScene().getWindow()).close();
    }
}
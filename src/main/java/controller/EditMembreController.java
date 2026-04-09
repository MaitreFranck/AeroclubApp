package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Membre;

import java.time.LocalDate;

public class EditMembreController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telField;
    @FXML private TextField licenceField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<String> droitsCombo;
    @FXML private Spinner<Double> soldeSpinner;

    private Membre membre;
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        // Initialisation des listes déroulantes
        typeCombo.getItems().addAll("pilote", "eleve", "instructeur", "admin");
        statutCombo.getItems().addAll("actif", "inactif");
        droitsCombo.getItems().addAll("utilisateur", "consulteur", "administrateur");

        // Configuration du spinner pour le solde (min, max, initial, step)
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-10000.0, 10000.0, 0.0, 10.0);
        soldeSpinner.setValueFactory(valueFactory);
        soldeSpinner.setEditable(true);
    }

    /**
     * Remplit les champs avec les données du membre sélectionné
     */
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

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        // Validation simple
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez remplir les champs obligatoires");
            alert.showAndWait();
            return;
        }

        // Mise à jour de l'objet membre
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

        saveClicked = true;
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}
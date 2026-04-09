package controller;

import database.MembreRepository;
import database.OperationRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Membre;

public class EditOperationController {
    @FXML private ComboBox<Membre> membreCombo;
    @FXML private ComboBox<String> typeCombo; // 'debit', 'credit'
    @FXML private ComboBox<String> modeCombo; // 'CB', 'Chèque', etc.
    @FXML private TextField montantField;
    @FXML private TextArea commentaireArea;

    private final MembreRepository membreRepo = new MembreRepository();
    private final OperationRepository opRepo = new OperationRepository();
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        // Charger les membres
        membreCombo.getItems().setAll(membreRepo.getAllMembres());

        // Configurer l'affichage des membres dans la liste
        membreCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Membre m) { return m == null ? "" : m.getNom() + " " + m.getPrenom(); }
            @Override public Membre fromString(String s) { return null; }
        });

        // Remplir les types et modes
        typeCombo.getItems().addAll("credit", "debit");
        modeCombo.getItems().addAll("CB", "Chèque", "Espèces", "Virement", "Prélèvement Vol");
    }

    public boolean isSaveClicked() { return saveClicked; }

    @FXML
    private void handleSave() {
        try {
            Membre m = membreCombo.getValue();
            String type = typeCombo.getValue();
            String mode = modeCombo.getValue();
            double montant = Double.parseDouble(montantField.getText());

            if (m == null || type == null || mode == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Si c'est un débit, on s'assure que le montant enregistré en base est négatif
            double montantFinal = type.equals("debit") ? -Math.abs(montant) : Math.abs(montant);

            if (opRepo.addOperation(m.getId(), montantFinal, type, mode)) {
                saveClicked = true;
                closeWindow();
            }
        } catch (NumberFormatException e) {
            showAlert("Format invalide", "Le montant doit être un nombre.");
        }
    }

    @FXML private void handleCancel() { closeWindow(); }

    private void closeWindow() {
        ((Stage) membreCombo.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
package controller;

import database.CotisationRepository;
import database.MembreRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Membre;

public class EditCotisationController {
    @FXML private ComboBox<Membre> membreCombo;
    @FXML private TextField anneeField;
    @FXML private TextField montantField;
    @FXML private ComboBox<String> modeCombo;

    private final CotisationRepository cotisRepo = new CotisationRepository();
    private final MembreRepository membreRepo = new MembreRepository();
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        membreCombo.getItems().setAll(membreRepo.getAllMembres());
        membreCombo.setConverter(new StringConverter<Membre>() {
            @Override public String toString(Membre m) { return m == null ? "" : m.getNom() + " " + m.getPrenom(); }
            @Override public Membre fromString(String s) { return null; }
        });

        modeCombo.getItems().addAll("CB", "Chèque", "Espèces", "Virement");
        modeCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSave() {
        Membre m = membreCombo.getValue();
        if (m == null) return;

        try {
            int annee = Integer.parseInt(anneeField.getText());
            double montant = Double.parseDouble(montantField.getText());
            String mode = modeCombo.getValue();

            if (cotisRepo.enregistrerPaiementCotis(m.getId(), annee, montant, mode)) {
                saveClicked = true;
                handleCancel();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur : Le membre a déjà payé ou l'ID est invalide.").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Données invalides.").show();
        }
    }

    @FXML private void handleCancel() {
        ((Stage) membreCombo.getScene().getWindow()).close();
    }

    public boolean isSaveClicked() { return saveClicked; }
}
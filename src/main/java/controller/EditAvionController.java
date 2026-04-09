package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Avion;

public class EditAvionController {

    @FXML private TextField immatField;
    @FXML private TextField modeleField;
    @FXML private Spinner<Integer> catSpinner;
    @FXML private ComboBox<String> statutCombo;
    @FXML private Spinner<Double> heuresSpinner;

    private Avion avion;
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        // Liste des statuts possibles pour l'avion
        statutCombo.getItems().addAll("Disponible", "En maintenance", "Hors service", "École uniquement");

        // Config Spinner Catégorie (ID de 1 à 10 par exemple)
        catSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        // Config Spinner Heures (0 à 50000h, pas de 0.1)
        SpinnerValueFactory<Double> hoursFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 50000.0, 0.0, 0.1);
        heuresSpinner.setValueFactory(hoursFactory);
        heuresSpinner.setEditable(true);
    }

    public void setAvion(Avion avion) {
        this.avion = avion;

        immatField.setText(avion.getImmatriculation());
        modeleField.setText(avion.getModele());
        catSpinner.getValueFactory().setValue(avion.getIdCategorie());
        statutCombo.setValue(avion.getStatut());
        heuresSpinner.getValueFactory().setValue(avion.getHeuresVol());
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (immatField.getText().isEmpty() || modeleField.getText().isEmpty()) {
            return; // On pourrait ajouter une alerte ici
        }

        // Mise à jour de l'objet
        avion.setImmatriculation(immatField.getText());
        avion.setModele(modeleField.getText());
        avion.setIdCategorie(catSpinner.getValue());
        avion.setStatut(statutCombo.getValue());
        avion.setHeuresVol(heuresSpinner.getValue());

        saveClicked = true;
        close();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) immatField.getScene().getWindow();
        stage.close();
    }
}
package controller;

import database.AvionRepository;
import database.MembreRepository;
import database.ReservationRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Avion;
import model.Membre;
import model.Reservation;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class EditReservationController {

    @FXML private ComboBox<Membre> membreCombo;
    @FXML private ComboBox<Avion> avionCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField debutField;
    @FXML private TextField finField;
    @FXML private ComboBox<String> statutCombo;

    private Reservation reservation;
    private boolean saveClicked = false;
    private final MembreRepository membreRepo = new MembreRepository();
    private final AvionRepository avionRepo = new AvionRepository();
    private final ReservationRepository resRepo = new ReservationRepository();

    @FXML
    public void initialize() {
        membreCombo.getItems().setAll(membreRepo.getAllMembres());
        avionCombo.getItems().setAll(avionRepo.getAllAvions());

        // CORRECTION : Utilisation des valeurs exactes attendues par la base de données
        statutCombo.getItems().addAll("confirmee", "annulee");

        setupFormatters();
    }

    private void setupFormatters() {
        membreCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Membre m) { return m == null ? "" : m.getNom() + " " + m.getPrenom(); }
            @Override public Membre fromString(String s) { return null; }
        });
        avionCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Avion a) { return a == null ? "" : a.getImmatriculation(); }
            @Override public Avion fromString(String s) { return null; }
        });
    }

    public void setReservation(Reservation res) {
        this.reservation = res;
        datePicker.setValue(res.getDate());
        debutField.setText(res.getHeureDebut().toString());
        finField.setText(res.getHeureFin().toString());

        // Protection si la valeur initiale n'est pas dans la liste
        if (res.getStatut() != null && (res.getStatut().equals("confirmee") || res.getStatut().equals("annulee"))) {
            statutCombo.setValue(res.getStatut());
        } else {
            statutCombo.setValue("confirmee");
        }

        if (res.getId() != 0) {
            membreCombo.getItems().stream()
                    .filter(m -> m.getNom().equals(res.getNomMembre()))
                    .findFirst().ifPresent(membreCombo::setValue);

            avionCombo.getItems().stream()
                    .filter(a -> a.getImmatriculation().equals(res.getImmatriculationAvion()))
                    .findFirst().ifPresent(avionCombo::setValue);
        }
    }

    public boolean isSaveClicked() { return saveClicked; }

    @FXML
    private void handleSave() {
        try {
            Membre m = membreCombo.getValue();
            Avion a = avionCombo.getValue();

            if (m == null || a == null || datePicker.getValue() == null || statutCombo.getValue() == null) {
                showAlert("Erreur", "Tous les champs sont obligatoires.");
                return;
            }

            reservation.setDate(datePicker.getValue());
            reservation.setHeureDebut(LocalTime.parse(debutField.getText()));
            reservation.setHeureFin(LocalTime.parse(finField.getText()));
            reservation.setStatut(statutCombo.getValue());

            boolean success;
            if (reservation.getId() == 0) {
                // AJOUT
                success = resRepo.addReservation(m.getId(), a.getId(), reservation);
            } else {
                // MODIFICATION (On passe maintenant les IDs du membre et de l'avion)
                success = resRepo.updateReservation(m.getId(), a.getId(), reservation);
            }

            if (success) {
                saveClicked = true;
                close();
            } else {
                showAlert("Erreur SQL", "La mise à jour a échoué en base de données.");
            }
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'heure invalide (HH:mm)");
        }
    }

    @FXML private void handleCancel() { close(); }

    private void close() { ((Stage) debutField.getScene().getWindow()).close(); }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
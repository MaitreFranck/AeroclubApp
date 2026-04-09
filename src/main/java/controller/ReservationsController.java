package controller;

import database.ReservationRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Reservation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class ReservationsController {
    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, LocalDate> colDate;
    @FXML private TableColumn<Reservation, String> colAvion;
    @FXML private TableColumn<Reservation, String> colMembre;
    @FXML private TableColumn<Reservation, LocalTime> colDebut;
    @FXML private TableColumn<Reservation, LocalTime> colFin;
    @FXML private TableColumn<Reservation, String> colStatut;
    @FXML private TextField searchField;

    private final ReservationRepository repo = new ReservationRepository();
    private final ObservableList<Reservation> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAvion.setCellValueFactory(new PropertyValueFactory<>("immatriculationAvion"));
        colMembre.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        setupSearch();
        loadReservations();
    }

    @FXML
    public void loadReservations() {
        masterData.setAll(repo.getAllReservations());
    }

    private void setupSearch() {
        FilteredList<Reservation> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(res -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase();
                return res.getNomMembre().toLowerCase().contains(filter) ||
                        res.getImmatriculationAvion().toLowerCase().contains(filter);
            });
        });
        reservationsTable.setItems(filteredData);
    }

    @FXML
    private void handleAddReservation() {
        Reservation newRes = new Reservation(0, "", "", LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), "confirmee");
        openDialog(newRes, "Nouvelle Réservation");
    }

    @FXML
    private void handleEditReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openDialog(selected, "Modifier Réservation");
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                if (repo.deleteReservation(selected.getId())) loadReservations();
            }
        }
    }

    private void openDialog(Reservation res, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_reservation.fxml"));
            Parent root = loader.load();
            EditReservationController controller = loader.getController();
            controller.setReservation(res);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(reservationsTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaveClicked()) loadReservations();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
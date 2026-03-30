package controller;

import database.ReservationRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationsController {
    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, LocalDate> colDate;
    @FXML private TableColumn<Reservation, String> colAvion;
    @FXML private TableColumn<Reservation, String> colMembre;
    @FXML private TableColumn<Reservation, LocalTime> colDebut;
    @FXML private TableColumn<Reservation, LocalTime> colFin;
    @FXML private TableColumn<Reservation, String> colStatut;

    private final ReservationRepository repo = new ReservationRepository();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAvion.setCellValueFactory(new PropertyValueFactory<>("immatriculationAvion"));
        colMembre.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        reservationsTable.setItems(FXCollections.observableArrayList(repo.getAllReservations()));
    }
}
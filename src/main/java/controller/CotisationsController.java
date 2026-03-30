package controller;

import database.CotisationRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Cotisation;
import java.time.LocalDate;

public class CotisationsController {
    @FXML private TableView<Cotisation> cotisationsTable;
    @FXML private TableColumn<Cotisation, String> colMembre;
    @FXML private TableColumn<Cotisation, Integer> colAnnee;
    @FXML private TableColumn<Cotisation, LocalDate> colDate;
    @FXML private TableColumn<Cotisation, Double> colMontant;

    private final CotisationRepository repo = new CotisationRepository();

    @FXML
    public void initialize() {
        colMembre.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));

        cotisationsTable.setItems(FXCollections.observableArrayList(repo.getAllCotisations()));
    }
}
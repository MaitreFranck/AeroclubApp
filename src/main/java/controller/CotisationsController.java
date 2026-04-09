package controller;

import database.CotisationRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cotisation;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CotisationsController {
    @FXML private TableView<Cotisation> cotisationsTable;
    @FXML private TableColumn<Cotisation, String> colMembre;
    @FXML private TableColumn<Cotisation, Integer> colAnnee;
    @FXML private TableColumn<Cotisation, LocalDate> colDate;
    @FXML private TableColumn<Cotisation, Double> colMontant;

    private final CotisationRepository repo = new CotisationRepository();
    private final ObservableList<Cotisation> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colMembre.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));

        setupFormatters();
        loadData();
    }

    private void setupFormatters() {
        colDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else if (item == null) {
                    setText("NON PAYÉ");
                    setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    setStyle("-fx-text-fill: #27ae60;");
                }
            }
        });
    }

    @FXML
    public void loadData() {
        masterData.setAll(repo.getCotisationsByAnnee(LocalDate.now().getYear()));
        cotisationsTable.setItems(masterData);
    }

    @FXML
    private void handleOpenSaisie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_cotisation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nouveau Paiement");
            stage.initModality(Modality.APPLICATION_MODAL);
            // On lie la fenêtre à la scène actuelle pour le focus
            stage.initOwner(cotisationsTable.getScene().getWindow());
            stage.setScene(new Scene(root));

            EditCotisationController ctrl = loader.getController();
            stage.showAndWait();

            if (ctrl.isSaveClicked()) {
                loadData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
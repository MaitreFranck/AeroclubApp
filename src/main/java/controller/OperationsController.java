package controller;

import database.OperationRepository;
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
import model.Operation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OperationsController {

    @FXML private TableView<Operation> operationsTable;
    @FXML private TableColumn<Operation, Integer> colId;
    @FXML private TableColumn<Operation, String> colMembre;
    @FXML private TableColumn<Operation, LocalDateTime> colDate;
    @FXML private TableColumn<Operation, Double> colMontant;
    @FXML private TableColumn<Operation, String> colType;
    @FXML private TableColumn<Operation, String> colMode;

    private final OperationRepository repo = new OperationRepository();
    private final ObservableList<Operation> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Liaison des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMembre.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateOperation"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMode.setCellValueFactory(new PropertyValueFactory<>("modePaiement"));

        // Formatage de la date
        colDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });

        // Formatage des couleurs du montant
        colMontant.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f €", item));
                    if (item < 0) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });

        loadOperations();
    }

    @FXML
    public void loadOperations() {
        masterData.setAll(repo.getAllOperations());
        operationsTable.setItems(masterData);
    }

    /**
     * Cette méthode règle ton erreur LoadException
     */
    @FXML
    private void handleAddOperation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_operation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nouvelle Opération financière");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(operationsTable.getScene().getWindow());
            stage.setScene(new Scene(root));

            stage.showAndWait();

            // Rafraîchir le tableau après la fermeture de la fenêtre
            loadOperations();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la fenêtre de saisie.");
            alert.showAndWait();
        }
    }
}
package controller;

import database.OperationRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Operation;
import java.time.LocalDateTime;

public class OperationsController {
    @FXML private TableView<Operation> operationsTable;
    @FXML private TableColumn<Operation, Integer> colId;
    @FXML private TableColumn<Operation, String> colMembre;
    @FXML private TableColumn<Operation, LocalDateTime> colDate;
    @FXML private TableColumn<Operation, Double> colMontant;
    @FXML private TableColumn<Operation, String> colType;
    @FXML private TableColumn<Operation, String> colMode;

    private final OperationRepository repo = new OperationRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMembre.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateOperation"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMode.setCellValueFactory(new PropertyValueFactory<>("modePaiement"));

        operationsTable.setItems(FXCollections.observableArrayList(repo.getAllOperations()));
    }
}
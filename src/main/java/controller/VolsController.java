package controller;

import database.VolRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Vol;
import java.time.LocalDateTime;

public class VolsController {
    @FXML private TableView<Vol> volsTable;
    @FXML private TableColumn<Vol, Integer> colId;
    @FXML private TableColumn<Vol, String> colAvion;
    @FXML private TableColumn<Vol, String> colPilote;
    @FXML private TableColumn<Vol, LocalDateTime> colDepart;
    @FXML private TableColumn<Vol, LocalDateTime> colArrivee;
    @FXML private TableColumn<Vol, Double> colDuree;

    private final VolRepository volRepo = new VolRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAvion.setCellValueFactory(new PropertyValueFactory<>("immatriculationAvion"));
        colPilote.setCellValueFactory(new PropertyValueFactory<>("nomPilote"));
        colDepart.setCellValueFactory(new PropertyValueFactory<>("dateHeureDepart"));
        colArrivee.setCellValueFactory(new PropertyValueFactory<>("dateHeureArrivee"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));

        volsTable.setItems(FXCollections.observableArrayList(volRepo.getAllVols()));
    }
}
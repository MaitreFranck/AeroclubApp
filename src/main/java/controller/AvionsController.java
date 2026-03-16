package controller;

import database.AvionRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Avion;

public class AvionsController {
    @FXML private TableView<Avion> avionsTable;
    @FXML private TableColumn<Avion, Integer> colId;
    @FXML private TableColumn<Avion, String> colImmat;
    @FXML private TableColumn<Avion, String> colModele;
    @FXML private TableColumn<Avion, Integer> colCat; // Correspond au fx:id="colCat"
    @FXML private TableColumn<Avion, String> colStatut;
    @FXML private TableColumn<Avion, Double> colHeures;

    private final AvionRepository avionRepo = new AvionRepository();

    @FXML
    public void initialize() {
        // Vérification de sécurité pour éviter le NullPointerException
        if (colCat == null) {
            System.err.println("Erreur : colCat n'est pas injecté. Vérifiez le fx:id dans avions.fxml");
            return;
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colImmat.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("idCategorie"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colHeures.setCellValueFactory(new PropertyValueFactory<>("heuresVol"));

        loadAvions();
    }

    private void loadAvions() {
        try {
            avionsTable.setItems(FXCollections.observableArrayList(avionRepo.getAllAvions()));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des avions : " + e.getMessage());
        }
    }
}
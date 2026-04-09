package controller;

import database.AvionRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Avion;

import java.io.IOException;

public class AvionsController {
    @FXML private TableView<Avion> avionsTable;
    @FXML private TableColumn<Avion, Integer> colId;
    @FXML private TableColumn<Avion, String> colImmat;
    @FXML private TableColumn<Avion, String> colModele;
    @FXML private TableColumn<Avion, Integer> colCat;
    @FXML private TableColumn<Avion, String> colStatut;
    @FXML private TableColumn<Avion, Double> colHeures;

    // Ajout du champ de recherche
    @FXML private TextField searchField;

    private final AvionRepository avionRepo = new AvionRepository();
    private final ObservableList<Avion> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
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

        // On charge les données et on configure la recherche
        loadAvions();
        setupSearch();
    }

    @FXML
    public void loadAvions() {
        try {
            masterData.setAll(avionRepo.getAllAvions());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des avions : " + e.getMessage());
        }
    }

    private void setupSearch() {
        FilteredList<Avion> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(avion -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase().trim();

                if (avion.getImmatriculation().toLowerCase().contains(filter)) return true;
                if (avion.getModele().toLowerCase().contains(filter)) return true;
                return false;
            });
        });

        avionsTable.setItems(filteredData);
    }

    @FXML
    private void handleEditAvion() {
        Avion selected = avionsTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_avion.fxml"));
                Parent root = loader.load();

                EditAvionController controller = loader.getController();
                controller.setAvion(selected);

                Stage stage = new Stage();
                stage.setTitle("Modifier l'avion : " + selected.getImmatriculation());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(avionsTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isSaveClicked()) {
                    if (avionRepo.updateAvion(selected)) {
                        avionsTable.refresh();
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur chargement fenêtre édition avion : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
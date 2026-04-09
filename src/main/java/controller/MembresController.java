package controller;

import database.MembreRepository;
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
import model.Membre;

import java.io.IOException;

public class MembresController {

    @FXML private TableView<Membre> membresTable;
    @FXML private TableColumn<Membre, Integer> colId;
    @FXML private TableColumn<Membre, String> colNom;
    @FXML private TableColumn<Membre, String> colPrenom;
    @FXML private TableColumn<Membre, String> colEmail;
    @FXML private TableColumn<Membre, String> colType;
    @FXML private TableColumn<Membre, String> colStatut;
    @FXML private TableColumn<Membre, Double> colSolde;
    @FXML private TableColumn<Membre, String> colDroits;
    @FXML private TableColumn<Membre, String> colLicence;

    @FXML private TextField searchField;

    private final MembreRepository membreRepo = new MembreRepository();

    // On utilise masterData pour stocker les données brutes de la DB
    private final ObservableList<Membre> masterData = FXCollections.observableArrayList();
    // On utilise filteredData pour l'affichage filtré
    private FilteredList<Membre> filteredData;

    @FXML
    public void initialize() {
        // 1. Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeMembre"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("soldeCompte"));
        colDroits.setCellValueFactory(new PropertyValueFactory<>("droitsUtilisateurs"));
        colLicence.setCellValueFactory(new PropertyValueFactory<>("numeroLicence"));

        // 2. Initialisation de la liste filtrée
        filteredData = new FilteredList<>(masterData, p -> true);

        // 3. Liaison de la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(membre -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                if (membre.getNom().toLowerCase().contains(lowerCaseFilter)) return true;
                if (membre.getPrenom().toLowerCase().contains(lowerCaseFilter)) return true;
                if (membre.getEmail().toLowerCase().contains(lowerCaseFilter)) return true;

                return false;
            });
        });

        // 4. On fixe la liste FILTRÉE au TableView (Crucial !)
        membresTable.setItems(filteredData);

        // 5. Premier chargement
        loadMembres();
    }

    @FXML
    public void loadMembres() {
        // On met à jour masterData, filteredData réagira automatiquement
        masterData.setAll(membreRepo.getAllMembres());
    }

    @FXML
    private void handleEditMembre() {
        Membre selectedMembre = membresTable.getSelectionModel().getSelectedItem();

        if (selectedMembre != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_membre.fxml"));
                Parent root = loader.load();

                EditMembreController controller = loader.getController();
                controller.setMembre(selectedMembre);

                Stage stage = new Stage();
                stage.setTitle("Modifier le membre : " + selectedMembre.getNom());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(membresTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isSaveClicked()) {
                    if (membreRepo.updateMembre(selectedMembre)) {
                        membresTable.refresh();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
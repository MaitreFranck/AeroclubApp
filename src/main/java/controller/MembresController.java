package controller;

import database.MembreRepository;
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
import model.Membre;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class MembresController {
    @FXML private TableView<Membre> membresTable;
    @FXML private TableColumn<Membre, Integer> colId;
    @FXML private TableColumn<Membre, String> colNom;
    @FXML private TableColumn<Membre, String> colPrenom;
    @FXML private TableColumn<Membre, String> colEmail;
    @FXML private TableColumn<Membre, String> colLicence;
    @FXML private TableColumn<Membre, String> colType;
    @FXML private TableColumn<Membre, String> colStatut;
    @FXML private TableColumn<Membre, Double> colSolde;
    @FXML private TableColumn<Membre, String> colDroits;

    @FXML private TextField searchField;

    private final MembreRepository repo = new MembreRepository();
    private final ObservableList<Membre> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colLicence.setCellValueFactory(new PropertyValueFactory<>("numeroLicence"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeMembre"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDroits.setCellValueFactory(new PropertyValueFactory<>("droitsUtilisateurs"));

        // Formatage spécial pour le solde (€)
        colSolde.setCellValueFactory(new PropertyValueFactory<>("soldeCompte"));
        colSolde.setCellFactory(column -> new TableCell<Membre, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", item));
                    // Optionnel : afficher en rouge si solde négatif
                    if (item < 0) setStyle("-fx-text-fill: red;");
                    else setStyle("-fx-text-fill: green;");
                }
            }
        });

        loadMembres();
    }

    @FXML
    public void loadMembres() {
        masterData.setAll(repo.getAllMembres());
        membresTable.setItems(masterData);
    }

    @FXML
    private void handleAddMembre() {
        Membre nouveau = new Membre(0, "", "", "", "", LocalDate.now(), "", "pilote", "actif", 0.0, "utilisateur", "");
        openEditDialog(nouveau, "Nouveau Membre");
    }

    @FXML
    private void handleEditMembre() {
        Membre selected = membresTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditDialog(selected, "Modifier Membre");
        }
    }

    @FXML
    private void handleDeleteMembre() {
        Membre selected = membresTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + selected.getNom() + " ?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                if (repo.deleteMembre(selected.getId())) loadMembres();
            }
        }
    }

    private void openEditDialog(Membre m, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_membre.fxml"));
            Parent root = loader.load();
            EditMembreController controller = loader.getController();
            controller.setMembre(m);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(membresTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaveClicked()) loadMembres();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
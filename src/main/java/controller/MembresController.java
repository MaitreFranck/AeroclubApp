package controller;

import database.MembreRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Membre;

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

    private final MembreRepository membreRepo = new MembreRepository();

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les attributs de la classe model.Membre
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeMembre"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("soldeCompte"));
        colDroits.setCellValueFactory(new PropertyValueFactory<>("droitsUtilisateurs"));
        colLicence.setCellValueFactory(new PropertyValueFactory<>("numeroLicence"));

        loadMembres();
    }

    @FXML
    private void loadMembres() {
        ObservableList<Membre> data = FXCollections.observableArrayList(membreRepo.getAllMembres());
        membresTable.setItems(data);
    }
}
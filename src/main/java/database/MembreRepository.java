package database;

import model.Membre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreRepository {

    // Récupérer tous les membres (Lecture)
    public List<Membre> getAllMembres() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getAllMembres: " + e.getMessage());
        }
        return membres;
    }

    // Ajouter un nouveau membre (Création)
    public boolean addMembre(Membre m, String password) {
        String sql = "INSERT INTO membres (nom, prenom, email, telephone, date_naissance, " +
                "numero_licence, type_membre, statut, solde_compte, droits_utilisateurs, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            prepareStatement(pstmt, m, password);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mettre à jour un membre existant (Update)
    public boolean updateMembre(Membre m) {
        // On ne met pas à jour le password ici pour éviter de l'écraser par du vide
        String sql = "UPDATE membres SET nom=?, prenom=?, email=?, telephone=?, date_naissance=?, " +
                "numero_licence=?, type_membre=?, statut=?, solde_compte=?, droits_utilisateurs=? " +
                "WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int nextIndex = prepareStatement(pstmt, m, null);
            pstmt.setInt(nextIndex, m.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un membre (Delete)
    public boolean deleteMembre(int id) {
        String sql = "DELETE FROM membres WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Méthodes utilitaires privées ---

    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        return new Membre(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null,
                rs.getString("numero_licence"),
                rs.getString("type_membre"),
                rs.getString("statut"),
                rs.getDouble("solde_compte"),
                rs.getString("droits_utilisateurs")
        );
    }

    private int prepareStatement(PreparedStatement pstmt, Membre m, String password) throws SQLException {
        pstmt.setString(1, m.getNom());
        pstmt.setString(2, m.getPrenom());
        pstmt.setString(3, m.getEmail());
        pstmt.setString(4, m.getTelephone());
        pstmt.setDate(5, m.getDateNaissance() != null ? Date.valueOf(m.getDateNaissance()) : null);
        pstmt.setString(6, m.getNumeroLicence());
        pstmt.setString(7, m.getTypeMembre());
        pstmt.setString(8, m.getStatut());
        pstmt.setDouble(9, m.getSoldeCompte());
        pstmt.setString(10, m.getDroitsUtilisateurs());

        if (password != null) {
            pstmt.setString(11, password);
            return 12;
        }
        return 11; // Retourne l'index pour le WHERE id=? de l'update
    }
}
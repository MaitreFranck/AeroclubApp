package database;

import model.Membre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreRepository {

    public List<Membre> getAllMembres() {
        return getMembresByQuery("SELECT * FROM membres");
    }

    public List<Membre> getMembresByEtat(String etat) {
        if (etat == null || "Tous".equals(etat)) return getAllMembres();
        String sql = "SELECT * FROM membres WHERE etat_val_compte = ?";
        List<Membre> membres = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etat);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { membres.add(mapResultSetToMembre(rs)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return membres;
    }

    public boolean updateEtatMembre(int id, String nouvelEtat) {
        String sql = "UPDATE membres SET etat_val_compte = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nouvelEtat);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private List<Membre> getMembresByQuery(String sql) {
        List<Membre> membres = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { membres.add(mapResultSetToMembre(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return membres;
    }

    public boolean addMembre(Membre m) {
        String sql = "INSERT INTO membres (nom, prenom, email, telephone, date_naissance, numero_licence, type_membre, statut, solde_compte, droits_utilisateurs, password, etat_val_compte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            fillPreparedStatement(pstmt, m);
            pstmt.setString(11, m.getMotDePasse());
            pstmt.setString(12, m.getEtatValCompte());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateMembre(Membre m) {
        boolean updatePass = m.getMotDePasse() != null && !m.getMotDePasse().isEmpty();
        String sql = "UPDATE membres SET nom=?, prenom=?, email=?, telephone=?, date_naissance=?, numero_licence=?, type_membre=?, statut=?, solde_compte=?, droits_utilisateurs=?, etat_val_compte=?" + (updatePass ? ", password=?" : "") + " WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            fillPreparedStatement(pstmt, m);
            pstmt.setString(11, m.getEtatValCompte());
            int idx = 12;
            if (updatePass) pstmt.setString(idx++, m.getMotDePasse());
            pstmt.setInt(idx, m.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteMembre(int id) {
        String sql = "DELETE FROM membres WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        return new Membre(
                rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"),
                rs.getString("telephone"), rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null,
                rs.getString("numero_licence"), rs.getString("type_membre"), rs.getString("statut"),
                rs.getDouble("solde_compte"), rs.getString("droits_utilisateurs"), rs.getString("password"),
                rs.getString("etat_val_compte")
        );
    }

    private void fillPreparedStatement(PreparedStatement pstmt, Membre m) throws SQLException {
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
    }
}
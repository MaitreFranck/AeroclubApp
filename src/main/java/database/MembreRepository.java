package database;

import model.Membre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreRepository {

    public List<Membre> getAllMembres() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { membres.add(mapResultSetToMembre(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return membres;
    }

    public boolean addMembre(Membre m) {
        String sql = "INSERT INTO membres (nom, prenom, email, telephone, date_naissance, numero_licence, type_membre, statut, solde_compte, droits_utilisateurs, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            fillPreparedStatement(pstmt, m);
            pstmt.setString(11, m.getMotDePasse());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateMembre(Membre m) {
        boolean updatePass = m.getMotDePasse() != null && !m.getMotDePasse().isEmpty();
        String sql = "UPDATE membres SET nom=?, prenom=?, email=?, telephone=?, date_naissance=?, numero_licence=?, type_membre=?, statut=?, solde_compte=?, droits_utilisateurs=?" + (updatePass ? ", password=?" : "") + " WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            fillPreparedStatement(pstmt, m);
            int idx = 11;
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
                rs.getDouble("solde_compte"), rs.getString("droits_utilisateurs"), rs.getString("password")
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
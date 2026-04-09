package database;

import model.Avion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvionRepository {

    // Récupérer tous les avions
    public List<Avion> getAllAvions() {
        List<Avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM avions";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                avions.add(new Avion(
                        rs.getInt("id"),
                        rs.getString("immatriculation"),
                        rs.getString("modele"),
                        rs.getInt("id_categorie"),
                        rs.getString("statut"),
                        rs.getDouble("heures_vol")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avions;
    }

    // Mettre à jour un avion (Indispensable pour l'Atelier)
    public boolean updateAvion(Avion a) {
        String sql = "UPDATE avions SET immatriculation=?, modele=?, id_categorie=?, statut=?, heures_vol=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, a.getImmatriculation());
            pstmt.setString(2, a.getModele());
            pstmt.setInt(3, a.getIdCategorie());
            pstmt.setString(4, a.getStatut());
            pstmt.setDouble(5, a.getHeuresVol());
            pstmt.setInt(6, a.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ajouter un nouvel avion
    public boolean addAvion(Avion a) {
        String sql = "INSERT INTO avions (immatriculation, modele, id_categorie, statut, heures_vol) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, a.getImmatriculation());
            pstmt.setString(2, a.getModele());
            pstmt.setInt(3, a.getIdCategorie());
            pstmt.setString(4, a.getStatut());
            pstmt.setDouble(5, a.getHeuresVol());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
package database;

import model.Vol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolRepository {

    public List<Vol> getAllVols() {
        List<Vol> vols = new ArrayList<>();
        String sql = "SELECT v.id, a.immatriculation, m.nom, v.date_heure_depart, v.date_heure_arrivee, v.duree " +
                "FROM vols v " +
                "JOIN avions a ON v.id_avion = a.id " +
                "JOIN membres m ON v.id_pilote = m.id " +
                "ORDER BY v.date_heure_depart DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vols.add(new Vol(
                        rs.getInt("id"),
                        rs.getString("immatriculation"),
                        rs.getString("nom"),
                        rs.getTimestamp("date_heure_depart").toLocalDateTime(),
                        rs.getTimestamp("date_heure_arrivee").toLocalDateTime(),
                        rs.getDouble("duree")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vols;
    }
}
package database;

import model.Cotisation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CotisationRepository {

    public List<Cotisation> getAllCotisations() {
        List<Cotisation> list = new ArrayList<>();
        String sql = "SELECT c.id, m.nom, c.date_paiement, c.montant, c.annee " +
                "FROM cotisations c " +
                "JOIN membres m ON c.id_membre = m.id " +
                "ORDER BY c.annee DESC, m.nom ASC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Cotisation(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getDouble("montant"),
                        rs.getInt("annee")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
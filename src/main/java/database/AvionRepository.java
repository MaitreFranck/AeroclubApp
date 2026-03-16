package database;

import model.Avion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvionRepository {

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
}
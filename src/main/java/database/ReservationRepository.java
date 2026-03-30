package database;

import model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        // Requête avec jointures pour avoir l'immat de l'avion et le nom du membre
        String sql = "SELECT r.id, a.immatriculation, m.nom, r.date_reservation, r.heure_debut, r.heure_fin, r.statut " +
                "FROM reservations r " +
                "JOIN avions a ON r.id_avion = a.id " +
                "JOIN membres m ON r.id_membre = m.id " +
                "ORDER BY r.date_reservation DESC, r.heure_debut ASC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("immatriculation"),
                        rs.getString("nom"),
                        rs.getDate("date_reservation").toLocalDate(),
                        rs.getTime("heure_debut").toLocalTime(),
                        rs.getTime("heure_fin").toLocalTime(),
                        rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
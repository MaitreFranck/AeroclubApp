package database;

import model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
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

    public boolean addReservation(int idMembre, int idAvion, Reservation r) {
        String sql = "INSERT INTO reservations (id_membre, id_avion, date_reservation, heure_debut, heure_fin, statut) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMembre);
            pstmt.setInt(2, idAvion);
            pstmt.setDate(3, Date.valueOf(r.getDate()));
            pstmt.setTime(4, Time.valueOf(r.getHeureDebut()));
            pstmt.setTime(5, Time.valueOf(r.getHeureFin()));
            pstmt.setString(6, r.getStatut());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateReservation(Reservation r) {
        String sql = "UPDATE reservations SET date_reservation=?, heure_debut=?, heure_fin=?, statut=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(r.getDate()));
            pstmt.setTime(2, Time.valueOf(r.getHeureDebut()));
            pstmt.setTime(3, Time.valueOf(r.getHeureFin()));
            pstmt.setString(4, r.getStatut());
            pstmt.setInt(5, r.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- LA MÉTHODE MANQUANTE ---
    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
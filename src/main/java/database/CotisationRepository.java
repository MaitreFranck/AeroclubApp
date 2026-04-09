package database;

import model.Cotisation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CotisationRepository {

    // On récupère toutes les cotisations pour une année précise
    public List<Cotisation> getCotisationsByAnnee(int annee) {
        List<Cotisation> list = new ArrayList<>();
        // Jointure pour avoir tous les membres, même ceux sans cotisation (LEFT JOIN)
        String sql = "SELECT m.nom, m.prenom, c.id, c.date_paiement, c.montant, c.annee " +
                "FROM membres m " +
                "LEFT JOIN cotisations c ON m.id = c.id_membre AND c.annee = ? " +
                "WHERE m.statut = 'actif' " +
                "ORDER BY m.nom ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, annee);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomComplet = rs.getString("nom") + " " + rs.getString("prenom");
                Date d = rs.getDate("date_paiement");

                list.add(new Cotisation(
                        rs.getInt("id"), // Sera 0 si pas de cotisation trouvée
                        nomComplet,
                        d != null ? d.toLocalDate() : null,
                        rs.getDouble("montant"),
                        annee
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Pour ton test actuel ou vue globale
    public List<Cotisation> getAllCotisations() {
        return getCotisationsByAnnee(2026); // Par défaut 2026
    }

    public boolean enregistrerPaiementCotis(int idMembre, int annee, double montant, String mode) {
        String sqlCotis = "INSERT INTO cotisations (id_membre, date_paiement, montant, annee) VALUES (?, CURRENT_DATE, ?, ?)";
        String sqlOp = "INSERT INTO operations (id_membre, date_op, montant, type_op, mode_paiement) VALUES (?, CURRENT_TIMESTAMP, ?, 'credit', ?)";
        String sqlUpdateSolde = "UPDATE membres SET solde_compte = solde_compte + ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlCotis);
                 PreparedStatement ps2 = conn.prepareStatement(sqlOp);
                 PreparedStatement ps3 = conn.prepareStatement(sqlUpdateSolde)) {

                // 1. Table Cotisations
                ps1.setInt(1, idMembre);
                ps1.setDouble(2, montant);
                ps1.setInt(3, annee);
                ps1.executeUpdate();

                // 2. Table Operations (Journal)
                ps2.setInt(1, idMembre);
                ps2.setDouble(2, montant);
                ps2.setString(3, mode + " (Cotis " + annee + ")");
                ps2.executeUpdate();

                // 3. Update Solde Membre
                ps3.setDouble(1, montant);
                ps3.setInt(2, idMembre);
                ps3.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
package database;

import model.Operation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationRepository {

    public List<Operation> getAllOperations() {
        List<Operation> operations = new ArrayList<>();

        // Correction 1 : Utilisation des noms réels de ta base (date_op, type_op)
        // Correction 2 : Utilisation de || pour la concaténation (standard H2)
        String sql = "SELECT o.*, (m.nom || ' ' || m.prenom) as nom_complet " +
                "FROM operations o " +
                "JOIN membres m ON o.id_membre = m.id " +
                "ORDER BY o.date_op DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                operations.add(new Operation(
                        rs.getInt("id"),
                        rs.getString("nom_complet"),
                        rs.getTimestamp("date_op").toLocalDateTime(), // Match date_op
                        rs.getDouble("montant"),
                        rs.getString("type_op"), // Match type_op
                        rs.getString("mode_paiement")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    public boolean addOperation(int idMembre, double montant, String type, String mode) {
        // Match avec tes colonnes : date_op, type_op
        String sqlOp = "INSERT INTO operations (id_membre, date_op, montant, type_op, mode_paiement) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?)";
        String sqlUpdateMembre = "UPDATE membres SET solde_compte = solde_compte + ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psOp = conn.prepareStatement(sqlOp);
                 PreparedStatement psUp = conn.prepareStatement(sqlUpdateMembre)) {

                psOp.setInt(1, idMembre);
                psOp.setDouble(2, montant);
                psOp.setString(3, type);
                psOp.setString(4, mode);
                psOp.executeUpdate();

                psUp.setDouble(1, montant);
                psUp.setInt(2, idMembre);
                psUp.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
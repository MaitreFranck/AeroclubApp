package database;

import model.Operation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationRepository {

    public List<Operation> getAllOperations() {
        List<Operation> list = new ArrayList<>();
        String sql = "SELECT o.id, m.nom, o.date_op, o.montant, o.type_op, o.mode_paiement " +
                "FROM operations o " +
                "JOIN membres m ON o.id_membre = m.id " +
                "ORDER BY o.date_op DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Operation(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getTimestamp("date_op").toLocalDateTime(),
                        rs.getDouble("montant"),
                        rs.getString("type_op"),
                        rs.getString("mode_paiement")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
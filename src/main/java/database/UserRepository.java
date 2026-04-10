package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class UserRepository {

    public Map<String, String> getUserInfo(String email, String passwordClaire) {
        String sql = "SELECT prenom, droits_utilisateurs, password FROM membres WHERE email = ? AND statut = 'actif'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    if (BCrypt.checkpw(passwordClaire, storedHash)) {
                        Map<String, String> data = new HashMap<>();
                        data.put("prenom", rs.getString("prenom"));
                        data.put("role", rs.getString("droits_utilisateurs"));
                        return data;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
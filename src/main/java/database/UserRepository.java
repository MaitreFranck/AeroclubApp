package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    /**
     * Vérifie les identifiants et retourne le rôle de l'utilisateur.
     * Utilise la table 'membres' et la colonne 'email' comme identifiant.
     */
    public String getUserRights(String email, String password) {
        // On vérifie l'email, le mot de passe ET que le membre est actif
        String sql = "SELECT droits_utilisateurs FROM membres WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("droits_utilisateurs");
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB Error] Erreur lors de la vérification : " + e.getMessage());
        }
        return null; // Retourne null si les identifiants sont faux ou le compte inactif
    }

    /**
     * Ajoute un membre avec les droits par défaut 'utilisateur'
     */
    public void addUser(String nom, String prenom, String email, String password) {
        String sql = "INSERT INTO membres (nom, prenom, email, password, droits_utilisateurs, statut) VALUES (?, ?, ?, ?, 'utilisateur', 'actif')";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, email);
            pstmt.setString(4, password);

            pstmt.executeUpdate();
            System.out.println("[DB] Membre '" + email + "' créé avec succès.");

        } catch (SQLException e) {
            System.err.println("[DB Error] Impossible d'ajouter le membre : " + e.getMessage());
        }
    }
    public Map<String, String> getUserInfo(String email, String password) {
        String sql = "SELECT prenom, droits_utilisateurs FROM membres WHERE email = ? AND password = ? AND statut = 'actif'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, String> data = new HashMap<>();
                    data.put("prenom", rs.getString("prenom"));
                    data.put("role", rs.getString("droits_utilisateurs"));
                    return data;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
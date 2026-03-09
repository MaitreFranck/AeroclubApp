package database;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public void addUser(String login, String password) {
        String sql = "INSERT INTO utilisateurs (login, password) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            pstmt.executeUpdate();
            System.out.println("[DB] Utilisateur '" + login + "' créé avec succès.");

        } catch (SQLException e) {
            System.err.println("[DB Error] Impossible d'ajouter l'utilisateur : " + e.getMessage());
        }
    }

    public boolean checkLogin(String login, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE login = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("[DB Error] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }
}
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
        String sql = "SELECT prenom, droits_utilisateurs, password, etat_val_compte FROM membres WHERE email = ? AND statut = 'actif'";

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
                        data.put("etat_val_compte", rs.getString("etat_val_compte"));
                        return data;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveUser(String nom, String prenom, String email, String phone, java.time.LocalDate dateNais, String passClaire) {
        String checkEmailSql = "SELECT COUNT(*) FROM membres WHERE email = ?";

        String insertMembre = "INSERT INTO membres (nom, prenom, email, telephone, date_naissance, password, numero_licence, droits_utilisateurs, statut, solde_compte, created_at, etat_val_compte) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'utilisateur', 'inactif', 0.0, CURRENT_TIMESTAMP, 'a_valider')";

        String insertCotisation = "INSERT INTO cotisations (id_membre, type, montant, date_debut, date_fin, statut) " +
                "VALUES (?, 'cotisation', 120.00, ?, ?, 'impaye')";

        int currentYear = java.time.LocalDate.now().getYear();

        try (java.sql.Connection conn = database.DatabaseManager.getConnection()) {

            try (java.sql.PreparedStatement pstmtCheck = conn.prepareStatement(checkEmailSql)) {
                pstmtCheck.setString(1, email);
                try (java.sql.ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            conn.setAutoCommit(false);

            String numeroLicence = "L" + java.util.UUID.randomUUID().toString().substring(0, 7).toUpperCase();

            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(insertMembre, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, nom);
                pstmt.setString(2, prenom);
                pstmt.setString(3, email);
                pstmt.setString(4, phone);
                pstmt.setDate(5, java.sql.Date.valueOf(dateNais));
                pstmt.setString(6, org.mindrot.jbcrypt.BCrypt.hashpw(passClaire, org.mindrot.jbcrypt.BCrypt.gensalt()));
                pstmt.setString(7, numeroLicence);
                pstmt.executeUpdate();

                try (java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idMembre = generatedKeys.getInt(1);

                        try (java.sql.PreparedStatement pstmtCot = conn.prepareStatement(insertCotisation)) {
                            pstmtCot.setInt(1, idMembre);
                            pstmtCot.setDate(2, java.sql.Date.valueOf(currentYear + "-01-01"));
                            pstmtCot.setDate(3, java.sql.Date.valueOf(currentYear + "-12-31"));
                            pstmtCot.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
            return true;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
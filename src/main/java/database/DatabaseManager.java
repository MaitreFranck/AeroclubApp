package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./aeroclub_db";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        // 1. Création de la table membres avec la structure de ta prod
        String createMembresSQL = "CREATE TABLE IF NOT EXISTS membres (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "prenom VARCHAR(100) NOT NULL, " +
                "email VARCHAR(150) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "telephone VARCHAR(20), " +
                "date_naissance DATE, " +
                "numero_licence VARCHAR(50), " +
                "type_membre ENUM('pilote', 'eleve', 'instructeur', 'admin') DEFAULT 'pilote', " +
                "statut ENUM('actif', 'inactif') DEFAULT 'actif', " +
                "solde_compte DECIMAL(10,2) DEFAULT 0.00, " +
                "droits_utilisateurs ENUM('utilisateur', 'consulteur', 'administrateur') DEFAULT 'utilisateur'" +
                ");";

        // 2. Création de la table avions (pour anticiper la suite)
        String createAvionsSQL = "CREATE TABLE IF NOT EXISTS avions (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "immatriculation VARCHAR(20) UNIQUE NOT NULL, " +
                "modele VARCHAR(100) NOT NULL, " +
                "statut ENUM('disponible', 'maintenance', 'indisponible') DEFAULT 'disponible', " +
                "heures_vol DECIMAL(8,1) DEFAULT 0.0" +
                ");";

        // 3. Insertion de l'admin de test dans la table membres
        // Note : On utilise 'admin' comme email ici car ton LoginController utilise l'email pour se connecter
        String insertAdminSQL = "INSERT INTO membres (nom, prenom, email, password, droits_utilisateurs, statut) " +
                "SELECT 'Admin', 'Aeroclub', 'admin', '123', 'administrateur', 'actif' " +
                "WHERE NOT EXISTS (SELECT 1 FROM membres WHERE email = 'admin');";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Exécution des créations
            stmt.execute(createMembresSQL);
            stmt.execute(createAvionsSQL);

            // Insertion des données de base
            int rowsAffected = stmt.executeUpdate(insertAdminSQL);

            if (rowsAffected > 0) {
                System.out.println("[DB] Table membres creee et compte 'admin' genere.");
            } else {
                System.out.println("[DB] Table membres prete.");
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR DB] Impossible d'initialiser : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
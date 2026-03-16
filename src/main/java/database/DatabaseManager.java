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
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Création des tables de base (Membres)
            stmt.execute("CREATE TABLE IF NOT EXISTS membres (" +
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
                    ");");

            // 2. Création/Correction de la table avions
            stmt.execute("CREATE TABLE IF NOT EXISTS avions (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "immatriculation VARCHAR(20) UNIQUE NOT NULL, " +
                    "modele VARCHAR(100) NOT NULL, " +
                    "statut ENUM('disponible', 'maintenance', 'indisponible') DEFAULT 'disponible', " +
                    "heures_vol DECIMAL(8,1) DEFAULT 0.0" +
                    ");");

            // Correction dynamique : Ajout de la colonne manquante si nécessaire
            try {
                stmt.execute("ALTER TABLE avions ADD COLUMN id_categorie INT NOT NULL DEFAULT 1;");
                System.out.println("[DB] Colonne id_categorie ajoutée à la table avions.");
            } catch (SQLException e) {
                // La colonne existe déjà probablement, on ignore l'erreur
            }

            // 3. Insertion de l'admin par défaut
            stmt.executeUpdate("INSERT INTO membres (nom, prenom, email, password, droits_utilisateurs, statut) " +
                    "SELECT 'Admin', 'Aeroclub', 'admin', '123', 'administrateur', 'actif' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM membres WHERE email = 'admin');");

            // 4. Insertion des données réelles du fichier SQL
            // On sépare les inserts pour plus de fiabilité
            String[] avionData = {
                    "INSERT INTO avions (id, immatriculation, modele, id_categorie, statut, heures_vol) SELECT 1, 'F-ABC1', 'Cessna 152', 1, 'disponible', 1520.4 WHERE NOT EXISTS (SELECT 1 FROM avions WHERE id=1)",
                    "INSERT INTO avions (id, immatriculation, modele, id_categorie, statut, heures_vol) SELECT 2, 'F-ABC2', 'Piper PA-28', 1, 'maintenance', 2340.9 WHERE NOT EXISTS (SELECT 1 FROM avions WHERE id=2)",
                    "INSERT INTO avions (id, immatriculation, modele, id_categorie, statut, heures_vol) SELECT 3, 'F-XYZ1', 'Beechcraft Baron', 2, 'disponible', 980.3 WHERE NOT EXISTS (SELECT 1 FROM avions WHERE id=3)",
                    "INSERT INTO avions (id, immatriculation, modele, id_categorie, statut, heures_vol) SELECT 4, 'F-ULM3', 'Dynamic WT9', 3, 'disponible', 430.7 WHERE NOT EXISTS (SELECT 1 FROM avions WHERE id=4)"
            };

            for (String sql : avionData) {
                stmt.execute(sql);
            }

            System.out.println("[DB] Initialisation complète avec données de vol.");

        } catch (SQLException e) {
            System.err.println("[ERREUR DB] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
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

            // À ajouter dans DatabaseManager.initDatabase()
            String createResaSQL = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_avion INT, " +
                    "id_membre INT, " +
                    "date_reservation DATE, " +
                    "heure_debut TIME, " +
                    "heure_fin TIME, " +
                    "statut ENUM('confirmee','annulee') DEFAULT 'confirmee');";

            String insertResaData = "INSERT INTO reservations (id_avion, id_membre, date_reservation, heure_debut, heure_fin, statut) " +
                    "SELECT 1, 1, '2026-04-10', '09:00:00', '11:00:00', 'confirmee' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM reservations WHERE id=1);";

            stmt.execute(createResaSQL);
            stmt.execute(insertResaData);

            String createOpSQL = "CREATE TABLE IF NOT EXISTS operations (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_membre INT, " +
                    "date_op TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "montant DECIMAL(10,2), " +
                    "type_op ENUM('debit', 'credit'), " +
                    "mode_paiement VARCHAR(50));";

            String insertOpData = "INSERT INTO operations (id_membre, montant, type_op, mode_paiement) " +
                    "SELECT 1, 150.00, 'credit', 'Carte Bancaire' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM operations WHERE id=1);";

            stmt.execute(createOpSQL);
            stmt.execute(insertOpData);

            System.out.println("[DB] Initialisation complète avec données de vol.");

        } catch (SQLException e) {
            System.err.println("[ERREUR DB] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
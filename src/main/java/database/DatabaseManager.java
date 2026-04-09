package database;

import util.ConfigLoader;
import java.sql.*;

public class DatabaseManager {
    private static Connection connection;

    /**
     * Récupère la connexion active ou en crée une nouvelle selon la config
     */
    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        String dbType = ConfigLoader.getProperty("db.type", "h2");

        if ("mysql".equalsIgnoreCase(dbType)) {
            // Configuration Production (MySQL/MariaDB)
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("[DB] Driver MariaDB introuvable.");
            }

            String host = ConfigLoader.getProperty("db.host", "127.0.0.1");
            String name = ConfigLoader.getProperty("db.name", "aeroclub_db");
            String user = ConfigLoader.getProperty("db.user", "root");
            String pass = ConfigLoader.getProperty("db.pass", "");

            String url = "jdbc:mariadb://" + host + ":3306/" + name;
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("[DB] Connecté au serveur distant (MySQL/MariaDB)");
        } else {
            // Configuration Développement (H2 Local)
            connection = DriverManager.getConnection("jdbc:h2:./aeroclub_db", "user", "password");
            System.out.println("[DB] Connecté à la base locale (H2)");
        }

        return connection;
    }

    /**
     * Initialise les tables et les données de test UNIQUEMENT en mode H2
     */
    public static void initDatabase() {
        String dbType = ConfigLoader.getProperty("db.type", "h2");

        // Sécurité : si on n'est pas en H2, on n'initialise rien
        if (!"h2".equalsIgnoreCase(dbType)) {
            System.out.println("[DB] Mode Production : Skipping initDatabase.");
            return;
        }

        System.out.println("[DB] Mode Dev : Initialisation des tables H2...");

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Table Membres
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

            // 2. Table Avions
            stmt.execute("CREATE TABLE IF NOT EXISTS avions (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "immatriculation VARCHAR(20) UNIQUE NOT NULL, " +
                    "modele VARCHAR(100) NOT NULL, " +
                    "statut ENUM('disponible', 'maintenance', 'indisponible') DEFAULT 'disponible', " +
                    "heures_vol DECIMAL(8,1) DEFAULT 0.0, " +
                    "id_categorie INT NOT NULL DEFAULT 1" +
                    ");");

            // 3. Table Reservations
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_avion INT, " +
                    "id_membre INT, " +
                    "date_reservation DATE, " +
                    "heure_debut TIME, " +
                    "heure_fin TIME, " +
                    "statut ENUM('confirmee','annulee') DEFAULT 'confirmee');");

            // 4. Table Vols
            stmt.execute("CREATE TABLE IF NOT EXISTS vols (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_avion INT, " +
                    "id_pilote INT, " +
                    "date_heure_depart TIMESTAMP, " +
                    "date_heure_arrivee TIMESTAMP, " +
                    "duree DECIMAL(5,2), " +
                    "FOREIGN KEY (id_avion) REFERENCES avions(id), " +
                    "FOREIGN KEY (id_pilote) REFERENCES membres(id));");

            // 5. Table Operations (Comptabilité)
            stmt.execute("CREATE TABLE IF NOT EXISTS operations (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_membre INT, " +
                    "date_op TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "montant DECIMAL(10,2), " +
                    "type_op ENUM('debit', 'credit'), " +
                    "mode_paiement VARCHAR(50));");

            // 6. Table Cotisations
            stmt.execute("CREATE TABLE IF NOT EXISTS cotisations (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_membre INT, " +
                    "date_paiement DATE, " +
                    "montant DECIMAL(10,2), " +
                    "annee INT, " +
                    "FOREIGN KEY (id_membre) REFERENCES membres(id));");

            // --- INSERTION DES DONNÉES DE TEST (H2 Uniquement) ---

            // Admin par défaut
            stmt.executeUpdate("INSERT INTO membres (nom, prenom, email, password, droits_utilisateurs, statut) " +
                    "SELECT 'Admin', 'Aeroclub', 'admin', '123', 'administrateur', 'actif' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM membres WHERE email = 'admin');");

            // Avions de test
            String[] avionData = {
                    "INSERT INTO avions (id, immatriculation, modele, id_categorie, statut, heures_vol) SELECT 1, 'F-ABC1', 'Cessna 152', 1, 'disponible', 1520.4 WHERE NOT EXISTS (SELECT 1 FROM avions WHERE id=1)",
                    "INSERT INTO avions (id, immatriculation, modele, id_categorie, statut, heures_vol) SELECT 2, 'F-ABC2', 'Piper PA-28', 1, 'maintenance', 2340.9 WHERE NOT EXISTS (SELECT 1 FROM avions WHERE id=2)"
            };
            for (String sql : avionData) stmt.execute(sql);

            System.out.println("[DB] Initialisation complète (Tables + Données de test).");

        } catch (SQLException e) {
            System.err.println("[ERREUR DB] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
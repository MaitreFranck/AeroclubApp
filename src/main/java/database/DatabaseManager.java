package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./aeroclub_db";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "login VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL" +
                ");";

        String insertAdminSQL = "INSERT INTO utilisateurs (login, password) " +
                "SELECT 'admin', '123' " +
                "WHERE NOT EXISTS (SELECT 1 FROM utilisateurs WHERE login = 'admin');";

        try (Connection conn = getConnection();
             var statement = conn.createStatement()) {
            statement.execute(createTableSQL);
            int rowsAffected = statement.executeUpdate(insertAdminSQL);
            if (rowsAffected > 0) {
                System.out.println("Base de données initialisée : Compte 'admin' créé.");
            } else {
                System.out.println("Base de données prête : Le compte 'admin' existe déjà.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur d'initialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
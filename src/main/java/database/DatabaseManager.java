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
        String sql = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "login VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL" +
                ");";

        try (Connection conn = getConnection();
             var statement = conn.createStatement()) {
            statement.execute(sql);
            System.out.println("Base de données initialisée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package pu.project.app.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerDeleter {
    private static final String DB_URL = "jdbc:h2:~/airsoft-field";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        deletePlayerData();
    }

    // Method to delete player data from the database
    public static void deletePlayerData() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM players");
            System.out.println("Player data deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

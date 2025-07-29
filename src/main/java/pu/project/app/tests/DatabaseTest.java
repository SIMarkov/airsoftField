package pu.project.app.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTest {

    // Establishes connection to the database
    private Connection connect() throws SQLException {
        String url = "jdbc:h2:~/airsoft-field";
        String user = "admin";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }

    // Fetches data from the database
    public void fetchDataFromDatabase() {
        String sqlQuery = "SELECT * FROM players";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String team = rs.getString("team");
                String replicaType = rs.getString("replica_Type");
                String replicaSpeed = rs.getString("replica_Speed");
                String level = rs.getString("level");
                Boolean confirmation = rs.getBoolean("confirmation");

                System.out.println("ID: " + id + ", Name: " + name + ", Team: " + team + ", Replica Type:" + replicaType
                        + ", Replica Speed: " +
                        replicaSpeed + ", Level: " + level + ", Confirmation: " + confirmation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Starts the agent by fetching data from the database
    public void startAgent() {
        fetchDataFromDatabase();
    }

    public static void main(String[] args) {
        DatabaseTest agent = new DatabaseTest();
        agent.startAgent();
    }
}

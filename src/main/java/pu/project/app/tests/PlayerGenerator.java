package pu.project.app.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerGenerator {
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:h2:~/airsoft-field";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";

    private static int playerId = 1;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            createPlayersTable(connection);
            generatePlayers(connection);
            System.out.println("Players generated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create Players table if it doesn't exist
    private static void createPlayersTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Players (id INT PRIMARY KEY, name VARCHAR(255), team VARCHAR(255), replica_Type VARCHAR(255), replica_Speed VARCHAR(255), level VARCHAR(255), confirmation BOOLEAN, role VARCHAR(255))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    // Method to generate players and insert into the database
    private static void generatePlayers(Connection connection) throws SQLException {
        int numberOfPlayersToGenerate = 50;
        List<String> existingTeams = new ArrayList<>();

        for (int i = 0; i < numberOfPlayersToGenerate; i++) {
            String name = generateName();
            String team = "";
            if (random.nextDouble() < 0.5) {
                if (existingTeams.isEmpty() || random.nextDouble() < 0.5) {
                    team = generateUniqueTeam(existingTeams);
                } else {
                    team = existingTeams.get(random.nextInt(existingTeams.size()));
                }
                if (!team.isEmpty() && countPlayersInTeam(connection, team) >= 5) {
                    team = "";
                }
            }
            String replicaType = generateReplicaType();
            String replicaSpeed = generateReplicaSpeed(replicaType);
            String level = generateLevel();
            boolean confirmation = generateConfirmation();
            String role = generateRole();

            int currentId = playerId;
            while (playerIdExists(connection, currentId)) {
                currentId++;
            }

            insertPlayer(connection, currentId, name, team, replicaType, replicaSpeed, level, confirmation, role);
        }
    }

    // Method to count players in a team
    private static int countPlayersInTeam(Connection connection, String team) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Players WHERE team = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, team);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

    // Method to check if player ID exists in the database
    private static boolean playerIdExists(Connection connection, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Players WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    // Method to generate a random player name
    private static String generateName() {
        String firstName = firstNames.get(random.nextInt(firstNames.size()));
        String lastName = lastNames.get(random.nextInt(lastNames.size()));
        return firstName + " " + lastName;
    }

    // Method to generate a unique team name
    private static String generateUniqueTeam(List<String> existingTeams) {
        String teamFirst = teamFirsts.get(random.nextInt(teamFirsts.size()));
        String teamLast = teamLasts.get(random.nextInt(teamLasts.size()));
        String teamName = teamFirst + " " + teamLast;

        while (existingTeams.contains(teamName)) {
            teamFirst = teamFirsts.get(random.nextInt(teamFirsts.size()));
            teamLast = teamLasts.get(random.nextInt(teamLasts.size()));
            teamName = teamFirst + " " + teamLast;
        }

        existingTeams.add(teamName);
        return teamName;
    }

    // Method to generate a random replica type
    private static String generateReplicaType() {
        List<String> replicaTypes = new ArrayList<>();
        replicaTypes.addAll(Collections.nCopies(15, "Pistol"));
        replicaTypes.addAll(Collections.nCopies(10, "Shotgun"));
        replicaTypes.addAll(Collections.nCopies(30, "SMG"));
        replicaTypes.addAll(Collections.nCopies(30, "Assault rifle"));
        replicaTypes.addAll(Collections.nCopies(5, "Sniper rifle"));
        replicaTypes.addAll(Collections.nCopies(5, "Machine gun"));
        replicaTypes.addAll(Collections.nCopies(5, "Grenade launcher"));
        return replicaTypes.get(random.nextInt(replicaTypes.size()));
    }

    // Method to generate a random replica speed based on type
    private static String generateReplicaSpeed(String replicaType) {
        List<String> speeds = new ArrayList<>();
        speeds.addAll(Collections.nCopies(10, "100 m/s"));
        speeds.addAll(Collections.nCopies(50, "110 m/s"));
        speeds.addAll(Collections.nCopies(10, "120 m/s"));
        speeds.addAll(Collections.nCopies(10, "130 m/s"));
        speeds.addAll(Collections.nCopies(5, "140 m/s"));
        speeds.addAll(Collections.nCopies(5, "150 m/s"));
        return speeds.get(random.nextInt(speeds.size()));
    }

    // Method to generate a random player level
    private static String generateLevel() {
        List<String> levels = new ArrayList<>();
        levels.addAll(Collections.nCopies(40, "Beginner"));
        levels.addAll(Collections.nCopies(40, "Advanced"));
        levels.addAll(Collections.nCopies(20, "Expert"));
        return levels.get(random.nextInt(levels.size()));
    }

    // Method to generate a random confirmation status
    private static boolean generateConfirmation() {
        return random.nextDouble() < 0.8;
    }

    // Method to generate a default player role
    private static String generateRole() {
        return "defaultRole";
    }

    // Method to insert a player into the database
    private static void insertPlayer(Connection connection, int id, String name, String team, String replicaType,
            String replicaSpeed, String level, boolean confirmation, String role) throws SQLException {
        String sql = "INSERT INTO Players(id, name, team, replica_Type, replica_Speed, level, confirmation, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, team);
            statement.setString(4, replicaType);
            statement.setString(5, replicaSpeed);
            statement.setString(6, level);
            statement.setBoolean(7, confirmation);
            statement.setString(8, role);
            statement.executeUpdate();
        }
    }
public static void generatePlayers(int numberOfPlayersToGenerate) {
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        createPlayersTable(connection);
        for (int i = 0; i < numberOfPlayersToGenerate; i++) {
            String name = generateName();
            String team = "";
            if (random.nextDouble() < 0.5) {
                team = generateUniqueTeam(new ArrayList<>());
                if (countPlayersInTeam(connection, team) >= 5) {
                    team = "";
                }
            }
            String replicaType = generateReplicaType();
            String replicaSpeed = generateReplicaSpeed(replicaType);
            String level = generateLevel();
            boolean confirmation = random.nextDouble() < 0.8;
            String role = "defaultRole";
            int currentId = playerId;
            while (playerIdExists(connection, currentId)) {
                currentId++;
            }
            insertPlayer(connection, currentId, name, team, replicaType, replicaSpeed, level, confirmation, role);
        }
        System.out.println("Generated " + numberOfPlayersToGenerate + " players.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private static Random random = new Random();
    private static List<String> firstNames = Arrays.asList(
            "Mason", "Harper", "Jackson", "Aria", "Carter",
            "Savannah", "Logan", "Willow", "Elijah", "Aurora",
            "Wyatt", "Paisley", "Liam", "Brooklyn", "Riley",
            "Zoey", "Aiden", "Skylar", "Noah", "Hailey",
            "Grayson", "Piper", "Lucas", "Kennedy", "Ethan",
            "Madison", "Oliver", "Addison", "Caleb", "Taylor",
            "Benjamin", "Peyton", "Sebastian", "Harper", "Henry",
            "Morgan", "Alexander", "Quinn", "James", "Emerson",
            "William", "Reagan", "Daniel", "Jordan", "Gabriel",
            "Avery", "Owen", "Mackenzie", "Samuel", "Payton");

    private static List<String> lastNames = Arrays.asList(
            "Smith", "Johnson", "Williams", "Jones", "Brown",
            "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris",
            "Martin", "Thompson", "Garcia", "Martinez", "Robinson",
            "Clark", "Rodriguez", "Lewis", "Lee", "Walker",
            "Hall", "Allen", "Young", "Hernandez", "King",
            "Wright", "Lopez", "Hill", "Scott", "Green",
            "Adams", "Baker", "Gonzalez", "Nelson", "Carter",
            "Mitchell", "Perez", "Roberts", "Turner", "Phillips",
            "Campbell", "Parker", "Evans", "Edwards", "Collins");

    private static List<String> teamFirsts = Arrays.asList(
            "Shadow", "Phantom", "Steel", "Delta", "Ghost",
            "Blaze", "Eagle", "Thunder", "Viper", "Black",
            "Recon", "Desert", "Urban", "Firestorm", "Savage",
            "Iron", "Tactical", "Venom", "Liberty", "Nightfall",
            "Alpha", "Omega", "Storm", "Havoc", "Crimson",
            "Shadow", "Phoenix", "Warzone", "Avalanche", "Valor");

    private static List<String> teamLasts = Arrays.asList(
            "Strikers", "Fury", "Titans", "Raiders", "Reapers",
            "Battalion", "Elite", "Wolves", "Vanguard", "Ops",
            "Raptorz", "Troopers", "Outlaws", "Squad", "Serpents",
            "Guardians", "Titans", "Vipers", "Lions", "Ninjas",
            "Assassins", "Operatives", "Surge", "Hunters", "Commandos",
            "Stalkers", "Phantoms", "Warriors", "Avengers", "Vanguard");
}

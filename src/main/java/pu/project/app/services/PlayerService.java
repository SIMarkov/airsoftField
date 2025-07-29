package pu.project.app.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pu.project.app.models.Player;
import pu.project.app.repo.PlayerRepo;

@Service
public class PlayerService extends BaseService<Player> {

    @Autowired
    private PlayerRepo playerRepo;

    @Override
    protected JpaRepository<Player, Long> getRepo() {
        return playerRepo;
    }

    // Method to retrieve player data from the databas
    public List<Player> getPlayerDataFromDatabase() {
        List<Player> players = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:h2:~/airsoft-field", "admin", "password");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM players");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String team = resultSet.getString("team");
                String replicaType = resultSet.getString("replica_type");
                String replicaSpeed = resultSet.getString("replica_speed");
                String level = resultSet.getString("level");
                Boolean confirmation = resultSet.getBoolean("confirmation");
                players.add(new Player(id, name, team, replicaType, replicaSpeed, level, confirmation));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to retrieve data from the database.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return players;
    }

    // Method to list players with pagination
    public Page<Player> list(Pageable pageable) {
        return playerRepo.findAll(pageable);
    }

    // Method to delete a player by ID
    public void delete(Long id) {
        playerRepo.deleteById(id);
    }

    // Method to list players with pagination and filtering
    public Page<Player> list(Pageable pageable, Specification<Player> filter) {
        return playerRepo.findAll(pageable);
    }

    // Method to list players by name and team with pagination
    public Page<Player> list(String name, String team, Pageable pageable) {
        return playerRepo.findByNameAndTeam(name.toLowerCase(), team.toLowerCase(), pageable);
    }

    // Method to count players in a team
    public long countPlayersInTeam(String teamName) {
        return playerRepo.countByTeam(teamName);
    }

}
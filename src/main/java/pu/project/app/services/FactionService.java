package pu.project.app.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import pu.project.app.controllers.FactionController;
import pu.project.app.controllers.ScenarioController;
import pu.project.app.models.Player;

@Service
public class FactionService {

    private FactionController factionController;
    private final PlayerService playerService;
    private int scenario;

    public FactionService(PlayerService playerService) {
        this.playerService = playerService;
        this.factionController = new FactionController(new ArrayList<>());
    }

    // Load data from the database and assign players to factions
    public void assignFactionsFromDatabase() {
        List<Player> players = playerService.getPlayerDataFromDatabase();
        int confirmedPlayersCount = ScenarioController.calculateConfirmedPlayersCount(players);
        this.scenario = ScenarioController.getScenarioForPlayers(players);
        factionController = new FactionController(players);

        System.out.println("Confirmed players count: " + confirmedPlayersCount);
        System.out.println("Scenario: " + this.scenario);
        factionController.assignFactions(confirmedPlayersCount, this.scenario);
    }

    // Methods to retrieve factions
    public List<Player> getAlphaFaction() {
        return factionController.getAlphaFaction();
    }

    public List<Player> getBravoFaction() {
        return factionController.getBravoFaction();
    }

    public FactionController getFactionController() {
        return factionController;
    }

    // Method to retrieve the scenario
    public int getScenario() {
        return scenario;
    }
}

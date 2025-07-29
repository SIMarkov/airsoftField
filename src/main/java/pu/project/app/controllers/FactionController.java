package pu.project.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pu.project.app.models.Player;

public class FactionController {

    private List<Player> players;
    private List<Player> alphaFaction;
    private List<Player> bravoFaction;
    private FactionBalancer factionBalancer;

    // Constructor for FactionController
    public FactionController(List<Player> players) {
        this.players = players;
        this.alphaFaction = new ArrayList<>();
        this.bravoFaction = new ArrayList<>();
        this.factionBalancer = new FactionBalancer(players, alphaFaction, bravoFaction);
    }

    // Method to assign factions based on scenario and confirmed players count
    public void assignFactions(int scenario, int confirmedPlayersCount) {
        List<Player> filteredPlayers = ScenarioController.applyScenario(players, scenario);

        System.out.println("Filtered Players after scenario: " + filteredPlayers.size());

        assignTeamPlayers(filteredPlayers);

        List<Player> confirmedPlayers = filteredPlayers.stream()
                .filter(Player::getConfirmation)
                .collect(Collectors.toList());

        List<Player> playersWithoutTeam = confirmedPlayers.stream()
                .filter(player -> player.getTeam() == null || player.getTeam().isEmpty())
                .collect(Collectors.toList());
        assignPlayers(playersWithoutTeam);

        System.out.println("Alpha Faction size: " + alphaFaction.size());
        System.out.println("Bravo Faction size: " + bravoFaction.size());
    }

    // Assign players with existing teams to factions
    private void assignTeamPlayers(List<Player> filteredPlayers) {
        Map<String, List<Player>> teamsMap = factionBalancer.groupPlayersByTeam(filteredPlayers);

        for (Map.Entry<String, List<Player>> entry : teamsMap.entrySet()) {
            List<Player> teamPlayers = entry.getValue();

            if (teamPlayers.isEmpty()) {
                continue;
            }

            List<Player> confirmedPlayers = teamPlayers.stream()
                    .filter(Player::getConfirmation)
                    .collect(Collectors.toList());

            if (confirmedPlayers.isEmpty()) {
                continue;
            }

            List<Player> factionToAdd = factionBalancer.getFactionToAdd(confirmedPlayers.get(0));
            factionToAdd.addAll(confirmedPlayers);
        }
    }

    // Assign players without a team to factions
    private void assignPlayers(List<Player> confirmedPlayers) {
        List<Player> playersWithoutTeam = confirmedPlayers.stream()
                .filter(player -> player.getTeam() == null || player.getTeam().isEmpty())
                .collect(Collectors.toList());

        int alphaCount = alphaFaction.size();
        int bravoCount = bravoFaction.size();

        for (Player player : playersWithoutTeam) {
            List<Player> factionToAdd = (alphaCount <= bravoCount) ? alphaFaction : bravoFaction;
            factionToAdd.add(player);

            if (factionToAdd == alphaFaction) {
                alphaCount++;
            } else {
                bravoCount++;
            }
        }

        RoleAssigner.assignRoles(alphaFaction);
        RoleAssigner.assignRoles(bravoFaction);
    }

    // Get Alpha Faction players
    public List<Player> getAlphaFaction() {
        return new ArrayList<>(alphaFaction);
    }

    // Get Bravo Faction players
    public List<Player> getBravoFaction() {
        return new ArrayList<>(bravoFaction);
    }

    // Method to print faction information
    public void printFactionInfo() {
        System.out.println("Alpha Faction:");
        for (Player player : alphaFaction) {
            System.out.println("Name: " + player.getName() + ", Team: " + player.getTeam() + ", Replica type: " +
                    player.getReplicaType() + ", Replica Speed: " + player.getReplicaSpeed() + ", Level: "
                    + player.getLevel() +
                    ", Role: " + player.getRole() + ", Confirmation: " + player.getConfirmation());
        }

        System.out.println("\nBravo Faction:");
        for (Player player : bravoFaction) {
            System.out.println("Name: " + player.getName() + ", Team: " + player.getTeam() + ", Replica type: " +
                    player.getReplicaType() + ", Replica Speed: " + player.getReplicaSpeed() + ", Level: "
                    + player.getLevel() +
                    ", Role: " + player.getRole() + ", Confirmation: " + player.getConfirmation());
        }
    }

}
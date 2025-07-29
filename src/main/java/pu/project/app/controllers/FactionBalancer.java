package pu.project.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pu.project.app.models.Player;

public class FactionBalancer {

    private List<Player> players;
    private List<Player> alphaFaction;
    private List<Player> bravoFaction;

    // Constructor initializing the player lists for both factions
    public FactionBalancer(List<Player> players, List<Player> alphaFaction, List<Player> bravoFaction) {
        this.players = players;
        this.alphaFaction = alphaFaction;
        this.bravoFaction = bravoFaction;
    }

    // Group players by team
    public Map<String, List<Player>> groupPlayersByTeam(List<Player> filteredPlayers) {
        Map<String, List<Player>> teamsMap = new HashMap<>();
        for (Player player : filteredPlayers) {
            if (player.getTeam() != null && !player.getTeam().isEmpty()) {
                String team = player.getTeam();
                teamsMap.putIfAbsent(team, new ArrayList<>());
                teamsMap.get(team).add(player);
            }
        }
        return teamsMap;
    }

    // Balance players by total count
    public void balancePlayersByTotalCount() {
        balanceTeamPlayers();
        balancePlayers();
    }

    // Balance team players
    public void balanceTeamPlayers() {
        List<Player> filteredPlayers = players.stream()
                .filter(Player::getConfirmation)
                .collect(Collectors.toList());
        for (Map.Entry<String, List<Player>> entry : groupPlayersByTeam(filteredPlayers).entrySet()) {
            List<Player> teamPlayers = entry.getValue();
            if (!teamPlayers.isEmpty() && teamPlayers.stream().anyMatch(Player::getConfirmation)) {
                List<Player> factionToAdd = getFactionToAdd(teamPlayers.get(0));
                factionToAdd.addAll(teamPlayers);
            }
        }
    }

    // Balance players without team
    public void balancePlayers() {
        int alphaCount = alphaFaction.size();
        int bravoCount = bravoFaction.size();

        List<Player> playersWithoutTeam = players.stream()
                .filter(player -> player.getTeam() == null || player.getTeam().isEmpty())
                .collect(Collectors.toList());

        for (Player player : playersWithoutTeam) {
            List<Player> factionToAdd = (alphaCount <= bravoCount) ? alphaFaction : bravoFaction;
            factionToAdd.add(player);

            if (factionToAdd == alphaFaction) {
                alphaCount++;
            } else {
                bravoCount++;
            }
        }
    }

    // Get faction to add player to
    public List<Player> getFactionToAdd(Player player) {
        if (alphaFaction.size() == bravoFaction.size()) {
            return Math.random() < 0.5 ? alphaFaction : bravoFaction;
        } else {
            return alphaFaction.size() < bravoFaction.size() ? alphaFaction : bravoFaction;
        }
    }
}
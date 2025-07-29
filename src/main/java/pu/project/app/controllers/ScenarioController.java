package pu.project.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import pu.project.app.models.Player;

public class ScenarioController {

        // Method to calculate the count of confirmed players
        public static int calculateConfirmedPlayersCount(List<Player> players) {
                return (int) players.stream().filter(Player::getConfirmation).count();
        }

        // Method to calculate the count of players matching Scenario 1
        public static int calculatePlayersMatchingScenario1(List<Player> players) {
                return (int) players.stream().filter(player -> player.getConfirmation() &&
                                (player.getReplicaType().equals("Pistol") ||
                                                player.getReplicaType().equals("Shotgun") ||
                                                player.getReplicaType().equals("SMG") ||
                                                player.getReplicaType().equals("Assault rifle"))
                                &&
                                (player.getReplicaSpeed().equals("100 m/s") ||
                                                player.getReplicaSpeed().equals("110 m/s")))
                                .count();
        }

        // Method to calculate the count of players matching Scenario 2
        public static int calculatePlayersMatchingScenario2(List<Player> players) {
                return (int) players.stream().filter(player -> player.getConfirmation() &&
                                (player.getReplicaType().equals("Pistol") ||
                                                player.getReplicaType().equals("Shotgun") ||
                                                player.getReplicaType().equals("SMG") ||
                                                player.getReplicaType().equals("Assault rifle") ||
                                                player.getReplicaType().equals("Sniper rifle"))
                                &&
                                (player.getReplicaSpeed().equals("100 m/s") ||
                                                player.getReplicaSpeed().equals("110 m/s") ||
                                                player.getReplicaSpeed().equals("120 m/s") ||
                                                player.getReplicaSpeed().equals("130 m/s")))
                                .count();
        }

        // Method to calculate the count of players matching Scenario 3
        public static int calculatePlayersMatchingScenario3(List<Player> players) {
                return (int) players.stream().filter(player -> player.getConfirmation() &&
                                (player.getLevel().equals("Advanced") ||
                                                player.getLevel().equals("Expert"))
                                &&
                                (player.getReplicaType().equals("Pistol") ||
                                                player.getReplicaType().equals("Shotgun") ||
                                                player.getReplicaType().equals("SMG") ||
                                                player.getReplicaType().equals("Assault rifle") ||
                                                player.getReplicaType().equals("Sniper rifle") ||
                                                player.getReplicaType().equals("Machine gun") ||
                                                player.getReplicaType().equals("Grenade launcher"))
                                &&
                                (player.getReplicaSpeed().equals("100 m/s") ||
                                                player.getReplicaSpeed().equals("110 m/s") ||
                                                player.getReplicaSpeed().equals("120 m/s") ||
                                                player.getReplicaSpeed().equals("130 m/s") ||
                                                player.getReplicaSpeed().equals("140 m/s") ||
                                                player.getReplicaSpeed().equals("150 m/s")))
                                .count();
        }

        // Method to determine the scenario based on players' data
        public static int getScenarioForPlayers(List<Player> players) {
                int confirmedPlayersCount = calculateConfirmedPlayersCount(players);
                int scenario1Count = calculatePlayersMatchingScenario1(players);
                int scenario2Count = calculatePlayersMatchingScenario2(players);
                int scenario3Count = calculatePlayersMatchingScenario3(players);

                if (confirmedPlayersCount >= 0) {
                        if (confirmedPlayersCount >= 100 && scenario3Count >= 100) {
                                return 3;
                        } else if (confirmedPlayersCount >= 50 && scenario2Count >= 50) {
                                return 2;
                        } else if (confirmedPlayersCount >= 0 && scenario1Count >= 0) {
                                return 1;
                        }
                }
                return -1;
        }

        // Method to apply Scenario to players
        public static List<Player> applyScenario(List<Player> players, int scenarioFromArgument) {
                int scenario = getScenarioForPlayers(players);

                switch (scenario) {
                        case 1:
                                return applyScenario1(players);
                        case 2:
                                return applyScenario2(players);
                        case 3:
                                return applyScenario3(players);
                        default:
                                return new ArrayList<>();
                }
        }

        // Method to apply Scenario 1 to players
        public static List<Player> applyScenario1(List<Player> players) {
                return players.stream()
                                .filter(player -> player.getConfirmation() &&
                                                (player.getReplicaType().equals("Pistol") ||
                                                                player.getReplicaType().equals("Shotgun") ||
                                                                player.getReplicaType().equals("SMG") ||
                                                                player.getReplicaType().equals("Assault rifle"))
                                                &&
                                                (player.getReplicaSpeed().equals("100 m/s") ||
                                                                player.getReplicaSpeed().equals("110 m/s")))
                                .collect(Collectors.toList());
        }

        // Method to apply Scenario 2 to players
        public static List<Player> applyScenario2(List<Player> players) {
                return players.stream()
                                .filter(player -> player.getConfirmation() &&
                                                (player.getReplicaType().equals("Pistol") ||
                                                                player.getReplicaType().equals("Shotgun") ||
                                                                player.getReplicaType().equals("SMG") ||
                                                                player.getReplicaType().equals("Assault rifle") ||
                                                                player.getReplicaType().equals("Sniper rifle"))
                                                &&
                                                (player.getReplicaSpeed().equals("100 m/s") ||
                                                                player.getReplicaSpeed().equals("110 m/s") ||
                                                                player.getReplicaSpeed().equals("120 m/s") ||
                                                                player.getReplicaSpeed().equals("130 m/s")))
                                .collect(Collectors.toList());
        }

        // Method to apply Scenario 3 to players
        public static List<Player> applyScenario3(List<Player> players) {
                return players.stream()
                                .filter(player -> player.getConfirmation() &&
                                                (player.getLevel().equals("Advanced") ||
                                                                player.getLevel().equals("Expert"))
                                                &&
                                                (player.getReplicaType().equals("Pistol") ||
                                                                player.getReplicaType().equals("Shotgun") ||
                                                                player.getReplicaType().equals("SMG") ||
                                                                player.getReplicaType().equals("Assault rifle") ||
                                                                player.getReplicaType().equals("Sniper rifle") ||
                                                                player.getReplicaType().equals("Machine gun") ||
                                                                player.getReplicaType().equals("Grenade launcher"))
                                                &&
                                                (player.getReplicaSpeed().equals("100 m/s") ||
                                                                player.getReplicaSpeed().equals("110 m/s") ||
                                                                player.getReplicaSpeed().equals("120 m/s") ||
                                                                player.getReplicaSpeed().equals("130 m/s") ||
                                                                player.getReplicaSpeed().equals("140 m/s") ||
                                                                player.getReplicaSpeed().equals("150 m/s")))
                                .collect(Collectors.toList());
        }
}

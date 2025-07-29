package pu.project.app.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import pu.project.app.models.Player;

public class RoleAssigner {

    // Assign roles to players based on certain criteria
    public static void assignRoles(List<Player> faction) {
        int totalPlayers = faction.size();

        for (Player player : faction) {
            if (isSniper(player)) {
                player.setRole("Sniper");
            } else if (isSoldier(player)) {
                player.setRole("Soldier");
            }
        }

        int medicCount = totalPlayers / 20;
        int medicAssigned = 0;
        Random random = new Random();
        Set<Player> selectedMedics = new HashSet<>();
        while (medicAssigned < medicCount) {
            int index = random.nextInt(totalPlayers);
            Player player = faction.get(index);
            if (isSoldier(player) && !selectedMedics.contains(player)) {
                player.setRole("Medic");
                selectedMedics.add(player);
                medicAssigned++;
            }
        }

        int commanderCount = 1;
        int radioCount = 2;
        List<Player> eligiblePlayers = new ArrayList<>();
        for (Player player : faction) {
            if (player.getRole().equals("Soldier") || player.getRole().equals("Sniper")) {
                eligiblePlayers.add(player);
            }
        }
        Collections.sort(eligiblePlayers, Comparator.comparing((Player p) -> p.getLevel())
                .thenComparing((Player p) -> {
                    List<String> replicaPriority = List.of("Assault rifle", "SMG", "Shotgun", "Pistol", "Machine gun",
                            "Grenade launcher");
                    return replicaPriority.indexOf(p.getReplicaType());
                }));
        for (Player player : eligiblePlayers) {
            if (commanderCount > 0 && isCommander(player)) {
                player.setRole("Commander");
                commanderCount--;
            } else if (radioCount > 0 && isRadioOperator(player)) {
                player.setRole("Radio operator");
                radioCount--;
            }
        }
    }

    // Check if the player qualifies as a commander
    private static boolean isCommander(Player player) {
        return (player.getLevel().equals("Expert") || player.getLevel().equals("Advanced")
                || player.getLevel().equals("Beginner")) &&
                (player.getReplicaType().equals("Assault rifle") || player.getReplicaType().equals("SMG") ||
                        player.getReplicaType().equals("Shotgun") || player.getReplicaType().equals("Pistol") ||
                        player.getReplicaType().equals("Machine gun")
                        || player.getReplicaType().equals("Grenade launcher"));
    }

    // Check if the player qualifies as a radio operator
    private static boolean isRadioOperator(Player player) {
        return (player.getLevel().equals("Expert") || player.getLevel().equals("Advanced")
                || player.getLevel().equals("Beginner")) &&
                (player.getReplicaType().equals("Assault rifle") || player.getReplicaType().equals("SMG") ||
                        player.getReplicaType().equals("Shotgun") || player.getReplicaType().equals("Pistol") ||
                        player.getReplicaType().equals("Machine gun")
                        || player.getReplicaType().equals("Grenade launcher"));
    }

    // Check if the player has a sniper rifle
    private static boolean isSniper(Player player) {
        return player.getReplicaType().equals("Sniper rifle");
    }

    // Check if the player qualifies as a soldier
    private static boolean isSoldier(Player player) {
        List<String> replicaTypes = List.of("Assault rifle", "SMG", "Shotgun", "Pistol", "Machine gun",
                "Grenade launcher");
        return replicaTypes.contains(player.getReplicaType());
    }
}

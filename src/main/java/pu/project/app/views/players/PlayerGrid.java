package pu.project.app.views.players;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.function.ValueProvider;
import pu.project.app.models.Player;

public class PlayerGrid extends Grid<Player> {
    public PlayerGrid() {
        super(Player.class, false);
        initGrid();
    }

    // Initializes the player grid with columns and configurations
    private void initGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        addColumn(Player::getName).setHeader("Name").setSortable(true);
        addColumn(Player::getTeam).setFrozen(true).setHeader("Team").setSortable(true);
        addColumn(Player::getReplicaType).setHeader("Replica type").setSortable(true);
        addColumn(Player::getReplicaSpeed).setHeader("Replica speed").setSortable(true);
        addColumn(Player::getLevel).setFrozen(true).setHeader("Level").setSortable(true);
        addColumn((ValueProvider<Player, String>) player -> player.getConfirmation() ? "Yes" : "No")
                .setHeader("Confirmation")
                .setSortable(true);
    }
}
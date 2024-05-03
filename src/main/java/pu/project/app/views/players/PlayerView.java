package pu.project.app.views.players;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import pu.project.app.models.Player;
import pu.project.app.services.PlayerService;
import pu.project.app.views.MainLayout;

@PageTitle("Players")
@Route(value = "players", layout = MainLayout.class)
@AnonymousAllowed
public class PlayerView extends VerticalLayout {
    private final PlayerService playerService;
    private PlayerGrid playerGrid;
    private Button editButton;
    private Button deleteButton;

    // Constructor for PlayerView
    public PlayerView(PlayerService playerService) {
        this.playerService = playerService;
        initContent();
    }

    // Initialize the content of the view
    private void initContent() {
        setSizeFull();
        add(createButtons(), createGrid());
    }

    // Create the grid component
    private Component createGrid() {
        playerGrid = new PlayerGrid();
        playerGrid.setSizeFull();
        reloadGrid();
        SingleSelect<Grid<Player>, Player> singleSelect = playerGrid.asSingleSelect();
        singleSelect.addValueChangeListener(event -> {
            Player selectedPlayer = event.getValue();
            editButton.setEnabled(selectedPlayer != null);
            deleteButton.setEnabled(selectedPlayer != null);
        });
        return playerGrid;
    }

    // Create buttons for add, edit, and delete actions
    private HorizontalLayout createButtons() {
        final Button addButton = new Button(new Icon(VaadinIcon.PENCIL));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.setTooltipText("Add player");
        addButton.addClickListener(l -> openPlayerDialog(new Player()));
        
        editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(l -> openPlayerDialog(playerGrid.asSingleSelect().getValue()));
        editButton.setEnabled(false);
        editButton.setTooltipText("Edit player");
        
        deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.setEnabled(false);
        deleteButton.setTooltipText("Delete player");
        deleteButton.addClickListener(event -> {
            Dialog dialog = new Dialog();
            Player selectedPlayer = playerGrid.asSingleSelect().getValue();
            if (selectedPlayer != null) {
                dialog.setHeaderTitle("Confirm deleting player: " + selectedPlayer.getName());
                Button yes = new Button("Yes", ll -> {
                    playerService.remove(selectedPlayer.getId());
                    reloadGrid();
                    dialog.close();
                });
                Button no = new Button("No", ll -> dialog.close());
                dialog.getFooter().add(no, yes);
                dialog.open();
            }
        });
        return new HorizontalLayout(addButton, editButton, deleteButton);
    }

    // Open player dialog for adding or editing player
    private void openPlayerDialog(final Player player) {
        PlayerDialog playerDialog = new PlayerDialog(player, playerService);
        playerDialog.addSaveClickListener(event -> reloadGrid());
        playerDialog.open();
    }

    // Reload grid with updated data
    private void reloadGrid() {
        playerGrid.setItems(playerService.findAll());
    }
}
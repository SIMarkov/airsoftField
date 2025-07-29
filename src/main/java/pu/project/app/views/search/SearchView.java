package pu.project.app.views.search;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import pu.project.app.models.Player;
import pu.project.app.repo.PlayerRepo;
import pu.project.app.services.PlayerService;
import pu.project.app.views.MainLayout;

@PageTitle("Search")
@Route(value = "search", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)

public class SearchView extends VerticalLayout {

    private final PlayerRepo playerRepo;

    private final Grid<Player> grid = new Grid<>(Player.class);
    private final TextField nameFilter = new TextField();
    private final TextField teamFilter = new TextField();

    // Constructor for SearchView
    public SearchView(PlayerService playerService, PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;

        configureGrid();
        configureFilters();

        HorizontalLayout filtersLayout = new HorizontalLayout(nameFilter, teamFilter);

        VerticalLayout contentLayout = new VerticalLayout(filtersLayout, grid);
        contentLayout.setSizeFull();
        contentLayout.setFlexGrow(1, grid);

        add(contentLayout);
        setSizeFull();
    }

    // Configures the grid to display player information
    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(Player::getName).setHeader("Name");
        grid.addColumn(Player::getTeam).setHeader("Team");
        grid.addColumn(Player::getReplicaType).setHeader("Replica type");
        grid.addColumn(Player::getReplicaSpeed).setHeader("Replica speed");
        grid.addColumn(Player::getLevel).setHeader("Level");
        grid.addColumn((ValueProvider<Player, String>) player -> player.getConfirmation() ? "Yes" : "No")
                .setHeader("Confirmation")
                .setSortable(true);
        updateGrid("", "");
    }

    // Configures the filters for name and team
    private void configureFilters() {
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.setPlaceholder("Enter a name");
        nameFilter.addValueChangeListener(e -> updateGrid(nameFilter.getValue(), teamFilter.getValue()));

        teamFilter.setValueChangeMode(ValueChangeMode.EAGER);
        teamFilter.setPlaceholder("Enter a team");
        teamFilter.addValueChangeListener(e -> updateGrid(nameFilter.getValue(), teamFilter.getValue()));
    }

    // Updates the grid with filtered data based on name and team
    private void updateGrid(String name, String team) {
        name = name.toLowerCase();
        team = team.toLowerCase();

        List<Player> players;

        if (name.isEmpty() && team.isEmpty()) {
            players = playerRepo.findAll(PageRequest.of(0, 1000)).getContent();
        } else {
            players = playerRepo.findByNameAndTeam(name, team, PageRequest.of(0, 1000)).getContent();
        }

        grid.setItems(players);
    }
}
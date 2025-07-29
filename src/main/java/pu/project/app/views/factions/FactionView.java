package pu.project.app.views.factions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import pu.project.app.models.Player;
import pu.project.app.services.FactionService;
import pu.project.app.views.MainLayout;

@PageTitle("Factions")
@Route(value = "factions", layout = MainLayout.class)
@AnonymousAllowed
@Component
public class FactionView extends VerticalLayout {

    private final Grid<Player> alphaGrid;
    private final Grid<Player> bravoGrid;
    private final FactionService factionService;
    private final Button downloadFactionsButton;
    private boolean hasDataInAlphaGrid = false;
    private boolean hasDataInBravoGrid = false;
    private List<GridDataChangeListener> gridDataChangeListeners = new ArrayList<>();

    // Constructor for FactionView
    public FactionView(FactionService factionService, FactionPDF factionPDF) {
        this.factionService = factionService;

        alphaGrid = createGrid();
        bravoGrid = createGrid();

        downloadFactionsButton = new Button(new Icon(VaadinIcon.BOOK));
        downloadFactionsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        downloadFactionsButton.setTooltipText("Download PDF");
        downloadFactionsButton.addClickListener(event -> {
            try {
                List<Player> alphaPlayers = alphaGrid.getDataProvider().fetch(new Query<>())
                        .collect(Collectors.toList());
                List<Player> bravoPlayers = bravoGrid.getDataProvider().fetch(new Query<>())
                        .collect(Collectors.toList());

                byte[] pdfContent = factionPDF.generatePDFContent(alphaPlayers, bravoPlayers);

                StreamResource resource = new StreamResource("factions.pdf",
                        () -> new ByteArrayInputStream(pdfContent));
                resource.setContentType("application/pdf");

                resource.setCacheTime(0);

                Anchor anchor = new Anchor(resource, "");
                anchor.getElement().setAttribute("download", true);
                anchor.getElement().setAttribute("target", "_blank");
                anchor.getElement().getStyle().set("display", "none");

                add(anchor);
                anchor.getElement().executeJs("this.click();");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button assignFactionsButton = new Button(new Icon(VaadinIcon.TOOLS));
        assignFactionsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        assignFactionsButton.setTooltipText("Assign factions");
        assignFactionsButton.addClickListener(event -> {
            factionService.assignFactionsFromDatabase();
            factionService.getFactionController().printFactionInfo();
            updateGrids();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(assignFactionsButton, downloadFactionsButton);
        buttonLayout.setSizeFull();

        HorizontalLayout gridsLayout = new HorizontalLayout(alphaGrid, bravoGrid);
        gridsLayout.setSizeFull();

        addGridHeaders();

        VerticalLayout factionLayout = new VerticalLayout(buttonLayout, gridsLayout);
        factionLayout.setSizeFull();
        factionLayout.setAlignItems(Alignment.START);
        add(factionLayout);

        updateGrids();
    }

    // Method to update the state of buttons based on grid data
    private void updateButtonState() {
        hasDataInAlphaGrid = !alphaGrid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList()).isEmpty();
        hasDataInBravoGrid = !bravoGrid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList()).isEmpty();
        downloadFactionsButton.setEnabled(hasDataInAlphaGrid && hasDataInBravoGrid);
        fireDataChange();
    }

    // Method to create a grid with predefined columns
    private Grid<Player> createGrid() {
        Grid<Player> grid = new Grid<>();
        grid.addColumn(Player::getName).setHeader("Name");
        grid.addColumn(Player::getTeam).setHeader("Team");
        grid.addColumn(Player::getRole).setHeader("Role");
        grid.addColumn(Player::getReplicaType).setHeader("Replica type");
        grid.addColumn(Player::getReplicaSpeed).setHeader("Replica speed");
        grid.addColumn(Player::getLevel).setHeader("Level");
        return grid;
    }

    // Method to add headers to the grids
    private void addGridHeaders() {
        HeaderRow alphaHeaderRow = alphaGrid.prependHeaderRow();
        HeaderCell alphaHeaderCell = alphaHeaderRow.join(alphaGrid.getColumns().toArray(new Grid.Column<?>[0]));
        alphaHeaderCell.setComponent(
                new Html("<h3 style='text-align: center; color: #ffffff; background-color: #9e9a75;'>Alpha</h3>"));

        HeaderRow bravoHeaderRow = bravoGrid.prependHeaderRow();
        HeaderCell bravoHeaderCell = bravoHeaderRow.join(bravoGrid.getColumns().toArray(new Grid.Column<?>[0]));
        bravoHeaderCell.setComponent(
                new Html("<h3 style='text-align: center; color: #ffffff; background-color: #41533b;'>Bravo</h3>"));
    }

    // Method to update grids with faction data
    private void updateGrids() {
        alphaGrid.setItems(factionService.getAlphaFaction());
        bravoGrid.setItems(factionService.getBravoFaction());
        updateButtonState();
    }

    // Method to check if grids have data
    public boolean hasDataInGrids() {
        return hasDataInAlphaGrid || hasDataInBravoGrid;
    }

    // Interface for grid data change listeners
    public interface GridDataChangeListener {
        void dataChanged();
    }

    // Method to add grid data change listeners
    public void addGridDataChangeListener(GridDataChangeListener listener) {
        gridDataChangeListeners.add(listener);
    }

    // Method to notify grid data change listeners
    private void fireDataChange() {
        for (GridDataChangeListener listener : gridDataChangeListeners) {
            listener.dataChanged();
        }
    }
}

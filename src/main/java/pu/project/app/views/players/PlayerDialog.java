package pu.project.app.views.players;

import java.util.Objects;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import pu.project.app.models.Player;
import pu.project.app.services.PlayerService;

public class PlayerDialog extends Dialog {

    private Player player;
    private final PlayerService playerService;
    private TextField name = new TextField("Name");
    private TextField team = new TextField("Team");
    private ComboBox<String> replicaTypeComboBox = new ComboBox<>("Replica type");
    private ComboBox<String> replicaSpeedComboBox = new ComboBox<>("Replica speed");
    private ComboBox<String> levelComboBox = new ComboBox<>("Level");
    private Checkbox confirmation = new Checkbox("Confirmation");
    private BeanValidationBinder<Player> binder = new BeanValidationBinder<>(Player.class);
    private boolean isEditMode;
    private Button saveButton;

    // Constructor
    public PlayerDialog(Player player, PlayerService playerService) {
        this.player = player;
        this.playerService = playerService;
        isEditMode = player.getId() != null;
        initContent();
        binder.setBean(player);
    }

    // Setter for player
    public void setPlayer(Player player) {
        this.player = player;
        binder.setBean(player);
    }

    // Initialize dialog content
    private void initContent() {
        setHeaderTitle(isEditMode ? "Change Player" : "Create Player");
        createMainContent();
        configureFooter();
    }

    private String originalTeamValue;

    // Configure dialog footer
    private void configureFooter() {
        originalTeamValue = player.getTeam();

        saveButton = new Button("Save", l -> {
            String teamName = team.getValue();
            if (!Objects.equals(teamName, originalTeamValue) && !teamName.isBlank()) {
                long teamPlayerCount = playerService.countPlayersInTeam(teamName);
                if (teamPlayerCount >= 5) {
                    team.setInvalid(true);
                    team.setErrorMessage("Team is full!");
                    return;
                }
            }

            BinderValidationStatus<Player> validated = binder.validate();
            if (validated.isOk()) {
                try {
                    binder.writeBean(player);

                    if (isEditMode) {
                        playerService.update(player);
                    } else {
                        playerService.create(player);
                    }
                    close();
                } catch (ValidationException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        final Button cancelButton = new Button("Cancel", l -> close());
        getFooter().add(cancelButton, saveButton);
    }

    // Create main content of dialog
    private void createMainContent() {
        binder.bindInstanceFields(this);
        binder.forField(name).asRequired("Required!").bind(Player::getName, Player::setName)
                .setAsRequiredEnabled(true);
        String[] replicaTypeOptions = { "Pistol", "Shotgun", "SMG", "Assault rifle", "Sniper rifle", "Machine gun",
                "Grenade launcher" };
        replicaTypeComboBox.setItems(replicaTypeOptions);
        binder.forField(replicaTypeComboBox).asRequired("Required!")
                .bind(Player::getReplicaType, Player::setReplicaType)
                .setAsRequiredEnabled(true);
        String[] replicaSpeedOptions = { "100 m/s", "110 m/s", "120 m/s", "130 m/s", "140 m/s", "150 m/s" };
        replicaSpeedComboBox.setItems(replicaSpeedOptions);
        binder.forField(replicaSpeedComboBox).asRequired("Required!")
                .bind(Player::getReplicaSpeed, Player::setReplicaSpeed)
                .setAsRequiredEnabled(true);
        String[] levelOptions = { "Beginner", "Advanced", "Expert" };
        levelComboBox.setItems(levelOptions);
        binder.forField(levelComboBox).asRequired("Required!").bind(Player::getLevel, Player::setLevel)
                .setAsRequiredEnabled(true);

        FormLayout formLayout = new FormLayout(name, team, replicaTypeComboBox, replicaSpeedComboBox, levelComboBox,
                confirmation);
        formLayout.setSizeFull();
        add(formLayout);
    }

    // Add save button click listener
    public void addSaveClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        saveButton.addClickListener(listener);
    }
}
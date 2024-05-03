package pu.project.app.dto;

import jakarta.persistence.*;

@Entity
public class PlayerDTO extends EntityDTO {

    @Lob
    @Column(length = 1000000)
    private String name;
    private String team;
    private Boolean confirmation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }
}

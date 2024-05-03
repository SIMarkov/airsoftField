package pu.project.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Players")
public class Player extends BaseModel {

    @Column(length = 256, name = "NAME", nullable = false)
    @NotNull
    private String name;
    @Column(nullable = false)
    private String team;
    @Column(nullable = false)
    private String replicaType;
    @Column(nullable = false)
    private String replicaSpeed;
    @Column(nullable = false)
    private String level;
    @Column(nullable = false)
    private Boolean confirmation;
    @Column(nullable = false)
    private String role = "No role";

    public Player(Long id, String name, String team, String replicaType, String replicaSpeed, String level,
            Boolean confirmation) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.replicaType = replicaType;
        this.replicaSpeed = replicaSpeed;
        this.level = level;
        this.confirmation = confirmation;
    }

    @Transient
    private String temp;
}
package com.timesnewronan.hoopiq_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Create a player class
@Entity
@Table(name = "players")
public class Player {
    // Marks the primary key field
    @Id
    private Long id;

    // Maps the Java field nbaPlayerId to the DB column nba_player_id
    @Column(name = "nba_player_id", nullable = false, unique = true)
    private Integer nbaPlayerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "position")
    private String position;

    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private String weight;

    @Column(name = "active")
    private Boolean active;

    public Player() {

    }

    // Generate getters and setters
    public Long getId() {
        return id;
    }

    public Integer getNbaPlayerId() {
        return nbaPlayerId;
    }

    public void setNbaPlayerId(Integer nbaPlayerId) {
        this.nbaPlayerId = nbaPlayerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

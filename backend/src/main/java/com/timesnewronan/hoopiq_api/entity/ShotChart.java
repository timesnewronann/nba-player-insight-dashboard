package com.timesnewronan.hoopiq_api.entity;

import jakarta.persistence.*;

// needed for game date
import java.time.LocalDate;

// Mark the class as a JPA entity (map to a database table)
@Entity

// Tell JPA which table maps to shortchart
@Table(name = "shot_chart")

public class ShotChart {

    // Mark the id field as the primary key
    @Id

    // the database generates the id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Maps the field playerId to the DB column player_id
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // Maps the field gameId to the DB column game_id
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "loc_x", nullable = false)
    private Integer locX;

    @Column(name = "loc_y", nullable = false)
    private Integer locY;

    @Column(name = "shot_made", nullable = false)
    private Boolean shotMade;

    @Column(name = "shot_type", nullable = false)
    private String shotType;

    @Column(name = "shot_zone", nullable = false)
    private String shotZone;

    @Column(name = "game_date", nullable = false)
    private LocalDate gameDate;

    public ShotChart() {

    }

    // getter to get the shot chart id
    public Long getId() {
        return id;
    }

    // getter to get the player
    public Player getPlayer() {
        return player;
    }

    // setter to set the player_id
    public void setPlayer(Player player) {
        this.player = player;
    }

    // getter to get the game id
    public Game getGame() {
        return game;
    }

    // setter to set the game id
    public void setGame(Game game) {
        this.game = game;
    }

    // getter to get loc_x
    public Integer getLocX() {
        return locX;
    }

    // setter to set loc_x
    public void setLocX(Integer locX) {
        this.locX = locX;
    }

    // getter to get loc_y
    public Integer getLocY() {
        return locY;
    }

    // setter to set loc_y
    public void setLocY(Integer locY) {
        this.locY = locY;
    }

    // getter to get shot_made
    public Boolean getShotMade() {
        return shotMade;
    }

    // setter to set shot_made
    public void setShotMade(Boolean shotMade) {
        this.shotMade = shotMade;
    }

    // getter to get shot type
    public String getShotType() {
        return shotType;
    }

    // setter to set shot type
    public void setShotType(String shotType) {
        this.shotType = shotType;
    }

    // getter to get shot zone
    public String getShotZone() {
        return shotZone;
    }

    // setter to set shot zone
    public void setShotZone(String shotZone) {
        this.shotZone = shotZone;
    }

    // getter to get game date
    public LocalDate getGameDate() {
        return gameDate;
    }

    // setter to set game Date
    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

}

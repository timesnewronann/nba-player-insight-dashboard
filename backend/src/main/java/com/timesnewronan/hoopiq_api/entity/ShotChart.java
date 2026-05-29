package com.timesnewronan.hoopiq_api.entity;

import jakarta.persistence.*;

import java.sql.Date;
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
    @Column(name = "player_id", nullable = false, unique = true)
    private Integer playerId;

    // Maps the field gameId to the DB column game_id
    @Column(name = "game_id", nullable = false, unique = true)
    private Integer gameId;

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

    // getter to get the player_id
    public Integer getPlayerId() {
        return playerId;
    }

    public 

}

package com.timesnewronan.hoopiq_api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

// Mark the class as a JPA entity (map to a database table)
@Entity

// Tell JPA that the table maps to player_game_logs
@Table(name = "player_game_logs")

public class PlayerGameStat {
    // Mark the field id as the primary key
    @Id

    // the databse generates the id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A Player can have multiple seasons stat rows
    @ManyToOne

    // player_id column connects to the players table
    // make use of the entire player object rather than just the id
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // game_id column connects to the games table
    @Column(name = "game_id", nullable = false)
    private BigDecimal game_id;

    // team_id column connects to the teams table
    @Column(name = "team_id", nullable = false)
    private BigDecimal team_id;

    // points scored
    @Column(name = "pts_scored", nullable = false)
    private Integer ptsScored;

    // assists
    @Column(name = "assists", nullable = false)
    private Integer assists;

    // rebounds
    @Column(name = "rebounds", nullable = false)
    private Integer rebounds;

    // blocks
    @Column(name = "blocks", nullable = false)
    private Integer blocks;

    // steals
    @Column(name = "steals", nullable = false)
    private Integer steals;

    // minutes
    @Column(name = "minutes")
    private BigDecimal minutes;

    // field_goal_pct
    @Column(name = "field_goal_pct")
    private BigDecimal fieldGoalPct;

    // three_point_pct
    @Column(name = "field_goal_pct")
    private BigDecimal threePointPct;

    // free_throw_pct
    @Column(name = "free_throw_pct")
    private BigDecimal freeThrowPct;

    // turnovers

    @Column(name = "turnovers", nullable = false)
    private Integer turnovers;

    // win_loss
    @Column(name = "win_loss")
    private Character winLoss;

    public PlayerGameStat() {

    }

    // getter to get Id
    public Long getId() {
        return id;
    }

    // getter to get the player
    public Player getPlayer() {
        return player;
    }

    // setter to set the player
    public void setPlayer(Player player) {
        this.player = player;
    }

    // getter to get the game
    public BigDecimal getGameId() {
        return game_id;
    }

    // setter to set the game
    public void setGameId(BigDecimal game_id) {
        this.game_id = game_id;
    }

    // getter to get the team_id
    public BigDecimal getTeamId() {
        return team_id;
    }

    // setter to set the team_id
    public void setTeamId(BigDecimal team_id) {
        this.team_id = team_id;
    }

    // getter to get the points scored
    public Integer getPtsScored() {
        return ptsScored;
    }

    // setter to set the points scored
}

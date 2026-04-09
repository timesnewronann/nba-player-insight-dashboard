package com.timesnewronan.hoopiq_api.entity;

// import specification for mapping Java Objects to relational database
import jakarta.persistence.*;

// Import big decimal
import java.math.BigDecimal;

// Mark the class as a JPA entity (map to a database table)
@Entity

// Tell JPA that the table maps to player_season_stats
@Table(name = "player_season_stats")

public class PlayerSeasonStat {
    // Mark the field as the primary key
    @Id

    // the database generates the id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A player can have mulitple season stat rows
    // player_season_stats -> players
    @ManyToOne

    // player_id column connects to the players table
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // Season Column
    @Column(nullable = false)
    private String season;

    // player season stats variables
    private Integer gamesPlayed;
    private BigDecimal minutesPerGame;
    private BigDecimal pointsPerGame;
    private BigDecimal reboundsPerGame;
    private BigDecimal assistsPerGame;
    private BigDecimal stealsPerGame;
    private BigDecimal blocksPerGame;
    private BigDecimal fieldGoalPct;
    private BigDecimal threePointPct;
    private BigDecimal freeThrowPct;

    public PlayerSeasonStat() {

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

    // getter to get the season
    public String getSeason() {
        return season;
    }

    // setter to set the season
    public void setSeason(String season) {
        this.season = season;
    }

    // getter to get the gamesPlayed
    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    // setter to set games played
    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    // getter to get minutes per game
    public BigDecimal getMinutesPerGame() {
        return minutesPerGame;
    }

    // setter to set minutes per game
    public void setMinutesPerGame(BigDecimal minutesPerGame) {
        this.minutesPerGame = minutesPerGame;
    }

    // getter to get the points per game
    public BigDecimal getPointsPerGame() {
        return pointsPerGame;
    }

    // setter to set the points per game
    public void setPointsPerGame(BigDecimal pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
    }

    // getter to get the rebounds per game

}

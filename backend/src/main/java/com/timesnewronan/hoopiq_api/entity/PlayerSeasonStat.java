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
    public BigDecimal getReboundsPerGame() {
        return reboundsPerGame;
    }

    // setter to set the rebounds per game
    public void setReboundsPerGame(BigDecimal reboundsPerGame) {
        this.reboundsPerGame = reboundsPerGame;
    }

    // getter to get the assists per game
    public BigDecimal getAssistsPerGame() {
        return assistsPerGame;
    }

    // setter to set the assists per game
    public void setAssistsPerGame(BigDecimal assistsPerGame) {
        this.assistsPerGame = assistsPerGame;
    }

    // getter to get the steals per game
    public BigDecimal getStealsPerGame() {
        return stealsPerGame;
    }

    // setter to set the steals per game
    public void setStealsPerGame(BigDecimal stealsPerGame) {
        this.stealsPerGame = stealsPerGame;
    }

    // getter to get the blocks per game
    public BigDecimal getBlocksPerGame() {
        return blocksPerGame;
    }

    // setter to set the blocks per game
    public void setBlocksPerGame(BigDecimal blocksPerGame) {
        this.blocksPerGame = blocksPerGame;
    }

    // getter to get the field goal percentage
    public BigDecimal getFieldGoalPct() {
        return fieldGoalPct;
    }

    // setter to set the field goal percentage
    public void setFieldGoalPct(BigDecimal fieldGoalPct) {
        this.fieldGoalPct = fieldGoalPct;
    }

    // getter to get the 3pt percentage
    public BigDecimal getThreePointPct() {
        return threePointPct;
    }

    // setter to set the 3pt percentage
    public void setThreePointPct(BigDecimal threePointPct) {
        this.threePointPct = threePointPct;
    }

    // getter to get the free throw percentage
    public BigDecimal getFreeThrowPct() {
        return freeThrowPct;
    }

    // setter to set the free throw percentage
    public void setFreeThrowPct(BigDecimal freeThrowPct) {
        this.freeThrowPct = freeThrowPct;
    }

}

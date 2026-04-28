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

    // A Player can have multiple game stat rows
    @ManyToOne

    // player_id column connects to the players table
    // make use of the entire player object rather than just the id
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // One game has many player game logs
    @ManyToOne
    // game_id column connects to the games table
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    // Many players on the same team, many game log rows pointing to the same team
    // should only be on one team
    @ManyToOne
    // team_id column connects to the teams table
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

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
    @Column(name = "three_point_pct")
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
    public Game getGame() {
        return game;
    }

    // setter to set the game
    public void setGame(Game game) {
        this.game = game;
    }

    // getter to get the team
    public Team getTeam() {
        return team;
    }

    // setter to set the team
    public void setTeam(Team team) {
        this.team = team;
    }

    // getter to get the points scored
    public Integer getPtsScored() {
        return ptsScored;
    }

    // setter to set the points scored
    public void setPtsScored(Integer ptsScored) {
        this.ptsScored = ptsScored;
    }

    // getter to get the assists
    public Integer getAssists() {
        return assists;
    }

    // setter to set the assists
    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    // getter to get the rebounds
    public Integer getRebounds() {
        return rebounds;
    }

    // setter to set the rebounds
    public void setRebounds(Integer rebounds) {
        this.rebounds = rebounds;
    }

    // getter to get blocks
    public Integer getBlocks() {
        return blocks;
    }

    // setter to set the blocks
    public void setBlocks(Integer blocks) {
        this.blocks = blocks;
    }

    // getter to get steals
    public Integer getSteals() {
        return steals;
    }

    // setter to set steals
    public void setSteals(Integer steals) {
        this.steals = steals;
    }

    // getter to get minutes
    public BigDecimal getMinutes() {
        return minutes;
    }

    // setter to set minutes
    public void setMinutes(BigDecimal minutes) {
        this.minutes = minutes;
    }

    // getter to get field_goal_pct
    public BigDecimal getFieldGoalPct() {
        return fieldGoalPct;
    }

    // setter to set field_goal_pct
    public void setFieldGoalPct(BigDecimal fieldGoalPct) {
        this.fieldGoalPct = fieldGoalPct;
    }

    // getter to get the three_point_pct
    public BigDecimal getThreePointPct() {
        return threePointPct;
    }

    // setter to get three_point_pct
    public void setThreePointPct(BigDecimal threePointPct) {
        this.threePointPct = threePointPct;
    }

    // getter to get free_throw_pct
    public BigDecimal getFreeThrowPct() {
        return freeThrowPct;
    }

    // setter to set free_throw_pct
    public void setFreeThrowPct(BigDecimal freeThrowPct) {
        this.freeThrowPct = freeThrowPct;
    }

    // getter to get turnovers
    public Integer getTurnovers() {
        return turnovers;
    }

    // setter to set turnovers
    public void setTurnovers(Integer turnovers) {
        this.turnovers = turnovers;
    }

    // getter to get win loss
    public Character getWinLoss() {
        return winLoss;
    }

    // setter to set win loss
    public void setWinLoss(Character winLoss) {
        this.winLoss = winLoss;
    }
}

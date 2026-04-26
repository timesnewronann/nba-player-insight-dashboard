package com.timesnewronan.hoopiq_api.entity;

import jakarta.persistence.*;

// needed for date field
import java.time.LocalDate;

// Mark the class as a JPA entity (map to a database table)
@Entity

// Tell JPA which table maps to games
@Table(name = "games")

public class Game {

    // Mark the id field as the primary key
    @Id

    // the database generate the id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nba_game_id", nullable = false)
    private Integer nbaGameId;

    @ManyToOne
    // home team id
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne
    // away team id
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    // home team score
    @Column(name = "home_team_score", nullable = false)
    private Integer homeTeamScore;

    // away team score
    @Column(name = "away_team_score", nullable = false)
    private Integer awayTeamScore;

    // game date
    @Column(name = "game_date", nullable = false)
    private LocalDate gameDate;

    // season
    @Column(name = "season", nullable = false)
    private String season;

    public Game() {

    }

    // getter to get the Id
    public Long getId() {
        return id;
    }

    // getter to get the nba game id
    public Integer getNbaGameId() {
        return nbaGameId;
    }

    // setter to set the nba game id
    public void setNbaGameId(Integer nbaGameId) {
        this.nbaGameId = nbaGameId;
    }

    // getter to get home team id
    public Team getHomeTeam() {
        return homeTeam;
    }

    // setter to set the home team id
    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    // getter to get away team id
    public Team getAwayTeam() {
        return awayTeam;
    }

    // setter to set away team id
    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    // getter to get home team score
    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    // setter to set home team score
    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    // getter to get away team score
    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    // setter to set away team score
    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    // getter to get game date
    public LocalDate getGameDate() {
        return gameDate;
    }

    // setter to set game date
    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    // getter to get season
    public String getSeason() {
        return season;
    }

    // setter to set season
    public void setSeason(String season) {
        this.season = season;
    }

}

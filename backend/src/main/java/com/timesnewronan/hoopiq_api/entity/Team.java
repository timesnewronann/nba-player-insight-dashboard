package com.timesnewronan.hoopiq_api.entity;

import jakarta.persistence.*;

// Mark the class a JPA entity (map to database table)
@Entity

// Tell JPA which table maps to Teams
@Table(name = "teams")

public class Team {

    // Mark the Id field as the primary key
    @Id

    // the database generates the id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nba team id
    @Column(name = "nba_team_id", nullable = false)
    private Integer nbaTeamId;

    // team name
    @Column(name = "team_name", nullable = false)
    private String teamName;

    // abbreviation
    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;

    // city
    @Column(name = "city")
    private String city;

    // conference
    @Column(name = "conference")
    private String conference;

    // division
    @Column(name = "division")
    private String division;

    public Team() {

    }

    // getter to get the Id
    public Long getId() {
        return id;
    }

    // getter to get the team id
    public Integer getNbaTeamId() {
        return nbaTeamId;
    }

    // setter to set the team id
    public void setNbaTeamId(Integer nbaTeamId) {
        this.nbaTeamId = nbaTeamId;
    }

    // getter to get team name
    public String getTeamName() {
        return teamName;
    }

    // setter to set team name
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    // getter to get abbreviation
    public String getAbbreviation() {
        return abbreviation;
    }

    // setter to set abbreviation
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    // getter to get city
    public String getCity() {
        return city;
    }

    // setter to set city
    public void setCity(String city) {
        this.city = city;
    }

    // getter to get conference
    public String getConference() {
        return conference;
    }

    // setter to set conference
    public void setConference(String conference) {
        this.conference = conference;
    }

    // getter to get divsion
    public String getDivision() {
        return division;
    }

    // setter to set division
    public void setDivision(String division) {
        this.division = division;
    }

}

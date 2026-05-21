package com.timesnewronan.hoopiq_api.controller;

import com.timesnewronan.hoopiq_api.entity.Team;
import com.timesnewronan.hoopiq_api.entity.Player;

import com.timesnewronan.hoopiq_api.service.TeamService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    // Controller depends on the service layer, not the repository directly
    // This keeps the controller focused on handing HTTP requests
    private final TeamService teamService;
    // private final PlayerService playerService;

    // Spring injects the repository into the controller
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<Team> getAllTeams() {
        // fetches every team in the Teams table
        return teamService.getAllTeams();
    }

    // Add a new endpoint that reads the team id from the url
    // EX: /api/teams/1
    // PathVariable means Spring pulls the id from the URL path
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    // Endpoint to get all players by team id
    @GetMapping("/{id}/players")
    public List<Player> getPlayersByTeamId(@PathVariable Long id) {
        return teamService.getPlayersByTeamId(id);
    }

}

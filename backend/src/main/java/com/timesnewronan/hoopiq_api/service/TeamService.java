package com.timesnewronan.hoopiq_api.service;

import com.timesnewronan.hoopiq_api.entity.Team;
import com.timesnewronan.hoopiq_api.entity.Player;
import com.timesnewronan.hoopiq_api.repository.TeamRepository;
import com.timesnewronan.hoopiq_api.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@Service
public class TeamService {
    // Store the repository as a dependency because the service needs it to fetch
    // team data from the database
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;

    }

    // getAllTeams method
    // return every team in the database
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // getTeamById method
    // returns one team or throws 404
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found with id" + id));
    }

    // getPlayersByTeamId
    // returns players on that team
    public List<Player> getPlayersByTeamId(Long teamId) {
        return playerRepository.findPlayerByTeamId(teamId);
    }

}

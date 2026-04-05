package com.timesnewronan.hoopiq_api.controller;

import com.timesnewronan.hoopiq_api.entity.Player;
import com.timesnewronan.hoopiq_api.repository.PlayerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Tells Spring this class handles HTTP request and returns data directly as JSON
@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    // Spring injevts the repository into the controller
    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Maps HTTP GET requests for /api/players to this Java methods
    @GetMapping("/api/players")
    public List<Player> getAllPlayers() {
        // fetches every row in the players table
        return playerRepository.findAll();
    }
}
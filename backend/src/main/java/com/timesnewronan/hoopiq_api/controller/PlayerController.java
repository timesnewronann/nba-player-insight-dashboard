package com.timesnewronan.hoopiq_api.controller;

import com.timesnewronan.hoopiq_api.entity.Player;
// import com.timesnewronan.hoopiq_api.repository.PlayerRepository;
import com.timesnewronan.hoopiq_api.service.PlayerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// (Entry Point) Handles incoming HTTP rquests (ex GET, POST) acts a "traffic cop" that receives data from the user, calls the appropriate service to process it, and returns a response
// Tells Spring this class handles HTTP request and returns data directly as JSON
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    // Controller depends on the service layer, not the repository directly
    // This keeps the controller focused on handling HTTP requests
    private final PlayerService playerService;

    // Spring injevts the repository into the controller
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Maps HTTP GET requests for /api/players to this Java methods
    @GetMapping("/api/players")
    public List<Player> getAllPlayers() {
        // fetches every row in the players table
        return playerService.getAllPlayers();
    }

    // Add a new endpoint that accepts a query parameter
    // This query request for a specific player
    // EX: /api/players/search?query=Lebron
    @GetMapping("/api/players")
    public List<Player> searchPlayers(@RequestParam("query") String query) {
        // Controller job
        // receive the HTTP input and pass it to the service's layer
        return playerService.searchPlayers(query);
    }
}
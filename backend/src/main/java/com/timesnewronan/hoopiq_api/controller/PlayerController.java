package com.timesnewronan.hoopiq_api.controller;

import com.timesnewronan.hoopiq_api.entity.Player;
import com.timesnewronan.hoopiq_api.entity.PlayerSeasonStat;
// import com.timesnewronan.hoopiq_api.repository.PlayerRepository;
import com.timesnewronan.hoopiq_api.service.PlayerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public List<Player> getAllPlayers() {
        // fetches every row in the players table
        return playerService.getAllPlayers();
    }

    // Add a new endpoint that accepts a query parameter
    // This query request for a specific player
    // EX: /api/players/search?query=Lebron
    @GetMapping("/search")
    public List<Player> searchPlayers(@RequestParam("query") String query) {
        // Controller job
        // receive the HTTP input and pass it to the service's layer
        return playerService.searchPlayers(query);
    }

    // Adding a new route that reads the player id from the URL
    // EX: /api/players/1
    // PathVariable means Spring pulls the id from the URL path
    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    // Adding a new endpoint to get the player's season stats

    @GetMapping("/{id}/season-stats")
    public List<PlayerSeasonStat> getSeasonStatsByPlayerId(@PathVariable Long id) {
        return playerService.getSeasonStatsByPlayerId(id);
    }

    // Request for player's games
    @GetMapping("/{id}/games")
    public List<PlayerGameStat>getPlayerGameStatsByPlayerId(@PathVariable Long id) {
        return playerService;
    }
}
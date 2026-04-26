package com.timesnewronan.hoopiq_api.service;

import com.timesnewronan.hoopiq_api.entity.Player;
import com.timesnewronan.hoopiq_api.entity.PlayerGameStat;
import com.timesnewronan.hoopiq_api.entity.PlayerSeasonStat;
import com.timesnewronan.hoopiq_api.repository.PlayerGameStatRepository;
import com.timesnewronan.hoopiq_api.repository.PlayerRepository;
import com.timesnewronan.hoopiq_api.repository.PlayerSeasonStatRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PlayerService {
    // We store the repository as a dependency because the service needs it to fetch
    // player data from the databse
    private final PlayerRepository playerRepository;
    private final PlayerSeasonStatRepository playerSeasonStatRepository;
    private final PlayerGameStatRepository playerGameStatRepository;

    // Constructor injection is the preferred Spring style
    // Spring will automatically pass in the PlayerRepository
    public PlayerService(PlayerRepository playerRepository, PlayerSeasonStatRepository playerSeasonStatRepository,
            PlayerGameStatRepository playerGameStatRepository) {
        this.playerRepository = playerRepository;
        this.playerSeasonStatRepository = playerSeasonStatRepository;
        this.playerGameStatRepository = playerGameStatRepository;
    }

    // getAllPlayers method
    // Return every player in the database
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // Return players that match the search query
    public List<Player> searchPlayers(String query) {
        // If the query is null or only spaces -> return an empty list
        // no need to run a pointless query
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // trim() removes extra spaces before or after the user input
        // " curry " -> "curry"
        String cleanedQuery = query.trim();

        // After cleaning the input return the search to the repository
        return playerRepository.searchPlayers(cleanedQuery);

    }

    /*
     * Add a new method that:
     * - accepts an id
     * - asks the repository for that player
     * - returns the player if found
     */

    public Player getPlayerById(Long id) {
        // findById returns an Optional<Player>
        // If the player exists, return it
        // If not, throw an exception with a clear message
        return playerRepository.findById(id).orElseThrow(() -> new RuntimeException("Player not found with id: " + id));
    }

    /*
     * Method that gets all season stats for one player
     */
    public List<PlayerSeasonStat> getSeasonStatsByPlayerId(Long playerId) {
        // Verify that player exists
        // This will give us a failure if someone requests stats for a nonexistent
        // player id
        getPlayerById(playerId);

        // fetch all season stat rows for the requested player
        return playerSeasonStatRepository.findByPlayerId(playerId);
    }

    /*
     * Method that gets game stats for one player
     */
    public List<PlayerGameStat> getPlayerGameStatsByPlayerId(Long playerId) {
        // verify that the player exists
        getPlayerById(playerId);

        // fetch the game stats for that player
        return playerGameStatRepository.findByPlayerId(playerId);
    }
}

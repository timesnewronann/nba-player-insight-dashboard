package com.timesnewronan.hoopiq_api.service;

import com.timesnewronan.hoopiq_api.entity.Player;
import com.timesnewronan.hoopiq_api.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PlayerService {
    // We store the repository as a dependency because the service needs it to fetch
    // player data from the databse
    private final PlayerRepository playerRepository;

    // Constructor injection is the preferred Spring style
    // Spring will automatically pass in the PlayerRepository
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // getAllPlayers method
    // Return every player in the database
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

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
}

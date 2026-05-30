package com.timesnewronan.hoopiq_api.service;

import com.timesnewronan.hoopiq_api.entity.ShotChart;
import com.timesnewronan.hoopiq_api.repository.ShotChartRepository;
import com.timesnewronan.hoopiq_api.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ShotChartService {
    // We store the repository as a dependency because the service needs it to fetch
    // shot chart data from the database
    private final PlayerRepository playerRepository;
    private final ShotChartRepository shotChartRepository;

    // Constructor injectiojn is the preferred Spring style
    // Spring will automatically pass in the Shot chart repository
    public ShotChartService(ShotChartRepository shotChartRepository,
            PlayerRepository playerRepository) {
        this.shotChartRepository = shotChartRepository;
        this.playerRepository = playerRepository;

    }

    /*
     * getShotsByPlayerId(Long playerId) — returns all shots for a player
     * getShotsByPlayerIdAndGameId(Long playerId, Long gameId) — returns shots for a
     * specific game
     */

    public List<ShotChart> getShotsByPlayerId(Long playerId) {
        playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Player not found with id " + playerId));

        return shotChartRepository.findByPlayerIdOrderByGameDateDesc(playerId);
    }

    public List<ShotChart> getShotsByPlayerIdAndGameId(Long playerId, Long gameId) {
        playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Player not found with id " + playerId));

        return shotChartRepository.findByPlayerIdAndGameIdOrderByGameDateDesc(playerId, gameId);
    }

}

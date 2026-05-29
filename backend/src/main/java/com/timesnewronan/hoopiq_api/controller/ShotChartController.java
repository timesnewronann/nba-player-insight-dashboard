package com.timesnewronan.hoopiq_api.controller;

import com.timesnewronan.hoopiq_api.entity.Player;
import com.timesnewronan.hoopiq_api.entity.ShotChart;

import com.timesnewronan.hoopiq_api.service.ShotChartService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Entry Point Handles incoming HTTP requests 
// Tells Spring that this class handles HTTP requests and returns data directly as JSON

@RestController
@RequestMapping("/api/players")
public class ShotChartController {
    // Controller depends on the service layer, not the repository directly
    // Controller is focused on handling HTTP requests
    private final ShotChartService shotChartService;

    // Spring injects the repository into the controller
    public ShotChartController(ShotChartService shotChartService) {
        this.shotChartService = shotChartService;
    }

    /*
     * GET /api/players/{id}/shots — all shots for a player
     * GET /api/players/{id}/shots?gameId={gameId} — shots filtered by game
     */

    // GET /api/players/{id}/shots — all shots for a player
    @GetMapping("/{id}/shots")
    public List<ShotChart> getShots(
            @PathVariable Long id,
            @RequestParam(value = "gameId", required = false) Long gameId) {
        if (gameId != null) {
            return shotChartService.getShotsByPlayerIdAndGameId(id, gameId);
        }
        return shotChartService.getShotsByPlayerId(id);
    }

}

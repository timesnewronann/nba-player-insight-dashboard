package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.ShotChart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// allows us to fetch shot chart stats rows from the database
public interface ShotChartRepository extends JpaRepository<ShotChart, Long> {

    // Find all shots for a player — findByPlayerId
    List<ShotChart> findByPlayerIdOrderByGameDateDesc(Long playerId);

    // Find shots for a player filtered by game — findByPlayerIdAndGameId
    List<ShotChart> findByPlayerIdAndGameIdOrderByGameDateDesc(Long playerId, Long gameId);
}

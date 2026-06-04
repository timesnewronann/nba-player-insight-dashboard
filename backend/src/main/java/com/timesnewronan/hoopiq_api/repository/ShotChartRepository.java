package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.ShotChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// allows us to fetch shot chart stats rows from the database
public interface ShotChartRepository extends JpaRepository<ShotChart, Long> {

    // Find all shots for a player
    @Query("SELECT s FROM ShotChart s WHERE s.player.id = :playerId ORDER BY s.gameDate DESC")
    List<ShotChart> findByPlayerIdOrderByGameDateDesc(@Param("playerId") Long playerId);

    // Find shots for a player filtered by game
    @Query("SELECT s FROM ShotChart s WHERE s.player.id = :playerId AND s.game.id = :gameId ORDER BY s.gameDate DESC")
    List<ShotChart> findByPlayerIdAndGameIdOrderByGameDateDesc(@Param("playerId") Long playerId, @Param("gameId") Long gameId);
}

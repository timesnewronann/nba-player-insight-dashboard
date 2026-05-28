package com.timesnewronan.hoopiq_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.timesnewronan.hoopiq_api.entity.PlayerGameStat;
import org.springframework.data.jpa.repository.JpaRepository;

// import list 
import java.util.List;

// fetch game stat rows from the database
public interface PlayerGameStatRepository extends JpaRepository<PlayerGameStat, Long> {
    // Interface tells Spring Data JPA to find all game stat rows
    // Where the related player id matches the given value

    @Query("SELECT p FROM PlayerGameStat p WHERE p.player.id = :playerId ORDER BY p.game.gameDate DESC NULLS LAST")
    List<PlayerGameStat> findByPlayerIdOrderByGameDateDesc(@Param("playerId") Long playerId);

}

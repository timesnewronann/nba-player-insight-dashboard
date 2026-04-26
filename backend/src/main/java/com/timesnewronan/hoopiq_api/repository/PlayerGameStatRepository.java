package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.PlayerGameStat;
import org.springframework.data.jpa.repository.JpaRepository;

// import list 
import java.util.List;

// fetch game stat rows from the database
public interface PlayerGameStatRepository extends JpaRepository<PlayerGameStat, Long> {
    // Interface tells Spring Data JPA to find all game stat rows
    // Where the related player id matches the given value
    List<PlayerGameStat> findByPlayerId(Long playerId);

}

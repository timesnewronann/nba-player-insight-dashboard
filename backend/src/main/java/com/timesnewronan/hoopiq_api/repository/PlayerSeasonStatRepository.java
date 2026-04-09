package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.PlayerSeasonStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// allows us to fetch season stats rows from the database
public interface PlayerSeasonStatRepository extends JpaRepository<PlayerSeasonStat, Long> {
    // This interface tells Spring Data JPA to find all season stat rows
    // where the related player id matches the given values
    List<PlayerSeasonStat> findByPlayerId(Long playerId);
}

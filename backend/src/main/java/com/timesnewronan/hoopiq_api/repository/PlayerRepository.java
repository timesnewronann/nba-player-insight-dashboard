package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

// Gives us a read-made repository with build-in methods
public interface PlayerRepository extends JpaRepository<Player, Long> {
}

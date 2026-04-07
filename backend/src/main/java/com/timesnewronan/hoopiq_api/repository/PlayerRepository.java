package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // added missing query import
import org.springframework.data.repository.query.Param;

import java.util.List; // added missing List import

// Gives us a read-made repository with build-in methods
// findAll()
// findById()
// save()
// deleteById()
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /*
     * This custom JPQL query searches across full, name, first name, and last name
     * LOWER() is used for case-insensitive
     * % around the search term so it behaves like a "contains" search
     */
    @Query("""
            SELECT p
            FROM Player p
            WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    List<Player> searchPlayers(@Param("searchTerm") String searchTerm);
}

package com.timesnewronan.hoopiq_api.entity;

// import specification for mapping Java Objects to relational database
import jakarta.persistence.*;

// Import big decimal
import java.math.BigDecimal;

// Mark the class as a JPA entity (map to a database table)
@Entity

// Tell JPA that the table maps to player_season_stats
@Table(name = "player_season_stats")

public class PlayerSeasonStat {
    // Mark the field as the primary key
    @Id

    // the database generates the id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A player can have mulitple season stat rows
    // player_season_stats -> players
    @ManyToOne

    // player_id column connects to the players table
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // Season Column
    @Column(nullable = false)
    private String season;

    // player season stats variables

}

package com.timesnewronan.hoopiq_api.repository;

import com.timesnewronan.hoopiq_api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    /*
     * FindAll() comes from JpaRepostiroy no need to declare it
     * findTeamById(long id) comes from JpaRepository and returns Optional<Team>
     * don't need a custom version
     */
}

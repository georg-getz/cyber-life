package org.softuni.esports.repository;

import org.softuni.esports.domain.entities.JoinTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinTournamentRepository extends JpaRepository<JoinTournament, String> {
    @Query(value = "SELECT * FROM tournament_applications WHERE user_id = :userId"
            , nativeQuery = true)
    List<JoinTournament> findAllByUserId(@Param("userId") String userId);
}

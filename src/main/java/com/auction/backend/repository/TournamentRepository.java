package com.auction.backend.repository;

import com.auction.backend.dto.StandingResponse;
import com.auction.backend.entity.Match;
import com.auction.backend.entity.Tournament;
import com.auction.backend.enums.TournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament,Long> {

    List<Tournament> findByStatus(TournamentStatus status);

    @Query("""
       SELECT DISTINCT t
       FROM Tournament t
       LEFT JOIN FETCH t.tournamentTeams tt
       LEFT JOIN FETCH tt.team
       """)
    List<Tournament> findAllWithTeams();


}

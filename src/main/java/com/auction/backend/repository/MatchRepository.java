package com.auction.backend.repository;

import com.auction.backend.entity.Match;
import com.auction.backend.entity.Tournament;
import com.auction.backend.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {
    List<Match> findByTournamentOrderByIdAsc(Tournament tournament);


    List<Match> findByTournamentAndStatus(Tournament tournament, MatchStatus status);

    long countByTournamentAndStatus(Tournament tournament, MatchStatus status);
}

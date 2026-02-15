package com.auction.backend.repository;

import com.auction.backend.entity.Members;
import com.auction.backend.entity.Tournament;
import com.auction.backend.entity.TournamentTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TournamentTeamRepository extends JpaRepository<TournamentTeam,Long> {
    List<TournamentTeam> findByTournament(Tournament tournament);

    Optional<TournamentTeam> findByTournamentAndTeam(Tournament tournament, Members team);

    void deleteByTournament(Tournament tournament);
}

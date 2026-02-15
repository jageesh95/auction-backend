package com.auction.backend.repository;

import com.auction.backend.entity.Members;
import com.auction.backend.entity.Standing;
import com.auction.backend.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingRepository extends JpaRepository<Standing,Long> {
    Optional<Standing> findByTournamentAndTeam(Tournament tournament, Members team);

    List<Standing> findByTournamentOrderByPointsDescGoalDifferenceDescGoalsForDesc(Tournament tournament);

}

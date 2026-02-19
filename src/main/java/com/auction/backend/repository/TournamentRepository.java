package com.auction.backend.repository;

import com.auction.backend.entity.Match;
import com.auction.backend.entity.Tournament;
import com.auction.backend.enums.TournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament,Long> {

    List<Tournament> findByStatus(TournamentStatus status);
}

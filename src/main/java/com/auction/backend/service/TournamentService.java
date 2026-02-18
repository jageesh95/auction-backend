package com.auction.backend.service;

import com.auction.backend.dto.AssignTeamsRequest;
import com.auction.backend.dto.TournamentRequest;
import com.auction.backend.dto.TournamentResponse;
import com.auction.backend.dto.UpdateScoreRequest;

import java.util.List;

public interface TournamentService {

    TournamentResponse createTournament(TournamentRequest request);
    void assignTeams(Long tournamentId, AssignTeamsRequest request);
    void generateMatches(Long tournamentId);

    void updateMatchScore(Long matchId, UpdateScoreRequest request);

    List<TournamentResponse> getAll();
}

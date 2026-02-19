package com.auction.backend.service;

import com.auction.backend.dto.*;

import java.util.List;

public interface TournamentService {

    TournamentResponse createTournament(TournamentRequest request);
    String assignTeams(Long tournamentId, AssignTeamsRequest request);
    void generateMatches(Long tournamentId);

    void updateMatchScore(Long matchId, UpdateScoreRequest request);

    List<MatchResponse> getMatchesByTournament(Long tournamentId);
    List<TournamentResponse> getAll();
}

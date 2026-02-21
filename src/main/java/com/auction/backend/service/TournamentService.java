package com.auction.backend.service;

import com.auction.backend.dto.*;
import com.auction.backend.entity.Tournament;
import com.auction.backend.enums.MatchStatus;

import java.util.List;

public interface TournamentService {

    TournamentResponse createTournament(TournamentRequest request);
    TournamentResponse updateTournament(Long id,TournamentRequest request);
    String assignTeams(Long tournamentId, AssignTeamsRequest request);
    void generateMatches(Long tournamentId);

    void updateMatchScore(Long matchId, UpdateScoreRequest request);

    List<MatchResponse> getMatchesByTournament(Long tournamentId);
    List<TournamentResponse> getAll();

    List<MatchResponse> getMatchesByStatus(Long tournamentId, MatchStatus status);

    List<MatchResponse> getMatchesByTeam(Long teamId);

    MatchResponse getMatch(Long matchId);
    List<TournamentTeamResponse> getAllTournaments();

    List<StandingResponse> getStandings(Tournament tournament);
}

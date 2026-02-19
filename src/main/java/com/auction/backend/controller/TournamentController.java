package com.auction.backend.controller;

import com.auction.backend.dto.*;
import com.auction.backend.entity.Match;
import com.auction.backend.entity.Standing;
import com.auction.backend.entity.Tournament;
import com.auction.backend.repository.MatchRepository;
import com.auction.backend.repository.StandingRepository;
import com.auction.backend.repository.TournamentRepository;
import com.auction.backend.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;
    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;

    @PostMapping
    public TournamentResponse createTournament(
            @RequestBody TournamentRequest request) {

        return tournamentService.createTournament(request);
    }
    @GetMapping("/{tournamentId}/matches")
    public List<MatchResponse> getTournamentMatches(
            @PathVariable Long tournamentId) {

        return tournamentService.getMatchesByTournament(tournamentId);
    }
    @GetMapping
    public List<TournamentResponse> getTournament(){
        return tournamentService.getAll();
    }
    @PostMapping("/{tournamentId}/teams")
    public String assignTeams(
            @PathVariable Long tournamentId,
            @RequestBody AssignTeamsRequest request) {

        return tournamentService.assignTeams(tournamentId, request);
    }

    @PostMapping("/{tournamentId}/generate-matches")
    public String generateMatches(@PathVariable Long tournamentId) {

        tournamentService.generateMatches(tournamentId);
        return "Matches generated successfully";
    }

    @PutMapping("/matches/{matchId}/score")
    public String updateScore(
            @PathVariable Long matchId,
            @RequestBody UpdateScoreRequest request) {

        tournamentService.updateMatchScore(matchId, request);
        return "Score updated successfully";
    }
    @GetMapping("/{tournamentId}/standings")
    public List<Standing> getStandings(@PathVariable Long tournamentId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow();

        return standingRepository
                .findByTournamentOrderByPointsDescGoalDifferenceDescGoalsForDesc(tournament);
    }

}

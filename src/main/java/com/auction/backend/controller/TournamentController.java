package com.auction.backend.controller;

import com.auction.backend.dto.AssignTeamsRequest;
import com.auction.backend.dto.TournamentRequest;
import com.auction.backend.dto.TournamentResponse;
import com.auction.backend.dto.UpdateScoreRequest;
import com.auction.backend.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public TournamentResponse createTournament(
            @RequestBody TournamentRequest request) {

        return tournamentService.createTournament(request);
    }
    @PostMapping("/{tournamentId}/teams")
    public String assignTeams(
            @PathVariable Long tournamentId,
            @RequestBody AssignTeamsRequest request) {

        tournamentService.assignTeams(tournamentId, request);
        return "Teams assigned successfully";
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
}

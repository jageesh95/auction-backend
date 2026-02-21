package com.auction.backend.service.serviceImpl;

import com.auction.backend.dto.*;
import com.auction.backend.entity.*;
import com.auction.backend.enums.MatchStatus;
import com.auction.backend.enums.TournamentStatus;
import com.auction.backend.exception.ResourceNotFoundException;
import com.auction.backend.repository.*;
import com.auction.backend.service.TournamentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final MembersRepository membersRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;

    @Override
    public TournamentResponse createTournament(TournamentRequest request) {

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(TournamentStatus.UPCOMING) // default
                .build();

        Tournament saved = tournamentRepository.save(tournament);

        return TournamentResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .status(saved.getStatus())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .build();
    }

    @Override
    public TournamentResponse updateTournament(Long id,TournamentRequest request) {
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        tournament.setName(request.getName());
        Tournament saved =tournamentRepository.save(tournament);

        return mapToDto(saved);
    }

    @Transactional
    @Override
    public String assignTeams(Long tournamentId, AssignTeamsRequest request) {

        String result="";
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        if (tournament.getStatus() != TournamentStatus.UPCOMING) {
            throw new RuntimeException("Cannot assign teams after tournament started");
        }

        for (Long teamId : request.getTeamIds()) {

            Members team = membersRepository.findById(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));

            // prevent duplicate mapping
            boolean alreadyExists =
                    tournamentTeamRepository
                            .findByTournamentAndTeam(tournament, team)
                            .isPresent();

            if (!alreadyExists) {

                // insert into mapping table
                TournamentTeam mapping = TournamentTeam.builder()
                        .tournament(tournament)
                        .team(team)
                        .build();

                tournamentTeamRepository.save(mapping);

                // create initial standing row
                Standing standing = Standing.builder()
                        .tournament(tournament)
                        .team(team)
                        .played(0)
                        .win(0)
                        .draw(0)
                        .loss(0)
                        .goalsFor(0)
                        .goalsAgainst(0)
                        .goalDifference(0)
                        .points(0)
                        .build();

                standingRepository.save(standing);
                result="added successfully";
            }
            else{
                result="team already present";
            }

        }
        return result;
    }

    @Override
    public void generateMatches(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        if (tournament.getStatus() != TournamentStatus.UPCOMING) {
            throw new RuntimeException("Matches already generated or tournament started");
        }

        // Fetch assigned teams
        List<TournamentTeam> mappings =
                tournamentTeamRepository.findByTournament(tournament);

        if (mappings.size() < 2) {
            throw new RuntimeException("Minimum 2 teams required");
        }

        List<Members> teams = mappings.stream()
                .map(TournamentTeam::getTeam)
                .toList();

        List<Match> matches = new ArrayList<>();

        // Round Robin Logic
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {

                Match match = Match.builder()
                        .tournament(tournament)
                        .teamA(teams.get(i))
                        .teamB(teams.get(j))
                        .status(MatchStatus.SCHEDULED)
                        .scoreA(0)
                        .scoreB(0)
                        .build();

                matches.add(match);
            }
        }

        // Shuffle matches for random order
        Collections.shuffle(matches);

        matchRepository.saveAll(matches);
        // Mark tournament as ONGOING
        tournament.setStatus(TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);

    }

    @Transactional
    @Override
    public void updateMatchScore(Long matchId, UpdateScoreRequest request) {

            Match match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found"));

            if (match.getStatus() == MatchStatus.COMPLETED) {
                throw new RuntimeException("Match already completed");
            }
        match.setScoreA(request.getScoreA());
        match.setScoreB(request.getScoreB());
        match.setStatus(MatchStatus.COMPLETED);

        updateStandings(match);

        matchRepository.save(match);

        checkAndCompleteTournament(match.getTournament());
    }


    @Override
    public List<MatchResponse> getMatchesByTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        List<Match> matches = matchRepository
                .findByTournamentOrderByIdAsc(tournament);

        return matches.stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public List<TournamentResponse> getAll() {
        List<Tournament> data= tournamentRepository.findAll();
        List<TournamentResponse> response=new ArrayList<>();
        for(Tournament val:data){
            response.add(mapToDto(val));
        }
        return response;
    }

    @Override
    public List<MatchResponse> getMatchesByStatus(Long tournamentId, MatchStatus status) {
        return null;
    }

    @Override
    public List<MatchResponse> getMatchesByTeam(Long teamId) {
      //  return matchRepository.findMatchesByTeamId(teamId);
        return null;
    }

    @Override
    public MatchResponse getMatch(Long matchId) {
        return null;
    }

    private void checkAndCompleteTournament(Tournament tournament) {

        long remainingMatches = matchRepository
                .countByTournamentAndStatus(tournament, MatchStatus.SCHEDULED);

        if (remainingMatches == 0) {
            tournament.setStatus(TournamentStatus.COMPLETED);
            tournamentRepository.save(tournament);
        }
    }

    private void updateStandings(Match match) {

        Standing standingA = standingRepository
                .findByTournamentAndTeam(match.getTournament(), match.getTeamA())
                .orElseThrow();

        Standing standingB = standingRepository
                .findByTournamentAndTeam(match.getTournament(), match.getTeamB())
                .orElseThrow();

        // Played
        standingA.setPlayed(standingA.getPlayed() + 1);
        standingB.setPlayed(standingB.getPlayed() + 1);

        // Goals
        standingA.setGoalsFor(standingA.getGoalsFor() + match.getScoreA());
        standingA.setGoalsAgainst(standingA.getGoalsAgainst() + match.getScoreB());

        standingB.setGoalsFor(standingB.getGoalsFor() + match.getScoreB());
        standingB.setGoalsAgainst(standingB.getGoalsAgainst() + match.getScoreA());

        // Goal Difference
        standingA.setGoalDifference(
                standingA.getGoalsFor() - standingA.getGoalsAgainst());

        standingB.setGoalDifference(
                standingB.getGoalsFor() - standingB.getGoalsAgainst());

        // Win / Loss / Draw
        if (match.getScoreA() > match.getScoreB()) {
            standingA.setWin(standingA.getWin() + 1);
            standingB.setLoss(standingB.getLoss() + 1);
            standingA.setPoints(standingA.getPoints() + 3);
        }
        else if (match.getScoreA() < match.getScoreB()) {
            standingB.setWin(standingB.getWin() + 1);
            standingA.setLoss(standingA.getLoss() + 1);
            standingB.setPoints(standingB.getPoints() + 3);
        }
        else {
            standingA.setDraw(standingA.getDraw() + 1);
            standingB.setDraw(standingB.getDraw() + 1);
            standingA.setPoints(standingA.getPoints() + 1);
            standingB.setPoints(standingB.getPoints() + 1);
        }

        standingRepository.save(standingA);
        standingRepository.save(standingB);

    }

    private MatchResponse mapToResponse(Match match) {

        return MatchResponse.builder()
                .matchId(match.getId())
                .teamAName(match.getTeamA().getName())
                .teamBName(match.getTeamB().getName())
                .scoreA(match.getScoreA())
                .scoreB(match.getScoreB())
                .status(match.getStatus().name())
                .build();
    }
    private TournamentResponse mapToDto(Tournament t){


        return TournamentResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .status(t.getStatus())
                .startDate(t.getStartDate())
                .endDate(t.getEndDate())
                .build();


    }

    @Override
    public List<TournamentTeamResponse> getAllTournaments() {

        List<Tournament> tournaments = tournamentRepository.findAllWithTeams();

        return tournaments.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TournamentTeamResponse mapToResponse(Tournament tournament) {

        List<TeamDTO> teams = tournament.getTournamentTeams()
                .stream()
                .map(tt -> new TeamDTO(
                        tt.getTeam().getId(),
                        tt.getTeam().getTeamName(),
                        tt.getTeam().getName()
                ))
                .toList();

        return TournamentTeamResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .status(tournament.getStatus().name())
                .teams(teams)
                .build();
    }
}

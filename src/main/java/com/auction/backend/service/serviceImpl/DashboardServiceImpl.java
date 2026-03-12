package com.auction.backend.service.serviceImpl;

import com.auction.backend.dto.*;
import com.auction.backend.entity.Members;
import com.auction.backend.entity.User;
import com.auction.backend.repository.MatchRepository;
import com.auction.backend.repository.MembersRepository;
import com.auction.backend.repository.TournamentTeamRepository;
import com.auction.backend.repository.UserRepository;
import com.auction.backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final MembersRepository membersRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;

    public DashboardServiceImpl(UserRepository userRepository, MembersRepository membersRepository, TournamentTeamRepository tournamentTeamRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.membersRepository = membersRepository;
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public DashboardResponse getDashboard(Long id) {

        // 1️⃣ Get logged user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Get member/team
        Members member = membersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 3️⃣ Get tournament dashboard projection
        List<TournamentDashboardProjection> projections =
                tournamentTeamRepository.getDashboardData(member.getId());

        // If user not part of any tournament
        if (projections.isEmpty()) {

            DashboardResponse response = new DashboardResponse();
            response.setUserName(member.getName());
            response.setTeamName(member.getTeamName());
            response.setTournaments(Collections.emptyList());

            return response;
        }
        // 4️⃣ Get tournament ids
        List<Long> tournamentIds = projections.stream()
                .map(TournamentDashboardProjection::getTournamentId)
                .distinct()
                .toList();

        // 6️⃣ Build tournament dashboard
        List<TournamentDashboardDTO> tournaments = new ArrayList<>();

        for (TournamentDashboardProjection p : projections) {

            TournamentDashboardDTO dto = new TournamentDashboardDTO();

            dto.setTournamentId(p.getTournamentId());
            dto.setTournamentName(p.getTournamentName());
            dto.setTeamBalance(p.getRemainingBudget());
            dto.setPlayersBought(p.getPlayersBought());

            // Auction mapping
            if (p.getAuctionId() != null) {

                AuctionDto auctionDTO = new AuctionDto();
                auctionDTO.setAuctionId(p.getAuctionId());
                auctionDTO.setStatus(p.getAuctionStatus());

                if (p.getPlayerId() != null) {

                    PlayerBidDto playerDTO = new PlayerBidDto();
                    playerDTO.setPlayerId(p.getPlayerId());
                    playerDTO.setPlayerName(p.getPlayerName());
                    playerDTO.setCurrentBid(p.getCurrentBid());

                    auctionDTO.setCurrentPlayer(playerDTO);
                }

                dto.setAuction(auctionDTO);
            }
            tournaments.add(dto);
        }
        DashboardResponse response = new DashboardResponse();

        response.setUserName(member.getName());
        response.setTeamName(member.getTeamName());
        response.setTournaments(tournaments);

        return response;
    }
}

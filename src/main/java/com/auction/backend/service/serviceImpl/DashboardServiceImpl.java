package com.auction.backend.service.serviceImpl;

import com.auction.backend.dto.*;
import com.auction.backend.entity.Members;
import com.auction.backend.entity.User;
import com.auction.backend.repository.*;
import com.auction.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final MembersRepository membersRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final AuctionRepository auctionRepository;
    private final MatchRepository matchRepository;


    @Override
    public AdminDashboardResponse getAdminDashboard(Long id) {
        AdminDashboardResponse response=new AdminDashboardResponse();
        response.setTotalPlayers(playerRepository.count());
        response.setTotalMembers(playerRepository.count());
        response.setTotalTournaments(playerRepository.count());
        response.setActiveAuctions(auctionRepository.count());

        return response;
    }

    @Override
    public MemberDashboardResponse getMemberDashboard(Long id) {

        // 1️⃣ Get logged user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Get member/team
        Members member = membersRepository.findByUserId(user.getId());


        // 3️⃣ Get tournament dashboard projection
        List<TournamentDashboardProjection> projections =
                tournamentTeamRepository.getDashboardData(member.getId());

        // If user not part of any tournament
        if (projections.isEmpty()) {

            MemberDashboardResponse response = new MemberDashboardResponse();
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
        MemberDashboardResponse response = new MemberDashboardResponse();

        response.setUserName(member.getName());
        response.setTeamName(member.getTeamName());
        response.setTournaments(tournaments);

        return response;
    }
}

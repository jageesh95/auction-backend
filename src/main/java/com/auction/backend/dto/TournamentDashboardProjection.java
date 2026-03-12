package com.auction.backend.dto;

import com.auction.backend.enums.AuctionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Data
public class TournamentDashboardProjection {
    private Long tournamentId;
    private String tournamentName;
    private Double remainingBudget;
    private Integer playersBought;
    private Long auctionId;

    private AuctionStatus auctionStatus;
    private Long playerId;
    private String playerName;
    private Double currentBid;

    public TournamentDashboardProjection(
            Long tournamentId,
            String tournamentName,
            Double remainingBudget,
            Integer playersBought,
            Long auctionId,
            AuctionStatus auctionStatus,
            Long playerId,
            String playerName,
            Double currentBid) {

        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.remainingBudget = remainingBudget;
        this.playersBought = playersBought;
        this.auctionId = auctionId;
        this.auctionStatus=auctionStatus;
        this.playerId = playerId;
        this.playerName = playerName;
        this.currentBid = currentBid;
    }
}

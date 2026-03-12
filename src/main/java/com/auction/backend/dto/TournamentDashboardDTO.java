package com.auction.backend.dto;

import lombok.Data;

@Data
public class TournamentDashboardDTO {

    private Long tournamentId;

    private String tournamentName;

    private Double teamBalance;

    private Integer playersBought;

    private AuctionDto auction;
}

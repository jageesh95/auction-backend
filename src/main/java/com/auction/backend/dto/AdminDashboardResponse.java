package com.auction.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AdminDashboardResponse {
    private Long totalMembers;
    private Long totalPlayers;
    private Long totalTournaments;
    private Long activeAuctions;

}

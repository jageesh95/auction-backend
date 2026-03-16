package com.auction.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberDashboardResponse {

    private String userName;

    private String teamName;

    private List<TournamentDashboardDTO> tournaments;

}

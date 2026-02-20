package com.auction.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TournamentTeamResponse {

    private Long id;
    private String name;
    private String status;
    private List<TeamDTO> teams;
}

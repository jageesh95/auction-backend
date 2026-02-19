package com.auction.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponse {
    private Long matchId;

    private String teamAName;
    private String teamBName;

    private Integer scoreA;
    private Integer scoreB;

    private String status;
}

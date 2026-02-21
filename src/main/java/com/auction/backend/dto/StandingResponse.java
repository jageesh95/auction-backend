package com.auction.backend.dto;

import lombok.Data;

@Data
public class StandingResponse {
    Long id;
    String name;
    String teamName;

    private int played = 0;

    private int win = 0;

    private int draw = 0;

    private int loss = 0;

    private int goalsFor = 0;

    private int goalsAgainst = 0;

    private int goalDifference = 0;

    private int points = 0;
}

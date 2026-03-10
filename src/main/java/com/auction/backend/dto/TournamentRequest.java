package com.auction.backend.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class TournamentRequest {


    private String name;
    private Double defaultBalance;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minPlayers;
    private Integer MaxPlayers;
}

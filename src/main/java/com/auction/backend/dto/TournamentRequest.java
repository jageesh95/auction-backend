package com.auction.backend.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class TournamentRequest {


    private String name;

    private LocalDate startDate;

    private LocalDate endDate;
}

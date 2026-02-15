package com.auction.backend.dto;

import com.auction.backend.enums.TournamentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class TournamentResponse {

    private Long id;

    private String name;

    private TournamentStatus status;

    private LocalDate startDate;

    private LocalDate endDate;
}

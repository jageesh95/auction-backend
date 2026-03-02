package com.auction.backend.entity;

import com.auction.backend.enums.AuctionStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tournamentId;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status; // CREATED, STARTED, FINISHED

    private Long currentPlayerId; // active player

    private boolean active = true;
}

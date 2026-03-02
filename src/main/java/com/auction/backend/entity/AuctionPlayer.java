package com.auction.backend.entity;

import com.auction.backend.enums.PlayerAuctionStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AuctionPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionId;

    private Long playerId;

    @Enumerated(EnumType.STRING)
    private PlayerAuctionStatus status; // WAITING, ACTIVE, SOLD, UNSOLD
    private Double currentHighestBid;
    private Long currentHighestBidUserId;

    private Long soldToUserId;

    private Double soldAmount;
}

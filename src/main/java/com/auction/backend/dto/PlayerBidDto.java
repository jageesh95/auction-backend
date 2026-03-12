package com.auction.backend.dto;

import lombok.Data;

@Data
public class PlayerBidDto {

    Long playerId;
    String playerName;
    Double currentBid;
}

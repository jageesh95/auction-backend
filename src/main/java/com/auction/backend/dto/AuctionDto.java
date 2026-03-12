package com.auction.backend.dto;

import com.auction.backend.enums.AuctionStatus;
import lombok.Data;

@Data
public class AuctionDto {

    Long auctionId;
    AuctionStatus status;
    PlayerBidDto currentPlayer;
}

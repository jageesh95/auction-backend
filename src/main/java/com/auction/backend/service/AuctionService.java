package com.auction.backend.service;

import com.auction.backend.entity.Auction;
import com.auction.backend.entity.AuctionPlayer;

public interface AuctionService {

    Auction createAuction(Long tournamentId);
    AuctionPlayer addPlayerToAuction(Long auctionId, Long playerId);
    void startAuction(Long auctionId);
    void activatePlayer(Long auctionId, Long auctionPlayerId);
    void placeBid(Long auctionId, Double amount, Long userId);
    void closeCurrentPlayer(Long auctionId);
    void finishAuction(Long auctionId);

}

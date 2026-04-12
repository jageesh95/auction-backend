package com.auction.backend.controller;

import com.auction.backend.dto.BidRequest;
import com.auction.backend.entity.Auction;
import com.auction.backend.entity.AuctionPlayer;
import com.auction.backend.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Auction> createAuction(@RequestParam Long tournamentId){

       Auction auction= auctionService.createAuction(tournamentId);
       return ResponseEntity.ok(auction);
    }

    @PostMapping("/{auctionId}/players")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuctionPlayer> addPlayerToAuction(
            @PathVariable Long auctionId,
            @RequestParam Long playerId) {

       AuctionPlayer auctionPlayer= auctionService.addPlayerToAuction(auctionId, playerId);
        return ResponseEntity.ok(auctionPlayer);
    }

    @PostMapping("/{auctionId}/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> startAuction(
            @PathVariable Long auctionId) {

        auctionService.startAuction(auctionId);
        return ResponseEntity.ok("Auction started");
    }

    @PostMapping("/{auctionId}/activate/{auctionPlayerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activatePlayer(
            @PathVariable Long auctionId,
            @PathVariable Long auctionPlayerId) {

        auctionService.activatePlayer(auctionId, auctionPlayerId);
        return ResponseEntity.ok("Player activated");
    }

    @PostMapping("/{auctionId}/bid")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> placeBid(
            @PathVariable Long auctionId,
            @RequestBody BidRequest request,
            Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        auctionService.placeBid(
                auctionId,
                request.getAmount(),
                userId
        );

        return ResponseEntity.ok("Bid placed successfully");
    }

    @PostMapping("/{auctionId}/close-player")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> closeCurrentPlayer(
            @PathVariable Long auctionId) {

        auctionService.closeCurrentPlayer(auctionId);
        return ResponseEntity.ok("Player closed successfully");
    }


    @PostMapping("/{auctionId}/finish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> finishAuction(
            @PathVariable Long auctionId) {

        auctionService.finishAuction(auctionId);
        return ResponseEntity.ok("Auction finished successfully");
    }

}

package com.auction.backend.service.serviceImpl;

import com.auction.backend.entity.*;
import com.auction.backend.enums.AuctionStatus;
import com.auction.backend.enums.PlayerAuctionStatus;
import com.auction.backend.repository.*;
import com.auction.backend.service.AuctionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionPlayerRepository auctionPlayerRepository;
    private final BidRepository bidRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TournamentRepository tournamentRepository;

    @Override
    @Transactional
    public Auction createAuction(Long tournamentId) {
        Auction auction = new Auction();
        auction.setTournamentId(tournamentId);
        auction.setStatus(AuctionStatus.CREATED);

        return auctionRepository.save(auction);
    }

    @Override
    @Transactional
    public AuctionPlayer addPlayerToAuction(Long auctionId, Long playerId) {
        Auction auction = getAuctionOrThrow(auctionId);

        if (auction.getStatus() == AuctionStatus.FINISHED) {
            throw new RuntimeException("Cannot add players to finished auction");
        }

        AuctionPlayer auctionPlayer = new AuctionPlayer();
        auctionPlayer.setAuctionId(auctionId);
        auctionPlayer.setPlayerId(playerId);
        auctionPlayer.setStatus(PlayerAuctionStatus.WAITING);
        auctionPlayer.setCurrentHighestBid(0.0);

       return auctionPlayerRepository.save(auctionPlayer);

    }

    @Override
    @Transactional
    public void startAuction(Long auctionId) {
        Auction auction = getAuctionOrThrow(auctionId);

        if (auction.getStatus() != AuctionStatus.CREATED) {
            throw new RuntimeException("Auction already started or finished");
        }

        auction.setStatus(AuctionStatus.STARTED);
        auctionRepository.save(auction);
    }

    @Override
    @Transactional
    public void activatePlayer(Long auctionId, Long auctionPlayerId) {
        Auction auction = getAuctionOrThrow(auctionId);

        if (auction.getStatus() != AuctionStatus.STARTED) {
            throw new RuntimeException("Auction not started");
        }

        if (auction.getCurrentPlayerId() != null) {
            throw new RuntimeException("Another player already active");
        }

        AuctionPlayer player = auctionPlayerRepository
                .findById(auctionPlayerId)
                .orElseThrow();

        if (player.getStatus() != PlayerAuctionStatus.WAITING) {
            throw new RuntimeException("Player not in waiting state");
        }

        player.setStatus(PlayerAuctionStatus.ACTIVE);
        auction.setCurrentPlayerId(player.getId());
        auctionRepository.save(auction);

    }

    @Override
    @Transactional
    public void placeBid(Long auctionId, Double amount, Long userId) {
        Auction auction = getAuctionOrThrow(auctionId);

        if (auction.getStatus() != AuctionStatus.STARTED) {
            throw new RuntimeException("Auction not active");
        }

        if (auction.getCurrentPlayerId() == null) {
            throw new RuntimeException("No active player");
        }

       TournamentTeam tournamentTeam=tournamentTeamRepository.findWithLock(auction.getTournamentId(),userId);
        if(tournamentTeam==null){
            throw new RuntimeException("Team not registered in tournament");
        }

        Optional<Tournament> tournament=tournamentRepository.findById(auction.getTournamentId());

        // 🚨 PLAYER LIMIT CHECK
        if(tournamentTeam.getPlayersBought() >= tournament.get().getMaxPlayers()){
            throw new RuntimeException("Team already reached maximum player limit");
        }
        // 🚨 BALANCE CHECK
        if(tournamentTeam.getBalance() < amount){
            throw new RuntimeException("Insufficient balance");
        }


        // 🔒 Lock active player row
        AuctionPlayer activePlayer =
                auctionPlayerRepository.findByIdForUpdate(
                        auction.getCurrentPlayerId());

        if (activePlayer.getStatus() != PlayerAuctionStatus.ACTIVE) {
            throw new RuntimeException("Player not active");
        }


        Double highest = activePlayer.getCurrentHighestBid();


       if (amount <= highest) {
            throw new RuntimeException("Bid must be higher than current highest");
        }

       // future implementation

        /*// 3️⃣ Minimum purse rule
        int playersLeft = tournament.get().getMaxPlayers() - tournamentTeam.getPlayersBought();

        double requiredMinimumBalance =
                (playersLeft - 1) * tournament.getMinPlayerPrice();

        double allowedMaxBid =
                tournamentTeam.getBalance() - requiredMinimumBalance;

        if(amount > allowedMaxBid){
            throw new RuntimeException("Bid exceeds allowed purse limit");
        }
*/


        // Update highest inside same lock
        activePlayer.setCurrentHighestBid(amount);
        activePlayer.setCurrentHighestBidUserId(userId);
        auctionPlayerRepository.save(activePlayer);

        // Save bid history
        Bid bid = new Bid();
        bid.setAuctionId(auctionId);
        bid.setAuctionPlayerId(activePlayer.getId());
        bid.setUserId(userId);
        bid.setAmount(amount);
        bid.setBidTime(LocalDateTime.now());

        bidRepository.save(bid);

    }

    @Override
    @Transactional
    public void closeCurrentPlayer(Long auctionId) {
        Auction auction = getAuctionOrThrow(auctionId);

        if (auction.getCurrentPlayerId() == null) {
            throw new RuntimeException("No active player");
        }

        AuctionPlayer player =
                auctionPlayerRepository.findByIdForUpdate(
                        auction.getCurrentPlayerId());

        if (player.getCurrentHighestBid() > 0) {

            TournamentTeam tm=tournamentTeamRepository.findWithLock(auction.getTournamentId(),player.getCurrentHighestBidUserId());
            if (tm.getBalance() < player.getCurrentHighestBid()) {
                throw new RuntimeException("Insufficient Balance");
            }

            tm.setBalance( (tm.getBalance() - player.getCurrentHighestBid()));
            tm.setTotalSpent( (tm.getTotalSpent() + player.getCurrentHighestBid()));
            tm.setPlayersBought(tm.getPlayersBought() +1);
            tournamentTeamRepository.save(tm);

            player.setStatus(PlayerAuctionStatus.SOLD);
            player.setSoldAmount(player.getCurrentHighestBid());
            player.setSoldToUserId(player.getCurrentHighestBidUserId());
        } else {
            player.setStatus(PlayerAuctionStatus.UNSOLD);
        }
        auctionPlayerRepository.save(player);

        auction.setCurrentPlayerId(null);
        auctionRepository.save(auction);

    }

    @Override
    @Transactional
    public void finishAuction(Long auctionId) {
        Auction auction = getAuctionOrThrow(auctionId);

        if (auction.getCurrentPlayerId() != null) {
            throw new RuntimeException("Close active player first");
        }

        auction.setStatus(AuctionStatus.FINISHED);

    }

    private Auction getAuctionOrThrow(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }
}

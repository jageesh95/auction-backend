package com.auction.backend.repository;

import com.auction.backend.dto.TournamentDashboardProjection;
import com.auction.backend.entity.Members;
import com.auction.backend.entity.Tournament;
import com.auction.backend.entity.TournamentTeam;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TournamentTeamRepository extends JpaRepository<TournamentTeam,Long> {
    List<TournamentTeam> findByTournament(Tournament tournament);

    Optional<TournamentTeam> findByTournamentAndTeam(Tournament tournament, Members team);
    @Query("SELECT tm FROM TournamentTeam tm WHERE tm.tournament.id = :tournamentId AND tm.team.id = :teamId")
    TournamentTeam findByTournamentIdAndTeamId(Long tournamentId,Long teamId);

    void deleteByTournament(Tournament tournament);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tm FROM TournamentTeam tm WHERE tm.tournament.id = :tournamentId AND tm.team.id = :teamId")
    TournamentTeam findWithLock(Long tournamentId, Long teamId);

    @Query("""
SELECT new com.auction.backend.dto.TournamentDashboardProjection(
    t.id,
    t.name,
    tt.balance,
    tt.playersBought,
    a.id,
    a.status,
    p.id,
    p.name,
    ap.currentHighestBid
)
FROM TournamentTeam tt
JOIN tt.tournament t
LEFT JOIN Auction a ON a.tournamentId = t.id
LEFT JOIN AuctionPlayer ap ON ap.auctionId = a.id AND ap.status = 'ACTIVE'
LEFT JOIN Players p ON p.id = ap.playerId
WHERE tt.team.id = :teamId
""")
    List<TournamentDashboardProjection> getDashboardData(Long teamId);
}

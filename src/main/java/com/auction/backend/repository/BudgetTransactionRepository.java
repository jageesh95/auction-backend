package com.auction.backend.repository;

import com.auction.backend.entity.BudgetTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetTransactionRepository extends JpaRepository<BudgetTransaction,Long> {

    List<BudgetTransaction> findByTournamentIdAndMemberIdOrderByCreatedAtDesc(
            Long tournamentId,
            Long memberId
    );
}

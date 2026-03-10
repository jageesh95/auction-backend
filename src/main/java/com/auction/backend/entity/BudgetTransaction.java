package com.auction.backend.entity;

import com.auction.backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="budget_transaction")
public class BudgetTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tournamentId;

    private Long memberId;

    private Long auctionPlayerId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Double balanceBefore;

    private Double balanceAfter;

    private String description;

    private LocalDateTime createdAt;
}

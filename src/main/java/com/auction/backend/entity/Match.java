package com.auction.backend.entity;
import com.auction.backend.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id", nullable = false)
    private Members teamA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id", nullable = false)
    private Members teamB;

    private Integer scoreA = 0;
    private Integer scoreB = 0;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;
    // SCHEDULED, COMPLETED

    private LocalDateTime matchDate;


}

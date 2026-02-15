package com.auction.backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "standings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tournament_id", "team_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Standing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Members team;

    private int played = 0;

    private int win = 0;

    private int draw = 0;

    private int loss = 0;

    private int goalsFor = 0;

    private int goalsAgainst = 0;

    private int goalDifference = 0;

    private int points = 0;
}

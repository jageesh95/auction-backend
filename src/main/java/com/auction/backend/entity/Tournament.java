package com.auction.backend.entity;

import com.auction.backend.enums.TournamentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status;
    // UPCOMING, ONGOING, COMPLETED

    private LocalDate startDate;

    private LocalDate endDate;

    // One tournament → many matches
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches;

    // One tournament → many standings
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Standing> standings;


}

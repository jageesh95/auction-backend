package com.auction.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String teamName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

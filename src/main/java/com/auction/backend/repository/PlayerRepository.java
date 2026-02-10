package com.auction.backend.repository;

import com.auction.backend.entity.Players;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Players,Long> {
}

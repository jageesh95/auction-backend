package com.auction.backend.repository;

import com.auction.backend.entity.AuctionPlayer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionPlayerRepository extends JpaRepository<AuctionPlayer,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ap FROM AuctionPlayer ap WHERE ap.id = :id")
    AuctionPlayer findByIdForUpdate(@Param("id") Long id);

}

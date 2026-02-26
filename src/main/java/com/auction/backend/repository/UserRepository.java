package com.auction.backend.repository;

import com.auction.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailOrMobile(String email, String mobile);
    Optional<User> findByEmail(String email);

    Optional<User> findByMobile(String mobile);
}

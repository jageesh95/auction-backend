package com.auction.backend.auth.service;

import com.auction.backend.auth.dto.AuthResponse;
import com.auction.backend.auth.dto.LoginRequest;
import com.auction.backend.entity.User;
import com.auction.backend.repository.UserRepository;
import com.auction.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailOrMobile(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

}

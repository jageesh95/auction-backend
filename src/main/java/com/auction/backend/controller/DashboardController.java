package com.auction.backend.controller;

import com.auction.backend.dto.DashboardResponse;
import com.auction.backend.security.JwtService;
import com.auction.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final JwtService jwtService;
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(Authentication authentication){

        Long id= Long.valueOf(authentication.getName());
        System.out.println("email: "+id);

        DashboardResponse response =
                dashboardService.getDashboard(id);

        return ResponseEntity.ok(response);
    }

}

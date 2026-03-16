package com.auction.backend.controller;

import com.auction.backend.dto.AdminDashboardResponse;
import com.auction.backend.dto.MemberDashboardResponse;
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

    @GetMapping("/dashboard/member")
    public ResponseEntity<MemberDashboardResponse> getMemberDashboard(Authentication authentication){

        Long id= Long.valueOf(authentication.getName());
        MemberDashboardResponse response =
                dashboardService.getMemberDashboard(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/admin")
    public ResponseEntity<AdminDashboardResponse> getAdminDashBoard(Authentication authentication){
         Long id=Long.valueOf(authentication.getName());
         AdminDashboardResponse response=dashboardService.getAdminDashboard(id);
         return ResponseEntity.ok(response);
    }


}

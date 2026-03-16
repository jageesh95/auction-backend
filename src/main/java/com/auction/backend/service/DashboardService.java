package com.auction.backend.service;

import com.auction.backend.dto.AdminDashboardResponse;
import com.auction.backend.dto.MemberDashboardResponse;

public interface DashboardService {

    MemberDashboardResponse getMemberDashboard(Long id);
    AdminDashboardResponse getAdminDashboard(Long id);



}

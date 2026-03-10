package com.auction.backend.auth.dto;

import lombok.Data;

@Data
public class UserResponse {

    private String token;
    private Long id;
    private String name;
    private String role;
}

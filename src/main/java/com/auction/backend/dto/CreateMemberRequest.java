package com.auction.backend.dto;

import lombok.Data;

@Data
public class CreateMemberRequest {

    private String name;
    private String teamName;
    private String email;
    private String mobile;
    private String password;
}

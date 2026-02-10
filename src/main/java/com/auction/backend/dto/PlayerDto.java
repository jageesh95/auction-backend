package com.auction.backend.dto;

import lombok.Data;

@Data
public class PlayerDto {

    private Long id;
    private String name;
    private String position;
    private Double basePrice;
    private String imageUrl;
}

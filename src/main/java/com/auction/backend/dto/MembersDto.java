package com.auction.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembersDto {
    private Long id;
    private String name;
    private String email;
    private String teamName;

}

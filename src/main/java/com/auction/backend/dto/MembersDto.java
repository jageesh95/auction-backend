package com.auction.backend.dto;

import com.auction.backend.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembersDto {
    private Long id;
    private String name;
    private String email;
    private String teamName;
    private Role role;
    private String phoneNumber;

}

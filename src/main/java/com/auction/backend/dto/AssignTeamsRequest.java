package com.auction.backend.dto;

import lombok.Data;

import java.util.List;
@Data
public class AssignTeamsRequest {

    private List<Long> teamIds;
}

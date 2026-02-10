package com.auction.backend.service;

import com.auction.backend.dto.PlayerDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlayersService
{
    PlayerDto create(PlayerDto dto, MultipartFile image) throws IOException;

    List<PlayerDto> getAll();

    PlayerDto getById(Long id);

    PlayerDto update(Long id, PlayerDto dto, MultipartFile image) throws IOException;

    void delete(Long id);
}

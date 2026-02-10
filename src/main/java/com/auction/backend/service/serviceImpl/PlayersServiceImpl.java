package com.auction.backend.service.serviceImpl;

import com.auction.backend.dto.PlayerDto;
import com.auction.backend.entity.Players;
import com.auction.backend.repository.PlayerRepository;
import com.auction.backend.service.PlayersService;
import com.auction.backend.utill.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayersServiceImpl implements PlayersService {
    private final PlayerRepository repo;
    private final ImageService imageService;

    @Override
    public PlayerDto create(PlayerDto dto, MultipartFile image) throws IOException {

        String imageUrl = imageService.upload(image);

        Players player = mapToEntity(dto);
        player.setImageUrl(imageUrl);

        return mapToDto(repo.save(player));
    }

    @Override
    public List<PlayerDto> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public PlayerDto getById(Long id) {
        return mapToDto(repo.findById(id).orElseThrow());
    }

    @Override
    public PlayerDto update(Long id, PlayerDto dto, MultipartFile image) throws IOException {

        Players player = repo.findById(id).orElseThrow();

        player.setName(dto.getName());
        player.setPosition(dto.getPosition());
        player.setBasePrice(dto.getBasePrice());

        if (image != null && !image.isEmpty()) {
            player.setImageUrl(imageService.upload(image));
        }

        return mapToDto(repo.save(player));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private Players mapToEntity(PlayerDto dto) {
        return new Players(
                dto.getId(),
                dto.getName(),
                dto.getPosition(),
                dto.getBasePrice(),
                dto.getImageUrl()
        );
    }

    private PlayerDto mapToDto(Players p) {
        PlayerDto dto = new PlayerDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPosition(p.getPosition());
        dto.setBasePrice(p.getBasePrice());
        dto.setImageUrl(p.getImageUrl());
        return dto;
    }
}

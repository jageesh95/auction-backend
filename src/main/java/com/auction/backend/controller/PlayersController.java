package com.auction.backend.controller;

import com.auction.backend.dto.PlayerDto;
import com.auction.backend.service.PlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayersController {

    private final PlayersService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PlayerDto create(
            @RequestParam String name,
            @RequestParam String position,
            @RequestParam Integer age,
            @RequestParam Double basePrice,
            @RequestParam MultipartFile image
    ) throws IOException {

        PlayerDto dto = new PlayerDto();
        dto.setName(name);
        dto.setPosition(position);
        dto.setBasePrice(basePrice);

        return service.create(dto, image);
    }

    @GetMapping
    public List<PlayerDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PlayerDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PlayerDto update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String position,
            @RequestParam Integer age,
            @RequestParam Double basePrice,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        PlayerDto dto = new PlayerDto();
        dto.setName(name);
        dto.setPosition(position);
        dto.setBasePrice(basePrice);

        return service.update(id, dto, image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

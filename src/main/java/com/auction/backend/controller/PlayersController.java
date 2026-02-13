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

    @PostMapping()
    public PlayerDto create(@RequestBody PlayerDto dto) {
        return service.create(dto);
    }

    @PostMapping("/image/{id}")
    public PlayerDto updloadImage(@PathVariable final  Long id, @RequestParam final MultipartFile file) throws IOException {
        System.out.println("id - "+id);
        System.out.println("file"+ file.getBytes());
        System.out.println("file"+ file.getName());
        PlayerDto dto= service.update(id,file);
        return  dto;
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

        return service.update(id ,image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

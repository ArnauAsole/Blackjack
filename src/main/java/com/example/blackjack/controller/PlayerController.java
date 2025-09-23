package com.example.blackjack.controller;

import com.example.blackjack.dto.request.CreatePlayerRequest;
import com.example.blackjack.dto.request.RenamePlayerRequest;
import com.example.blackjack.dto.response.PlayerResponse;
import com.example.blackjack.mapper.PlayerMapper;
import com.example.blackjack.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PlayerController {

    private final PlayerService service;
    private final PlayerMapper mapper;

    @Operation(summary = "Crear nou jugador")
    @PostMapping("/player/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlayerResponse> create(@Valid @RequestBody CreatePlayerRequest req) {
        return service.create(req.name()).map(mapper::toResponse);
    }

    @Operation(summary = "Canviar nom del jugador")
    @PutMapping("/player/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlayerResponse> rename(
            @PathVariable("id") Long id,
            @Valid @RequestBody RenamePlayerRequest req
    ) {
        return service.rename(id, req.name()).map(mapper::toResponse);
    }

    @Operation(summary = "Obtenir rànquing de jugadors", description = "Jugadors ordenats per victories i derrotes")
    @GetMapping("/ranking")
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlayerResponse> ranking() {
        return service.listAll().map(mapper::toResponse);
    }
}

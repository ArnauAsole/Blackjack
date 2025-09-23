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
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService service;
    private final PlayerMapper mapper;

    @Operation(summary = "Crear un nuevo jugador")
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlayerResponse> create(@Valid @RequestBody CreatePlayerRequest req) {
        return service.create(req.name()).map(mapper::toResponse);
    }

    @Operation(summary = "Cambiar nombre del jugador")
    @PutMapping("/{playerId}")
    public Mono<PlayerResponse> rename(@PathVariable Long playerId,
                                       @Valid @RequestBody RenamePlayerRequest req) {
        return service.rename(playerId, req.name()).map(mapper::toResponse);
    }

    @Operation(summary = "Listado de jugadores")
    @GetMapping("/list")
    public Flux<PlayerResponse> listAll() {
        return service.listAll().map(mapper::toResponse);
    }
}

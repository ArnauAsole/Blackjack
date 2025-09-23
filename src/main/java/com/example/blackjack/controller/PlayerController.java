package com.example.blackjack.controller;

import com.example.blackjack.domain.player.Player;
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

import java.util.UUID;

@RestController @RequiredArgsConstructor
public class PlayerController {
    private final PlayerService service;

    @Operation(summary = "Crear un nuevo jugador")
    @PostMapping("/player/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlayerResponse> create(@Valid @RequestBody CreatePlayerRequest req) {
        return service.create(req.name()).map(PlayerMapper::toResponse);
    }

    @Operation(summary = "Canviar nom del jugador")
    @PutMapping("/player/{playerId}")
    public Mono<Player> rename(@PathVariable UUID playerId, @Valid @RequestBody RenamePlayerRequest req){
        return service.rename(playerId, req.name());
    }

    @GetMapping("/player/list")
    public Flux<Player> listAll()  {
        return service.listAll();
    }
}

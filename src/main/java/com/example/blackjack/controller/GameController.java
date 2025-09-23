// src/main/java/com/example/blackjack/controller/GameController.java
package com.example.blackjack.controller;

import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.dto.request.PlayRequest;
import com.example.blackjack.dto.response.GameResponse;
import com.example.blackjack.mapper.GameMapper;
import com.example.blackjack.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService service;

    @Operation(summary = "Crear nova partida de Blackjack")
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> create(@Valid @RequestBody CreateGameRequest req) {
        return service.createGame(req).map(GameMapper::toResponse);
    }

    @Operation(summary = "Obtenir detalls d'una partida")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GameResponse> get(@PathVariable String id) {
        return service.get(id).map(GameMapper::toResponse);
    }

    @Operation(summary = "Fer una jugada (HIT/STAND)")
    @PostMapping("/{id}/play")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GameResponse> play(@PathVariable String id, @Valid @RequestBody PlayRequest req) {
        return service.play(id, req.action()).map(GameMapper::toResponse);
    }

    @Operation(summary = "Eliminar partida")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }
}

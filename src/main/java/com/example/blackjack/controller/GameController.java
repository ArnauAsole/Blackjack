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

<<<<<<< HEAD
    /**
     * Crear partida
     */
=======
    @Operation(summary = "Crear nova partida de Blackjack")
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> create(@Valid @RequestBody CreateGameRequest req) {
        return service.createGame(req).map(GameMapper::toResponse);
    }

<<<<<<< HEAD
    /**
     * Obtenir detalls d’una partida
     */
=======
    @Operation(summary = "Obtenir detalls d'una partida")
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GameResponse> get(@PathVariable String id) {
        return service.get(id).map(GameMapper::toResponse);
    }

<<<<<<< HEAD
    /**
     * Fer jugada (HIT o STAND)
     */
=======
    @Operation(summary = "Fer una jugada (HIT/STAND)")
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
    @PostMapping("/{id}/play")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GameResponse> play(@PathVariable String id, @Valid @RequestBody PlayRequest req) {
        return service.play(id, req.action()).map(GameMapper::toResponse);
    }

<<<<<<< HEAD
    /**
     * Eliminar partida
     */
=======
    @Operation(summary = "Eliminar partida")
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }
}

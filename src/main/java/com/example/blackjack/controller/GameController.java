package com.example.blackjack.controller;

import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.dto.request.PlayRequest;
import com.example.blackjack.dto.response.GameResponse;
import com.example.blackjack.service.GameService;
import com.example.blackjack.mapper.GameMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * Crear partida
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> newGame(@Valid @RequestBody CreateGameRequest req) {
        return gameService.createGame(req)
                .map(GameMapper::toResponse);
    }

    /**
     * Obtenir detalls d’una partida
     */
    @GetMapping("/{id}")
    public Mono<GameResponse> getGame(@PathVariable String id) {
        return gameService.get(id)
                .map(GameMapper::toResponse);
    }

    /**
     * Fer jugada (HIT o STAND)
     */
    @PostMapping("/{id}/play")
    public Mono<GameResponse> play(@PathVariable String id,
                                   @Valid @RequestBody PlayRequest req) {
        return gameService.play(id, req.action())
                .map(GameMapper::toResponse);
    }

    /**
     * Eliminar partida
     */
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return gameService.delete(id);
    }
}

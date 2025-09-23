package com.example.blackjack.controller;

import com.example.blackjack.domain.game.*;
import com.example.blackjack.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebFluxTest(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    WebTestClient web;

    @MockBean
    GameService svc;

    @Test
    void newGame_returns201() {
        Game game = Game.builder()
                .id("game-1")
                .playerId(1L)                            // <--- Long, no UUID
                .playerName("Bob")
                .deck(new ArrayList<>())
                .playerHand(new ArrayList<>(List.of(new Card(Suit.HEARTS, Rank.SEVEN))))
                .dealerHand(new ArrayList<>(List.of(new Card(Suit.SPADES, Rank.SIX))))
                .status(GameStatus.IN_PROGRESS)
                .bet(5)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Mockito.when(svc.createGame(Mockito.any())).thenReturn(Mono.just(game));

        web.post().uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"playerName\":\"Bob\",\"bet\":5}")
                .exchange()
                .expectStatus().isCreated();
    }
}

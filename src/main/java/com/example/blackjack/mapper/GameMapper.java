// src/main/java/com/example/blackjack/mapper/GameMapper.java
package com.example.blackjack.mapper;

import com.example.blackjack.domain.game.Card;
import com.example.blackjack.domain.game.Game;
import com.example.blackjack.domain.game.GameStatus;
import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.dto.response.GameResponse;
import com.example.blackjack.service.BlackjackEngine;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class GameMapper {

    private GameMapper() {
    }

    /**
<<<<<<< HEAD
     * Calcula la puntuació tenint l'As com 1 o 11 segons convingui
     */
    public static int score(List<Card> hand) {
        int total = 0, aces = 0;
        for (Card c : hand) {
            total += c.rank().value;
            if (c.rank() == Rank.ACE) aces++;
        }
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    /**
     * Crea un Game inicial a partir del request + playerId
     */
    public static Game toNewGame(CreateGameRequest req, Long playerId) {
        List<Card> deck = newShuffledDeck();
        List<Card> ph = new ArrayList<>(), dh = new ArrayList<>();
        ph.add(deck.remove(0));
        ph.add(deck.remove(0));
        dh.add(deck.remove(0));
        dh.add(deck.remove(0));
=======
     * Crea un Game inicial: baraja, reparte 2+2 y marca IN_PROGRESS.
     */
    public static Game toNewGame(CreateGameRequest req, Long playerId) {
        List<Card> deck = new ArrayList<>(BlackjackEngine.newShuffledDeck());

        List<Card> player = new ArrayList<>();
        List<Card> dealer = new ArrayList<>();

        // repartir 2 + 2
        player.add(deck.remove(0));
        dealer.add(deck.remove(0));
        player.add(deck.remove(0));
        dealer.add(deck.remove(0));
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)

        return Game.builder()
                .playerId(playerId)
                .playerName(req.playerName())
                .deck(deck)
                .playerHand(player)
                .dealerHand(dealer)
                .status(GameStatus.IN_PROGRESS)
                .bet(req.bet() == null ? 0 : Math.max(0, req.bet()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    /**
<<<<<<< HEAD
     * Converteix el model Game a la resposta pública GameResponse
=======
     * Game -> GameResponse calculando scores con el motor.
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
     */
    public static GameResponse toResponse(Game g) {
        int pScore = BlackjackEngine.score(g.getPlayerHand());
        int dScore = BlackjackEngine.score(g.getDealerHand());
        return new GameResponse(
                g.getId(),
                g.getPlayerName(),
                pScore,
                dScore,
                g.getPlayerHand(),
                g.getDealerHand(),
                g.getStatus(),
                g.getBet(),
                g.getUpdatedAt()
        );
    }
}

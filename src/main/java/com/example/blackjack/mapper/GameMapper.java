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
     * Crea un Game inicial: baraja, reparte 2+2 y marca IN_PROGRESS.
     */
    public static Game toNewGame(CreateGameRequest req, Long playerId) {
        List<Card> deck = new ArrayList<>(BlackjackEngine.newShuffledDeck());

        List<Card> player = new ArrayList<>();
        List<Card> dealer = new ArrayList<>();

        // reparte 2 + 2 del tope del mazo
        player.add(deck.remove(0));
        dealer.add(deck.remove(0));
        player.add(deck.remove(0));
        dealer.add(deck.remove(0));

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
     * Mapea Game -> GameResponse recalculando puntuaciones con el motor.
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

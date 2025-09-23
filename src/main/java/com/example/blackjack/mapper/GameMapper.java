package com.example.blackjack.mapper;

import com.example.blackjack.domain.game.*;
import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.dto.response.GameResponse;

import java.time.Instant;
import java.security.SecureRandom;
import java.util.*;

public class GameMapper {

    private static final SecureRandom RND = new SecureRandom();

    public static List<Card> newShuffledDeck() {
        List<Card> d = new ArrayList<>();
        for (Suit s : Suit.values()) for (Rank r : Rank.values()) d.add(new Card(s, r));
        Collections.shuffle(d, RND);
        return d;
    }

    /** Calcula la puntuació tenint l'As com 1 o 11 segons convingui */
    public static int score(List<Card> hand) {
        int total = 0, aces = 0;
        for (Card c : hand) { total += c.rank().value; if (c.rank()==Rank.ACE) aces++; }
        while (total > 21 && aces > 0) { total -= 10; aces--; }
        return total;
    }

    /** Crea un Game inicial a partir del request + playerId */
    public static Game toNewGame(CreateGameRequest req, UUID playerId) {
        List<Card> deck = newShuffledDeck();
        List<Card> ph = new ArrayList<>(), dh = new ArrayList<>();
        ph.add(deck.remove(0)); ph.add(deck.remove(0));
        dh.add(deck.remove(0)); dh.add(deck.remove(0));

        return Game.builder()
                .playerId(playerId)
                .playerName(req.playerName())
                .deck(deck)
                .playerHand(ph)
                .dealerHand(dh)
                .status(GameStatus.IN_PROGRESS)
                .bet(req.bet() == null ? 0 : req.bet())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    /** Converteix el model Game a la resposta pública GameResponse */
    public static GameResponse toResponse(Game g) {
        return new GameResponse(
                g.getId(),
                g.getPlayerName(),
                score(g.getPlayerHand()),
                score(g.getDealerHand()),
                g.getPlayerHand(),
                g.getDealerHand(),
                g.getStatus(),
                g.getBet(),
                g.getUpdatedAt()
        );
    }
}

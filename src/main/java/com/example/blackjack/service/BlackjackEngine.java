// src/main/java/com/example/blackjack/service/BlackjackEngine.java
package com.example.blackjack.service;

import com.example.blackjack.domain.game.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackjackEngine {
    private static final SecureRandom RND = new SecureRandom();

    /**
     * Baraja nueva (52) y mezclada.
     */
    public static List<Card> newShuffledDeck() {
        List<Card> d = new ArrayList<>();
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                d.add(new Card(s, r));
            }
        }
        Collections.shuffle(d, RND);
        return d;
    }

    /**
     * Puntuación (AS vale 11 o 1 según convenga).
     */
    public static int score(List<Card> hand) {
        int total = 0;
        int aces = 0;
        for (Card c : hand) {
            total += c.rank().value;
            if (c.rank() == Rank.ACE) aces++;
        }
        while (total > 21 && aces > 0) {
<<<<<<< HEAD
            total -= 10;
            aces--;
        } // baixa ACE d'11 a 1
        return total;
    }

    public static GameStatus decide(List<Card> player, List<Card> dealer) {
        int ps = score(player), ds = score(dealer);
        if (ps > 21) return GameStatus.PLAYER_BUST;
        if (ds > 21) return GameStatus.DEALER_BUST;
        if (ps > ds) return GameStatus.PLAYER_WIN;
        if (ps < ds) return GameStatus.DEALER_WIN;
        return GameStatus.PUSH;
=======
            total -= 10; // bajar un AS de 11 a 1
            aces--;
        }
        return total;
    }

    /**
     * Política del dealer: roba hasta llegar a 18 o más.
     */
    public static void dealerPlay(List<Card> deck, List<Card> dealerHand) {
        while (score(dealerHand) < 18 && !deck.isEmpty()) {
            dealerHand.add(deck.remove(0));
        }
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
    }

    /**
     * Resultado final según puntuación actual.
     */
    public static GameStatus decide(List<Card> player, List<Card> dealer) {
        int ps = score(player), ds = score(dealer);
        if (ps > 21) return GameStatus.PLAYER_BUST;
        if (ds > 21) return GameStatus.DEALER_BUST;
        if (ps > ds) return GameStatus.PLAYER_WIN;
        if (ps < ds) return GameStatus.DEALER_WIN;
        return GameStatus.PUSH;
    }
}

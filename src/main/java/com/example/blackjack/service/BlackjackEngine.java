package com.example.blackjack.service;

import com.example.blackjack.domain.game.*;

import java.security.SecureRandom;
import java.util.*;

public class BlackjackEngine {
    private static final SecureRandom RND = new SecureRandom();

    public static List<Card> newShuffledDeck() {
        List<Card> d = new ArrayList<>();
        for (Suit s : Suit.values()) for (Rank r : Rank.values()) d.add(new Card(s, r));
        Collections.shuffle(d, RND);
        return d;
    }

    public static int score(List<Card> hand) {
        int total = 0;
        int aces = 0;
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

    public static GameStatus decide(List<Card> player, List<Card> dealer) {
        int ps = score(player), ds = score(dealer);
        if (ps > 21) return GameStatus.PLAYER_BUST;
        if (ds > 21) return GameStatus.DEALER_BUST;
        if (ps > ds) return GameStatus.PLAYER_WIN;
        if (ps < ds) return GameStatus.DEALER_WIN;
        return GameStatus.PUSH;
    }

    public static void dealerPlay(List<Card> deck, List<Card> dealerHand) {
        while (score(dealerHand) < 18) dealerHand.add(deck.remove(0));
    }
}

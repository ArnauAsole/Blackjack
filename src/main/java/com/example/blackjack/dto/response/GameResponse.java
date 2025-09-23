package com.example.blackjack.dto.response;

import com.example.blackjack.domain.game.Card;
import com.example.blackjack.domain.game.GameStatus;

import java.time.Instant;
import java.util.List;

public record GameResponse(
        String id,
        String playerName,
        int playerScore,
        int dealerScore,
        List<Card> playerHand,
        List<Card> dealerHand,
        GameStatus status,
        Integer bet,
        Instant updatedAt
) { }

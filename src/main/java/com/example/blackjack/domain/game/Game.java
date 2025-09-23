package com.example.blackjack.domain.game;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Document("game")
public class Game {
    @Id
    private String id;

    private UUID playerId;
    private String playerName;

    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    private GameStatus status;
    private Integer bet;

    private Instant createdAt;
    private Instant updatedAt;
}

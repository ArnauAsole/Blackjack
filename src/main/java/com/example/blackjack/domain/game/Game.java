// src/main/java/com/example/blackjack/domain/game/Game.java
package com.example.blackjack.domain.game;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Document("games")
public class Game {

    @Id
    private String id;


    private Long playerId;
    private String playerName;

    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    private GameStatus status;

    private Integer bet;

    private Instant createdAt;
    private Instant updatedAt;
}

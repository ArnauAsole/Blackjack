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

    // relación con jugador (MySQL)
    private Long playerId;      // lo extraemos del Player real
    private String playerName;  // para mostrar en el response

    // mazo y manos
    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    // estado de la partida
    private GameStatus status;

    // apuesta (opcional)
    private Integer bet;

    // auditoría
    private Instant createdAt;
    private Instant updatedAt;
}

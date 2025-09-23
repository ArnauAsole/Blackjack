// src/main/java/com/example/blackjack/domain/game/GameStatus.java
package com.example.blackjack.domain.game;

public enum GameStatus {
    IN_PROGRESS,   // partida abierta
    PLAYER_BUST,   // jugador se pasa (>21)
    DEALER_BUST,   // dealer se pasa (>21)
    PLAYER_WIN,
    DEALER_WIN,
    PUSH           // empate
}

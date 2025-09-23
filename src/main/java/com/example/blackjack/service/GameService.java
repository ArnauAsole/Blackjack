package com.example.blackjack.service;

import com.example.blackjack.domain.game.Game;
import com.example.blackjack.domain.game.GameStatus;
import com.example.blackjack.domain.player.Player;
import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.service.BlackjackEngine;
import com.example.blackjack.mapper.GameMapper;
import com.example.blackjack.repo.GameRepository;
import com.example.blackjack.repo.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository games;
    private final PlayerRepository players;

    /** Crea una nova partida a partir del DTO (mapper + jugador a MySQL) */
    public Mono<Game> createGame(CreateGameRequest req) {
        UUID playerId = UUID.randomUUID();

        Player player = Player.builder()

                .name(req.playerName())
                .wins(0)
                .losses(0)
                .createdAt(Instant.now())
                .build();

        Game game = GameMapper.toNewGame(req, playerId);

        // Guardem jugador (MySQL) i partida (Mongo) de forma reactiva
        return players.save(player).then(games.save(game));
    }

    /** Obtenir partida per id */
    public Mono<Game> get(String id) {
        return games.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found")));
    }

    /** Esborrar partida */
    public Mono<Void> delete(String id) {
        return games.deleteById(id);
    }

    /** Fer jugada: HIT o STAND */
    public Mono<Game> play(String id, String action) {
        return get(id).flatMap(g -> {
            if (g.getStatus() != GameStatus.IN_PROGRESS)
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game ended"));

            List<com.example.blackjack.domain.game.Card> deck = g.getDeck();

            switch (action.toUpperCase()) {
                case "HIT" -> {
                    g.getPlayerHand().add(deck.remove(0));
                    if (BlackjackEngine.score(g.getPlayerHand()) > 21) {
                        g.setStatus(GameStatus.PLAYER_BUST);
                        return endAndUpdateRanking(g, false);
                    }
                    g.setUpdatedAt(Instant.now());
                    return games.save(g);
                }
                case "STAND" -> {
                    // Dealer roba fins a 18 o més
                    BlackjackEngine.dealerPlay(deck, g.getDealerHand());
                    GameStatus st = BlackjackEngine.decide(g.getPlayerHand(), g.getDealerHand());
                    g.setStatus(st);
                    boolean win = st == GameStatus.PLAYER_WIN || st == GameStatus.DEALER_BUST;
                    return endAndUpdateRanking(g, win);
                }
                default -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action"));
                }
            }
        });
    }

    /** Actualitza rànquing i desa partida */
    private Mono<Game> endAndUpdateRanking(Game g, boolean playerWin) {
        g.setUpdatedAt(Instant.now());

        return players.findById(g.getPlayerId())
                .switchIfEmpty(Mono.just(Player.builder()
                        .id(g.getPlayerId())
                        .name(g.getPlayerName())
                        .wins(0)
                        .losses(0)
                        .createdAt(Instant.now())
                        .build()))
                .flatMap(p -> {
                    if (playerWin) p.setWins(p.getWins() + 1);
                    else p.setLosses(p.getLosses() + 1);
                    return players.save(p);
                })
                .then(games.save(g));
    }

    /** Rànquing de jugadors (MySQL) */
    public Flux<Player> ranking() {
        return players.findAllByOrderByWinsDescLossesAsc();
    }
}

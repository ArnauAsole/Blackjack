package com.example.blackjack.service;

import com.example.blackjack.domain.game.Game;
import com.example.blackjack.domain.game.GameStatus;
import com.example.blackjack.domain.player.Player;
import com.example.blackjack.dto.request.CreateGameRequest;
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

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository games;
    private final PlayerRepository players;

    /**
     * Crea partida + jugador (MySQL)
     */
    public Mono<Game> createGame(CreateGameRequest req) {
        String name = req.playerName() == null ? "" : req.playerName().trim();
        if (name.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del jugador es obligatorio"));
        }

        Player player = Player.builder()
                .name(name)
                .wins(0)
                .losses(0)
                .createdAt(Instant.now())   // o dejar que MySQL ponga el DEFAULT
                .build();

        // 1) Guardar jugador -> MySQL genera id (Long)
        return players.save(player)
                // 2) Con ese id Long, crear y guardar la partida en Mongo
                .flatMap(savedPlayer -> {
                    Game game = GameMapper.toNewGame(req, savedPlayer.getId()); // <- Long
                    return games.save(game);
                });
    }

    /**
     * Obtener partida por id (Mongo)
     */
    public Mono<Game> get(String id) {
        return games.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found")));
    }

    /**
     * Borrar partida
     */
    public Mono<Void> delete(String id) {
        return games.deleteById(id);
    }

    /**
     * Jugar: HIT o STAND
     */
    public Mono<Game> play(String id, String action) {
        return get(id).flatMap(g -> {
            if (g.getStatus() != GameStatus.IN_PROGRESS) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game ended"));
            }

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

    /**
     * Actualiza ranking y guarda partida
     */
    private Mono<Game> endAndUpdateRanking(Game g, boolean playerWin) {
        g.setUpdatedAt(Instant.now());

        return players.findById(g.getPlayerId())  // <-- Long
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

    /**
     * Ranking de jugadores (MySQL)
     */
    public Flux<Player> ranking() {
        return players.findAllByOrderByWinsDescLossesAsc();
    }
}

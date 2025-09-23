// src/main/java/com/example/blackjack/service/GameService.java
package com.example.blackjack.service;

import com.example.blackjack.domain.game.Card;
import com.example.blackjack.domain.game.Game;
import com.example.blackjack.domain.game.GameStatus;
import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.repo.GameRepository;
import com.example.blackjack.repo.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static com.example.blackjack.mapper.GameMapper.toNewGame;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository games;     // Mongo
    private final PlayerRepository players; // MySQL (R2DBC)

    /**
<<<<<<< HEAD
     * Crea partida + jugador (MySQL)
=======
     * Crea partida validando que el jugador existe.
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
     */
    public Mono<Game> createGame(CreateGameRequest req) {
        String clean = req.playerName() == null ? "" : req.playerName().trim();
        if (clean.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nom del jugador és obligatori"));
        }

        return players.findByName(clean)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Jugador no registrat")))
                .flatMap(p -> games.save(toNewGame(req, p.getId())));
    }

    /**
     * Obtener partida por id.
     */
    public Mono<Game> get(String id) {
        return games.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida no trobada")));
    }

    /**
     * Realiza jugada: action = HIT o STAND.
     */
    public Mono<Game> play(String id, String actionRaw) {
        final String action = actionRaw == null ? "" : actionRaw.trim().toUpperCase();
        if (action.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cal indicar l'acció (HIT/STAND)"));
        }

        return games.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida no trobada")))
                .flatMap(g -> switch (action) {
                    case "HIT" -> hit(g);
                    case "STAND" -> stand(g);
                    default ->
                            Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acció invàlida (HIT/STAND)"));
                });
    }

    /**
<<<<<<< HEAD
     * Obtener partida por id (Mongo)
     */
    public Mono<Game> get(String id) {
        return games.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found")));
    }

    /**
     * Borrar partida
=======
     * Eliminar partida (idempotente).
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
     */
    public Mono<Void> delete(String id) {
        return games.deleteById(id);
    }

<<<<<<< HEAD
    /**
     * Jugar: HIT o STAND
     */
    public Mono<Game> play(String id, String action) {
        return get(id).flatMap(g -> {
            if (g.getStatus() != GameStatus.IN_PROGRESS) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game ended"));
            }
=======
    // ----------------- Helpers de jugada -----------------
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)

    private Mono<Game> hit(Game g) {
        if (g.getStatus() != GameStatus.IN_PROGRESS) {
            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "La partida ja està tancada"));
        }

        List<Card> deck = g.getDeck();
        if (deck.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "No queden cartes al mazo"));
        }
        g.getPlayerHand().add(deck.remove(0)); // roba 1
        int pScore = BlackjackEngine.score(g.getPlayerHand());

        if (pScore > 21) {
            g.setStatus(GameStatus.PLAYER_BUST);
            g.setUpdatedAt(Instant.now());
            return endAndUpdateRanking(g, /*playerWins=*/false);
        }

        g.setUpdatedAt(Instant.now());
        return games.save(g);
    }

<<<<<<< HEAD
    /**
     * Actualiza ranking y guarda partida
     */
    private Mono<Game> endAndUpdateRanking(Game g, boolean playerWin) {
=======
    private Mono<Game> stand(Game g) {
        if (g.getStatus() != GameStatus.IN_PROGRESS) {
            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "La partida ja està tancada"));
        }

        // turno del dealer
        BlackjackEngine.dealerPlay(g.getDeck(), g.getDealerHand());

        GameStatus finalStatus = BlackjackEngine.decide(g.getPlayerHand(), g.getDealerHand());
        g.setStatus(finalStatus);
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
        g.setUpdatedAt(Instant.now());

        if (finalStatus == GameStatus.PUSH) {
            // empate: no tocar ranking
            return games.save(g);
        }
        boolean playerWins = (finalStatus == GameStatus.DEALER_BUST || finalStatus == GameStatus.PLAYER_WIN);
        return endAndUpdateRanking(g, playerWins);
    }

    /**
     * Cierra la partida y actualiza ranking en MySQL.
     */
    private Mono<Game> endAndUpdateRanking(Game g, boolean playerWins) {
        return players.findById(g.getPlayerId())
                .flatMap(p -> {
                    if (playerWins) p.setWins(p.getWins() + 1);
                    else p.setLosses(p.getLosses() + 1);
                    return players.save(p);
                })
                .onErrorResume(ex -> Mono.empty()) // no bloquear si falla ranking
                .then(games.save(g));
    }
<<<<<<< HEAD

    /**
     * Ranking de jugadores (MySQL)
     */
    public Flux<Player> ranking() {
        return players.findAllByOrderByWinsDescLossesAsc();
    }
=======
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
}

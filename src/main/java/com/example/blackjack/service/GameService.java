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


    public Mono<Game> createGame(CreateGameRequest req) {
        String clean = req.playerName() == null ? "" : req.playerName().trim();
        if (clean.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nom del jugador és obligatori"));
        }

        return players.findByName(clean)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Jugador no registrat")))
                .flatMap(p -> games.save(toNewGame(req, p.getId())));
    }


    public Mono<Game> get(String id) {
        return games.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida no trobada")));
    }


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


    public Mono<Void> delete(String id) {
        return games.deleteById(id);
    }


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

    private Mono<Game> stand(Game g) {
        if (g.getStatus() != GameStatus.IN_PROGRESS) {
            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "La partida ja està tancada"));
        }

        // turno del dealer
        BlackjackEngine.dealerPlay(g.getDeck(), g.getDealerHand());
        int p = BlackjackEngine.score(g.getPlayerHand());
        int d = BlackjackEngine.score(g.getDealerHand());

        GameStatus finalStatus;
        if (d > 21) finalStatus = GameStatus.DEALER_BUST;       // gana jugador
        else if (p > d) finalStatus = GameStatus.PLAYER_WIN;
        else if (p < d) finalStatus = GameStatus.DEALER_WIN;
        else finalStatus = GameStatus.PUSH;

        g.setStatus(finalStatus);
        g.setUpdatedAt(Instant.now());

        boolean playerWins = (finalStatus == GameStatus.DEALER_BUST || finalStatus == GameStatus.PLAYER_WIN);
        if (finalStatus == GameStatus.PUSH) {
            // empate: no tocar ranking
            return games.save(g);
        }
        return endAndUpdateRanking(g, playerWins);
    }


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
}

package com.example.blackjack.service;

import com.example.blackjack.domain.player.Player;
import com.example.blackjack.domain.game.Game;
import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.repo.GameRepository;
import com.example.blackjack.repo.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GameServiceTest {

    @Test
    void createGame_ok() {
        // Repos mockeados
        GameRepository gRepo = Mockito.mock(GameRepository.class);
        PlayerRepository pRepo = Mockito.mock(PlayerRepository.class);

        // Al guardar un Player, simulamos que MySQL le asigna un ID autoincremental
        Mockito.when(pRepo.save(Mockito.any(Player.class)))
                .thenAnswer(inv -> {
                    Player p = inv.getArgument(0, Player.class);
                    p.setId(1L);                 // simulamos AUTO_INCREMENT
                    return Mono.just(p);
                });

        // Al guardar un Game, devolvemos el mismo objeto
        Mockito.when(gRepo.save(Mockito.any(Game.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // Servicio bajo prueba
        GameService svc = new GameService(gRepo, pRepo);

        // DTO de entrada
        CreateGameRequest req = new CreateGameRequest("Alice", 100);

        // Verificación
        StepVerifier.create(svc.createGame(req))
                .expectNextMatches(g ->
                        g.getPlayerName().equals("Alice")
                                && g.getBet() == 100
                                && g.getPlayerId() != null
                                && g.getPlayerId().equals(1L)      // ahora es Long, no UUID
                )
                .verifyComplete();
    }
}

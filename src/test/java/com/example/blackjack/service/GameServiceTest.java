package com.example.blackjack.service;

import com.example.blackjack.domain.game.Game;
import com.example.blackjack.dto.request.CreateGameRequest;
import com.example.blackjack.repo.GameRepository;
import com.example.blackjack.repo.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

class GameServiceTest {

    @Test
    void createGame_ok() {
        // mocks dels repos
        GameRepository gRepo = Mockito.mock(GameRepository.class);
        PlayerRepository pRepo = Mockito.mock(PlayerRepository.class);

        // quan es guardi un player → retorna’l tal qual
        Mockito.when(pRepo.save(Mockito.any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // quan es guardi un game → retorna’l tal qual
        Mockito.when(gRepo.save(Mockito.any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // servei amb els repos mockejats
        GameService svc = new GameService(gRepo, pRepo);

        // 👉 creem el DTO (record en Java 17+/21)
        CreateGameRequest req = new CreateGameRequest("Alice", 100);

        StepVerifier.create(svc.createGame(req))
                .expectNextMatches(g -> {
                    // comprovem que el joc té el nom del jugador correcte
                    return g.getPlayerName().equals("Alice")
                            && g.getBet() == 100
                            && g.getPlayerId() instanceof UUID;
                })
                .verifyComplete();
    }
}

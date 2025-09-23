package com.example.blackjack.repo;

import com.example.blackjack.domain.player.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends R2dbcRepository<Player, Long> {

    // leaderboard
    Flux<Player> findAllByOrderByWinsDescLossesAsc();

    // búsqueda por nombre
    Mono<Player> findByName(String name);

    // existe nombre
    Mono<Boolean> existsByName(String name);
}

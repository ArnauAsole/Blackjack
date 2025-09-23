package com.example.blackjack.repo;

import com.example.blackjack.domain.player.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends R2dbcRepository<Player, Long> {

    // leaderboard completo
    Flux<Player> findAllByOrderByWinsDescLossesAsc();

    // útiles:
    Mono<Boolean> existsByName(String name);
    Mono<Player> findByName(String name);

    // top N (opcional)
    @Query("SELECT * FROM player ORDER BY wins DESC, losses ASC LIMIT :limit")
    Flux<Player> findTop(@Param("limit") int limit);
}

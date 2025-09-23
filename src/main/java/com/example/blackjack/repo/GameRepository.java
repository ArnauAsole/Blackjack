package com.example.blackjack.repo;

import com.example.blackjack.domain.game.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}

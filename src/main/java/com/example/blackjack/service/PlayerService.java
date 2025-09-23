package com.example.blackjack.service;

import com.example.blackjack.domain.player.Player;
import com.example.blackjack.repo.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository players;

    public Mono<Player> create(String name) {
        String clean = name == null ? "" : name.trim();
        if (clean.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required"));
        }
        Player p = Player.builder()
                .id(UUID.randomUUID())
                .name(clean)
                .wins(0)
                .losses(0)
                .createdAt(Instant.now())
                .build();
        return players.save(p);
    }

    public Mono<Player> rename(UUID id, String newName){
        return players.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found")))
                .flatMap(p -> { p.setName(newName); return players.save(p); });
    }

    public Flux<Player> listAll()   {
        return players.findAll();
    }
}

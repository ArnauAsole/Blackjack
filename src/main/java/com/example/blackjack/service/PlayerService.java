package com.example.blackjack.service;

import com.example.blackjack.domain.player.Player;
import com.example.blackjack.repo.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository players;

    public Mono<Player> create(String name) {
        final String clean = name == null ? "" : name.trim();
        if (clean.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio"));
        }

        Player p = new Player();
        p.setName(clean);
        p.setWins(0);
        p.setLosses(0);
        p.setCreatedAt(Instant.now());

        // Primero comprobamos duplicado; si OK, guardamos y devolvemos el propio entity (R2DBC rellena el id)
        return players.existsByName(clean)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso"));
                    }
                    return players.save(p);
                })
                // por si hay condición de carrera con la UNIQUE del DB:
                .onErrorMap(DataIntegrityViolationException.class,
                        ex -> new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso", ex));
    }

    public Mono<Player> rename(Long id, String newName) {
        final String clean = newName == null ? "" : newName.trim();
        if (clean.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nuevo nombre es obligatorio"));
        }

        return players.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Jugador no encontrado")))
                .flatMap(player -> {
                    if (clean.equalsIgnoreCase(player.getName())) {
                        // Nada que cambiar
                        return Mono.just(player);
                    }
                    // Comprueba si el nombre ya existe en otro jugador
                    return players.findByName(clean)
                            .flatMap(existing -> {
                                if (existing != null && !existing.getId().equals(id)) {
                                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso"));
                                }
                                player.setName(clean);
                                return players.save(player);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                player.setName(clean);
                                return players.save(player);
                            }));
                })
                // por si el UNIQUE del DB salta igualmente:
                .onErrorMap(DataIntegrityViolationException.class,
                        ex -> new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso", ex));
    }

    public Flux<Player> listAll() {
        return players.findAllByOrderByWinsDescLossesAsc();
    }
}

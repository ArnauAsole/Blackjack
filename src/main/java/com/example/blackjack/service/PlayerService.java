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

<<<<<<< HEAD
        // Evita duplicado y, tras guardar, RELEER para devolver el id y el nombre correctos
=======
        // Primero comprobamos duplicado; si OK, guardamos y devolvemos el propio entity (R2DBC rellena el id)
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
        return players.existsByName(clean)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso"));
                    }
<<<<<<< HEAD
                    return players.save(p).then(players.findByName(clean));
                });
=======
                    return players.save(p);
                })
                // por si hay condición de carrera con la UNIQUE del DB:
                .onErrorMap(DataIntegrityViolationException.class,
                        ex -> new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso", ex));
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
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
<<<<<<< HEAD
                    // ¿ya existe alguien con ese nombre?
=======
                    // Comprueba si el nombre ya existe en otro jugador
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
                    return players.findByName(clean)
                            .flatMap(existing -> {
                                if (existing != null && !existing.getId().equals(id)) {
                                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Nombre ya en uso"));
                                }
                                player.setName(clean);
<<<<<<< HEAD
                                return players.save(player).then(players.findById(id));
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                player.setName(clean);
                                return players.save(player).then(players.findById(id));
=======
                                return players.save(player);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                player.setName(clean);
                                return players.save(player);
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
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

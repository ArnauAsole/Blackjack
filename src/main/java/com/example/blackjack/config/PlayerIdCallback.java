// src/main/java/com/example/blackjack/config/PlayerIdCallback.java
package com.example.blackjack.config;

import java.time.Instant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import reactor.core.publisher.Mono;
import com.example.blackjack.domain.player.Player;

@Configuration
public class PlayerIdCallback {

    @Bean
    public BeforeConvertCallback<Player> playerTimestamps() {
        return (player, table) -> {
            if (player.getCreatedAt() == null) {
                player.setCreatedAt(Instant.now());
            }
            // NO tocar player.setId(...)
            return Mono.just(player);
        };
    }
}

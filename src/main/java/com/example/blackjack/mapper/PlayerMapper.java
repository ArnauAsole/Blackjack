// src/main/java/com/example/blackjack/mapper/PlayerMapper.java
package com.example.blackjack.mapper;

import com.example.blackjack.domain.player.Player;
import com.example.blackjack.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerResponse toResponse(Player p) {
        if (p == null) return null;
        return new PlayerResponse(p.getId(), p.getName(), p.getWins(), p.getLosses());
    }
}

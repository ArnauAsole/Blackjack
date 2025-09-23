package com.example.blackjack.mapper;

import com.example.blackjack.domain.player.Player;
import com.example.blackjack.dto.response.PlayerResponse;

public class PlayerMapper {
    public static PlayerResponse toResponse(Player p) {
        return new PlayerResponse(p.getId(), p.getName(), p.getWins(), p.getLosses());
    }
}

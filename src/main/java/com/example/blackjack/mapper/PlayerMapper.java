package com.example.blackjack.mapper;

import com.example.blackjack.domain.player.Player;
import com.example.blackjack.dto.response.PlayerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerResponse toResponse(Player p);
}

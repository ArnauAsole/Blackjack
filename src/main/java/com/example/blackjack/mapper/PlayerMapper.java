// src/main/java/com/example/blackjack/mapper/PlayerMapper.java
package com.example.blackjack.mapper;

import org.mapstruct.*;
import com.example.blackjack.domain.player.Player;
import com.example.blackjack.dto.request.CreatePlayerRequest;
import com.example.blackjack.dto.request.RenamePlayerRequest;
import com.example.blackjack.dto.response.PlayerResponse;
<<<<<<< HEAD

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlayerMapper {

    @Mapping(target = "wins", source = "wins", defaultValue = "0")
    @Mapping(target = "losses", source = "losses", defaultValue = "0")
    PlayerResponse toResponse(Player source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wins", constant = "0")
    @Mapping(target = "losses", constant = "0")
    Player toEntity(CreatePlayerRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRename(RenamePlayerRequest req, @MappingTarget Player target);
=======
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerResponse toResponse(Player p) {
        if (p == null) return null;
        return new PlayerResponse(p.getId(), p.getName(), p.getWins(), p.getLosses());
    }
>>>>>>> ad32b6c (Changes in application.yml using org.springframework.r2dbc.connection.init: DEBUG)
}

package com.example.blackjack.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePlayerRequest(
        @NotBlank(message = "El nombre del jugador es obligatorio")
        String name
) {
}

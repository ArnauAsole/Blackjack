package com.example.blackjack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePlayerRequest(
        @NotBlank(message = "El nombre del jugador es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar {max} caracteres")
        String name
) {}

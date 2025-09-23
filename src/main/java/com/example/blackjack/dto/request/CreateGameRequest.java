package com.example.blackjack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateGameRequest(@NotBlank String playerName,
                                @PositiveOrZero Integer bet) { }

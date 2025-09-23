package com.example.blackjack.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RenamePlayerRequest(@NotBlank String name) { }

package com.example.blackjack.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PlayRequest(@NotBlank String action) {
} // "HIT" o "STAND"

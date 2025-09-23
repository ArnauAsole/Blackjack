package com.example.blackjack.dto.response;

public record PlayerResponse(
        Long id,
        String name,
        int wins,
        int losses
) {}

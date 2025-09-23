package com.example.blackjack.domain.player;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("player")
public class Player {
    @Id
    private Long id;              // <-- Long

    private String name;
    private int wins;
    private int losses;

    @Column("created_at")
    private Instant createdAt;
}


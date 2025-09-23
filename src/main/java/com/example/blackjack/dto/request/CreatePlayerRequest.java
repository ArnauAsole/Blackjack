import com.example.blackjack.domain.player.Player;
import com.example.blackjack.repo.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import java.time.Instant;

// DTO de entrada
public record CreatePlayerRequest(String name) {}

// Servicio
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository repo;

    public Mono<Player> create(String name) {
        Player p = new Player();
        p.setName(name);
        p.setWins(0);
        p.setLosses(0);
        p.setCreatedAt(Instant.now());
        // id = null -> INSERT y MySQL genera el auto_increment
        return repo.save(p);
    }
}

// Controller
@PostMapping("/player/new")
@ResponseStatus(HttpStatus.CREATED)
public Mono<Player> create(@RequestBody CreatePlayerRequest req) {
    return PlayerService.create(req.name());
}

package dev.chess.controller;

import dev.chess.entity.Game;
import dev.chess.entity.User;
import dev.chess.repository.GameRepository;
import dev.chess.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameReadController {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameReadController(GameRepository gameRepository,
                              UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Game>> listGames() {
        return ResponseEntity.ok(gameRepository.findAll());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return gameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    @Transactional(readOnly = true)
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/count")
    @Transactional(readOnly = true)
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(gameRepository.count());
    }
}
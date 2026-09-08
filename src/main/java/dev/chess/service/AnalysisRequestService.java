package dev.chess.service;

import dev.chess.entity.Game;
import dev.chess.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisRequestService {

    private final GameRepository gameRepository;

    public AnalysisRequestService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    public void requestAnalysis(Long gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new GameNotFoundException(gameId));
        game.setAnalysisRequested(true);
        gameRepository.save(game);
    }

    public static class GameNotFoundException extends RuntimeException {
        public GameNotFoundException(Long gameId) {
            super("Game not found: " + gameId);
        }
    }
}
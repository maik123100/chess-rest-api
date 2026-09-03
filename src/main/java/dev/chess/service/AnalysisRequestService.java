package dev.chess.service;

import dev.chess.entity.Game;
import dev.chess.repository.GameRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisRequestService {

    private final GameRepository gameRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AnalysisRequestService(GameRepository gameRepository,
                                  ApplicationEventPublisher eventPublisher) {
        this.gameRepository = gameRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Game requestAnalysis(Long gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new GameNotFoundException(gameId));
        game.setAnalysisRequested(true);
        gameRepository.save(game);
        eventPublisher.publishEvent(new AnalysisRequestedEvent(gameId));
        return game;
    }

    public static class GameNotFoundException extends RuntimeException {
        public GameNotFoundException(Long gameId) {
            super("Game not found: " + gameId);
        }
    }
}
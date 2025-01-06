package local.chess.chess_rest_api.service;

import local.chess.chess_rest_api.model.Game;
import local.chess.chess_rest_api.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findByOrderByDateDesc();
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
}
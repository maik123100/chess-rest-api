package local.chess.chessRestApi.service;

import local.chess.chessRestApi.model.Game;
import local.chess.chessRestApi.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GameService {

	private final GameRepository gameRepository;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	public Game getGameById(String uuid) {
		return gameRepository.findById(uuid).orElse(null);
	}
}

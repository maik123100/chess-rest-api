package local.chess.chessRestApi.service;

import local.chess.chessRestApi.model.Game;
import local.chess.chessRestApi.model.Analysis;
import local.chess.chessRestApi.repository.GameRepository;
import local.chess.chessRestApi.repository.AnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class GameService {

	private final GameRepository gameRepository;
	private final AnalysisRepository analysisRepository;

	public GameService(GameRepository gameRepository, AnalysisRepository analysisRepository) {
		this.analysisRepository = analysisRepository;
		this.gameRepository = gameRepository;
	}

	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	public Game getGameById(String uuid) {
		return gameRepository.findById(uuid).orElse(null);
	}

	public List<Game> getLastNGames(int n) {
		return gameRepository.findLastNGames(n);
	}

	public Optional<Map<Game, List<Analysis>>> getGameAnalysisById(String uuid) {
		Optional<Game> game = gameRepository.findById(uuid);
		if (game.isPresent()) {
			List<Analysis> analysisList = analysisRepository.findByGameUuid(uuid);
			Map<Game, List<Analysis>> result = new HashMap<>();
			result.put(game.get(), analysisList.isEmpty() ? Collections.emptyList() : analysisList);
			return Optional.of(result);
		}
		return Optional.empty();
	}
}

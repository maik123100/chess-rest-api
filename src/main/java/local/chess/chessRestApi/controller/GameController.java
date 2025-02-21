package local.chess.chessRestApi.controller;

import java.util.List;
import local.chess.chessRestApi.model.Game;
import local.chess.chessRestApi.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import local.chess.chessRestApi.service.GameService;

@RestController
@RequestMapping("/api/games")
public class GameController {

	private final GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping
	public List<Game> getAllGames() {
		return gameService.getAllGames();
	}

	@GetMapping("/{uuid}")
	public Game getGameById(@PathVariable String uuid) {
		return gameService.getGameById(uuid);
	}
}

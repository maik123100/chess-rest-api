package local.chess.chessRestApi.repository;

import local.chess.chessRestApi.model.Game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, String> {

	@Query(value = "SELECT * FROM games ORDER BY uuid DESC LIMIT ?1", nativeQuery = true)
	List<Game> findLastNGames(int n);
}

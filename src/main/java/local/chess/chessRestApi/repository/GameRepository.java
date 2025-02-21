package local.chess.chessRestApi.repository;

import local.chess.chessRestApi.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {

}
